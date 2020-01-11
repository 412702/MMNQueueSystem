package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.Event;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.EventScheduler;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.SystemEvent.EventFinishEvent;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.SystemEvent.EventFinishEventListener;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.RandomNumber.ExponentialDistribution;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

public class MMNQueuingSystem extends EventScheduler {

    public final long N; //服务台数
    public final double arriveRate; //到达率
    public final double serviceRate;    //服务率
    public final long queueLengthLimit; //最大队列长度
    public final ExponentialDistribution arriveExponentialDistribution;   //顾客到达指数分布随机数发生器
    public final ExponentialDistribution serviceExponentialDistribution; //服务事件指数分布随机数发生器
    public final double finishTime;    //模拟结束时间

    public long queueLength; //排队中的顾客数
    public long usedServer; //使用中的服务台数
    public long rejectedCustomer;   //因队列长度不足而离开的顾客数

    private Recorder recorder;  //记录器
    private MMNQueuingSystem self;

    /**
     * 用于记录的监听器
     */
    private EventFinishEventListener recordListener=(EventFinishEvent e)->{
        recorder.addState(self.getSystemTime(), self.queueLength, self.usedServer, self.rejectedCustomer, e.getEvent());
    };

    /**
     * 初始化模拟系统
     *
     * @param n 服务台数量
     * @param arriveRate 到达率
     * @param serviceRate 服务率
     * @param seed1 顾客到达指数分布随机数发生器种子
     * @param seed2 服务事件指数分布随机数发生器种子
     * @param queueLengthLimit 最大队列长度
     * @param finishTime 模拟结束时间
     * @param sleepTime 每个模拟系统时间后的暂停时间（毫秒）
     */
    public MMNQueuingSystem(long n,double arriveRate,double serviceRate,long seed1,long seed2,long queueLengthLimit,double finishTime,int sleepTime) {
        //事件排序规则
        super(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                double o1Time=o1.getInvokeTime();
                double o2Time=o2.getInvokeTime();
                if(o1Time!=o2Time){
                    if(o1Time-o2Time>0){
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
                else {
                    int o1Priority=getPriority(o1);
                    int o2Priority=getPriority(o2);
                    return o1Priority-o2Priority;
                }
            }

            int getPriority(Event o){
                if(o.getClass().getName().equals(SimulationFinishEvent.class.getName())){
                    return 1;
                }
                if(o.getClass().getName().equals(FinishServiceEvent.class.getName())){
                    return 2;
                }
                else if(o.getClass().getName().equals(CustomerArriveEvent.class.getName())){
                    return 3;
                }
                else {
                    return 4;
                }
            }
        },sleepTime);

        //初始化参数
        N=n;
        this.arriveRate=arriveRate;
        this.serviceRate=serviceRate;
        this.queueLengthLimit=queueLengthLimit;
        this.arriveExponentialDistribution=new ExponentialDistribution(seed1, arriveRate);
        this.serviceExponentialDistribution=new ExponentialDistribution(seed2, serviceRate);
        this.finishTime=finishTime;

        queueLength=0;
        usedServer=0;
        rejectedCustomer=0;

        recorder=new Recorder(this);
        self=this;

        //添加记录器记录每次事件发生后的系统状态
        this.addEventFinishEventListener(recordListener);
    }

    /**
     * 开始模拟
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public void Start() throws InvocationTargetException, IllegalAccessException {
        this.getEventList().add(new CustomerArriveEvent(0));
        this.getEventList().add(new SimulationFinishEvent(this.finishTime));
        super.Start();
    }

    /**
     * 将模拟过程及统计结果输出到EXCEL文件中
     * @param outputStream 目标输出流
     * @return 运行结果
     */
    public boolean wirteRecordsInEXCEL(OutputStream outputStream){
        return this.recorder.write(outputStream);
    }
}

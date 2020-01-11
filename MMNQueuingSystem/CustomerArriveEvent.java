package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.Event;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.EventScheduler;

public class CustomerArriveEvent extends Event {
    public CustomerArriveEvent(double invokeTime) {
        super(invokeTime);
        setType("CustomerArriveEvent");
    }

    /**
     * 该类型事件的处理方法
     *
     * @param eventScheduler 事件调度器对象，可用于获取系统状态，操作时间表，结束模拟等
     */
    @Override
    public void Invoke(EventScheduler eventScheduler) {
        MMNQueuingSystem mmnQueuingSystem=(MMNQueuingSystem)eventScheduler;
        if(mmnQueuingSystem.queueLength<mmnQueuingSystem.queueLengthLimit){
            //队列不满
            if(mmnQueuingSystem.usedServer<mmnQueuingSystem.N){
                //有空闲服务台
                double newFinishTime=this.getInvokeTime()+mmnQueuingSystem.serviceExponentialDistribution.next();
                mmnQueuingSystem.usedServer++;
                mmnQueuingSystem.getEventList().add(new FinishServiceEvent(newFinishTime));
            }
            else {
                //无空闲服务台，排队
                mmnQueuingSystem.queueLength++;
            }
        }
        else {
            //队列已满
            mmnQueuingSystem.rejectedCustomer++;
        }
        double newInvokeTime=this.getInvokeTime()+mmnQueuingSystem.arriveExponentialDistribution.next();
        mmnQueuingSystem.getEventList().add(new CustomerArriveEvent(newInvokeTime));
    }
}

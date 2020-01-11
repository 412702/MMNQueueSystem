package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.Event;

/**
 * 用于表示模拟系统在某时刻的状态
 */
public class SystemStateEntity {
    public double systemTime;   //系统时间
    public long queueLength; //排队中的顾客数
    public long usedServer; //使用中的服务台数
    public long rejectedCustomer;   //因队列长度不足而离开的顾客数
    public Event eventLastHappend;  //刚发生的事件（认为只有事件发生才会影响系统状态）

    public SystemStateEntity(double systemTime,long queueLength,long usedServer,long rejectedCustomer,Event event){
        this.systemTime=systemTime;
        this.queueLength=queueLength;
        this.usedServer=usedServer;
        this.rejectedCustomer=rejectedCustomer;
        this.eventLastHappend=event;
    }
}

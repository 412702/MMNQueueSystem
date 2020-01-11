package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.Event;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.EventScheduler;

public class FinishServiceEvent extends Event {
    public FinishServiceEvent(double invokeTime) {
        super(invokeTime);
        setType("FinishServiceEvent");
    }

    /**
     * 该类型事件的处理方法
     *
     * @param eventScheduler 事件调度器对象，可用于获取系统状态，操作时间表，结束模拟等
     */
    @Override
    public void Invoke(EventScheduler eventScheduler) {
        MMNQueuingSystem mmnQueuingSystem=(MMNQueuingSystem)eventScheduler;
        mmnQueuingSystem.usedServer--;
        if(mmnQueuingSystem.queueLength>0){
            mmnQueuingSystem.queueLength--;
            mmnQueuingSystem.usedServer++;
            double newFinishTime=this.getInvokeTime()+mmnQueuingSystem.serviceExponentialDistribution.next();
            mmnQueuingSystem.getEventList().add(new FinishServiceEvent(newFinishTime));
        }
    }
}

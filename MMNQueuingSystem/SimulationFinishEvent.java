package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.Event;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.EventScheduler;

public class SimulationFinishEvent extends Event {
    public SimulationFinishEvent(double invokeTime){
        super(invokeTime);
        setType("SimulationFinishEvent");
    }

    /**
     * 该类型事件的处理方法
     *
     * @param eventScheduler 事件调度器对象，可用于获取系统状态，操作时间表，结束模拟等
     */
    @Override
    public void Invoke(EventScheduler eventScheduler) {
        eventScheduler.Finish();
    }
}

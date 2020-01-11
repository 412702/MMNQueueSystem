package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler;

public abstract class Event {
    private double invokeTime;

    //标识该事件类型
    private String Type;

    public Event(double invokeTime){
        this.invokeTime=invokeTime;
    }

    public double getInvokeTime() {
        return invokeTime;
    }

    public String getType() {
        return Type;
    }

    protected void setType(String type) {
        Type = type;
    }

    /**
     * 该类型事件的处理方法
     * @param eventScheduler 事件调度器对象，可用于获取系统状态，操作时间表，结束模拟等
     */
    public abstract void Invoke(EventScheduler eventScheduler);

    @Override
    public String toString(){
        return "Event Type: "+Type+", Time: "+invokeTime;
    }
}

package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.SystemEvent;

public class SystemTimePushEvent {
    private double oldTime;
    private double newTime;

    public SystemTimePushEvent(double oldTime,double newTime){
        this.oldTime=oldTime;
        this.newTime=newTime;
    }

    public double getOldTime() {
        return oldTime;
    }

    public void setOldTime(double oldTime) {
        this.oldTime = oldTime;
    }

    public double getNewTime() {
        return newTime;
    }

    public void setNewTime(double newTime) {
        this.newTime = newTime;
    }
}

package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.SystemEvent;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.Event;

public class EventFinishEvent {
    Event event;

    public EventFinishEvent(Event event){
        this.event=event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}

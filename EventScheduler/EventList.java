package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class EventList {
    private List<Event> eventList;
    Comparator<Event> comparator;

    public EventList(Comparator<Event> comparator){
        eventList=new LinkedList<>();
        this.comparator=comparator;
    }

    public void add(Event event){
        eventList.add(event);
        eventList.sort(comparator);
    }

    public Event Pop() throws IndexOutOfBoundsException{
        Event event=eventList.get(0);
        eventList.remove(0);
        return event;
    }

    public boolean isEmpty(){
        return eventList.isEmpty();
    }


}

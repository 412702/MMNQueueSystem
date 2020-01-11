package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler;


import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.SystemEvent.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

public abstract class EventScheduler {

    private EventList eventList;

    private double systemTime;  //系统时间
    private boolean isFinish;   //模拟结束标识

    private SystemEventManager initEventManager;
    private SystemEventManager getNextEventEventManger;
    private SystemEventManager systemTimePushEventManager;
    private SystemEventManager eventStartEventManager;
    private SystemEventManager eventFinishEventManager;
    private SystemEventManager finishEventManager;
    private int sleepTime;  //每个系统事件触发后的暂停时间

    public double getSystemTime() {
        return systemTime;
    }

    public EventList getEventList() {
        return eventList;
    }

    /**
     * 初始化模拟系统
     * @param comparable 事件间执行顺序比较方法
     * @param sleepTime 每个模拟系统时间后的暂停时间（毫秒）
     */
    public EventScheduler(Comparator<Event> comparable,int sleepTime){
        this.eventList=new EventList((Comparator<Event>) comparable);

        this.systemTime=0;
        this.isFinish=false;

        this.initEventManager=new SystemEventManager(InitEvent.class, InitEventListener.class,sleepTime);
        this.getNextEventEventManger=new SystemEventManager(GetNetEventEvent.class, GetNextEventEventListener.class,sleepTime);
        this.systemTimePushEventManager=new SystemEventManager(SystemTimePushEvent.class, SystemTimePushEventListener.class,sleepTime);
        this.eventStartEventManager=new SystemEventManager(EventStartEvent.class, EventStartEVentListener.class,sleepTime);
        this.eventFinishEventManager=new SystemEventManager(EventFinishEvent.class, EventFinishEventListener.class,sleepTime);
        this.finishEventManager=new SystemEventManager(FinishEvent.class, FinishEventListener.class,sleepTime);

        this.sleepTime=0;
    }

    /**
     * 添加初始化事件监听器
     * @param listener 监听器
     */
    public void addInitEventListener(InitEventListener listener){
        initEventManager.addListener(listener);
    }

    /**
     * 添加获取下一个事件监听器
     * @param listener 监听器
     */
    public void addGetNextEventEventListener(GetNextEventEventListener listener){
        getNextEventEventManger.addListener(listener);
    }

    /**
     * 添加系统模拟时间推进监听器
     * @param listener 监听器
     */
    public void addSystemTimePushEventListener(SystemTimePushEventListener listener){
        systemTimePushEventManager.addListener(listener);
    }

    /**
     * 添加事件开始执行监听器
     * @param listener 监听器
     */
    public void addEventStartEVentListener(EventStartEVentListener listener){
        eventStartEventManager.addListener(listener);
    }

    /**
     * 添加事件执行结束监听器
     * @param listener 监听器
     */
    public void addEventFinishEventListener(EventFinishEventListener listener){
        eventFinishEventManager.addListener(listener);
    }

    /**
     * 添加模拟结束监听器
     * @param listener 监听器
     */
    public void addFinishEventListener(FinishEventListener listener){
        finishEventManager.addListener(listener);
    }


    /**
     * 开始模拟
     */
    public void Start() throws InvocationTargetException, IllegalAccessException {
        initEventManager.invoke(new InitEvent());
        while ((! eventList.isEmpty()) && (! isFinish)){
            Event event=eventList.Pop();
            getNextEventEventManger.invoke(new GetNetEventEvent(event));
            if(this.systemTime<event.getInvokeTime()){
                systemTimePushEventManager.invoke(new SystemTimePushEvent(systemTime, event.getInvokeTime()));
                systemTime=event.getInvokeTime();
            }
            eventStartEventManager.invoke(new EventStartEvent(event));
            event.Invoke(this);
            eventFinishEventManager.invoke(new EventFinishEvent(event));
        }
        finishEventManager.invoke(new FinishEvent());
    }

    /**
     * 终止模拟
     */
    public void Finish(){
        isFinish=true;
    }

    public int getSleepTime() {
        return sleepTime;
    }
}

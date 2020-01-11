package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.EventScheduler.SystemEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SystemEventManager {
    private Class eventType;
    private Class listenerType;

    private List listeners;
    private int sleepTime;  //每个事件的所有监听器执行完成后的暂停时间

    public SystemEventManager(Class eventType,Class listenerType){
        this.eventType=eventType;
        this.listenerType=listenerType;

        listeners=new ArrayList();
        sleepTime=0;
    }

    public SystemEventManager(Class eventType,Class listenerType,int sleepTime){
        this(eventType,listenerType);
        this.sleepTime=sleepTime;
    }

    /**
     * 添加监听器
     * @param listener 监听器
     */
    public void addListener(Object listener){
        Class listenerClass=listener.getClass();
        Class[] classes=listenerClass.getInterfaces();
        boolean suitableType=false;
        for(Class aclass:classes){
            if(aclass.getName().equals(listenerType.getName())){
                suitableType=true;
                break;
            }
        }
        if(!suitableType){
            return;
        }
        Method[] methods=listenerClass.getMethods();
        for(Method method:methods){
            Class[] parameterTypes=method.getParameterTypes();
            if(parameterTypes.length==1 && parameterTypes[0].getName().equals(eventType.getName())){
                listeners.add(listener);
                break;
            }
        }
    }

    /**
     * 执行添加到该事件的所有监听器
     * @param event 事件
     */
    public void invoke(Object event) throws InvocationTargetException, IllegalAccessException {
        if(!eventType.getName().equals(event.getClass().getName())){
            return;
        }
        for(Object listener:listeners){
            Class listenerClass=listener.getClass();
            Method[] methods=listenerClass.getMethods();
            for(Method method:methods){
                Class[] parameterTypes=method.getParameterTypes();
                if(parameterTypes.length==1 && parameterTypes[0].getName().equals(eventType.getName())){
                    method.setAccessible(true);
                    method.invoke(listener, event);
                }
            }
        }
        //暂停
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

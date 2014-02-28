package org.ybygjy.pattern.proxy.tracer;

import java.awt.Component;
import java.awt.Container;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ����¼���Ϣ�����������Ӧ���������Java���ļ�����I���Լ���ƪ
 * @author WangYanCheng
 * @version 2011-1-26
 */
public class EventTracer {
    /** Dynamic Proxy */
    private InvocationHandler handler;

    /**
     * Constructor
     */
    public EventTracer() {
        handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method + ":" + args[0]);
                return null;
            }
        };
    }
    /**
     * ����JavaBean#Introspector���ƽ�����ȡ����ṹ������ȡ�Ľṹ��Ϣ��Proxy���ư�
     * @param c ���ʵ��
     */
    public void add(Component c) {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(c.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        EventSetDescriptor[] eventSets = info.getEventSetDescriptors();
        for (EventSetDescriptor eventSet : eventSets) {
            addListener(c, eventSet);
        }
        if (c instanceof Container) {
            for (Component comp : ((Container) c).getComponents()) {
                add(comp);
            }
        }
    }
    /**
     * ����JavaBean��ȡ�Ľṹʵ������Proxy���ƽ�����װ�Ӷ��𵽼������ص�����
     * @param c ����¼����
     * @param eventSet �¼�����ʵ�����洢���¼��ӿ���Ϣ���¼�������¼����ݵ�
     */
    public void addListener(Component c, EventSetDescriptor eventSet) {
        Object proxy = Proxy.newProxyInstance(null, new Class[] {eventSet.getListenerType()}, handler);
        Method addListenerMethod = eventSet.getAddListenerMethod();
        try {
            addListenerMethod.invoke(c, proxy);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

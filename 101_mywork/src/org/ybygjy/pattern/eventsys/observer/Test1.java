package org.ybygjy.pattern.eventsys.observer;


/**
 * Java2ƽ̨�¼��������-->Observerģʽ-->�������
 * @author WangYanCheng
 * @version 2010-2-20
 */
public class Test1 {
    /**
     * �������
     * @param args args
     */
    public static void main(String[] args) {
        boolean isDebug = true;
        Thermostat tsInst = new Thermostat(isDebug);
        IndoorTemperature itInst = new IndoorTemperature(isDebug);
        itInst.addObserver(tsInst);
    }
}

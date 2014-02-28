package org.ybygjy.pattern.eventsys.jbmulticastevent;
/**
 * �¼�����-->JavaBeanģʽ-->Multicast Event Pattern
 * @author WangYanCheng
 * @version 2010-2-20
 */
public class Test {
    /**
     * �������
     * @param args args
     */
    public static void main(String[] args) {
        TemperatureChangeListener tclInst = new Thermostat(true);
        IndoorTemperature itInst = new IndoorTemperature(true);
        itInst.addTemperatureChangeListener(tclInst);
    }
}

package org.ybygjy.pattern.eventsys.jbboundproperty;
/**
 * �¼�����-->JavaBeanģʽ-->Bound Property Pattern
 * @author WangYanCheng
 * @version 2010-2-20
 */
public class Test {
    /**
     * �������
     * @param args args
     */
    public static void main(String[] args) {
        Thermostat therInst = new Thermostat(true);
        IndoorTemperature itInst = new IndoorTemperature(true);
        itInst.addPropertyChangeListener(therInst);
    }
}

package org.ybygjy.pattern.eventsys.service.esdecoupe;


/**
 * �¼��������-->JavaBean�¼�ģʽ-->���¼������������
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
        new DownstairsTemperature(true);
        new UpstairsTemperature(true);
        EventServiceImpl.getServiceInstance().addTemperatureChangeListener(therInst);
    }
}

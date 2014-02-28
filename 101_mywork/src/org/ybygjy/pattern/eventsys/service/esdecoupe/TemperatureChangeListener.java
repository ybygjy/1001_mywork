package org.ybygjy.pattern.eventsys.service.esdecoupe;

import java.util.EventListener;

/**
 * �¼��������-->JavaBean�¼�����-->���¼������������<br>
 * �������ӿ�
 * @author WangYanCheng
 * @version 2010-2-20
 */
public interface TemperatureChangeListener extends EventListener {
    /**
     * monitor the temperature change listener
     * @param tce tce
     */
    void updateTemperature(TemperatureChangeEvent tce);
}

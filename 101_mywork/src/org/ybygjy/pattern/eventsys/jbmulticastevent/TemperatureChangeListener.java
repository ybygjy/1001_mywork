package org.ybygjy.pattern.eventsys.jbmulticastevent;

import java.util.EventListener;

/**
 * �¼�����-->JavaBeanģʽ-->Multicast Event<br>
 * �������ӿ�
 * @author WangYanCheng
 * @version 2010-2-20
 */
public interface TemperatureChangeListener extends EventListener {
    /**
     * updateTemperature
     * @param tceInst tceInst
     */
    void updateTemperature(TemperatureChangeEvent tceInst);
}

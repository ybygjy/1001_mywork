package org.ybygjy.pattern.eventsys.service.esrmi.listener;

import java.rmi.RemoteException;

import org.ybygjy.pattern.eventsys.service.esrmi.module.TemperatureChangeEvent;

/**
 * �������ӿ�
 * @author WangYanCheng
 * @version 2010-7-23
 */
public interface TemperatureChangeListener extends java.util.EventListener, java.rmi.Remote {
    /**
     * �¶��¼�����
     * @param evtInst evtInst
     * @throws RemoteException RemoteException
     */
    void updateTemperature(TemperatureChangeEvent evtInst) throws RemoteException;
}

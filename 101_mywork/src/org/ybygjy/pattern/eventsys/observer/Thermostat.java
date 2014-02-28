package org.ybygjy.pattern.eventsys.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * Java2ƽ̨�¼��������-->Observerģʽ
 * @author WangYanCheng
 * @version 2010-2-20
 */
public class Thermostat implements Observer {
    /** is debug */
    private boolean debug = false;

    /**
     * Constructor
     * @param debug is debug
     */
    public Thermostat(boolean debug) {
        this.debug = debug;
    }

    /**
     * {@inheritDoc}
     */
    public void update(Observable o, Object arg) {
        if (debug) {
            System.out.println("Instance of IndoorTemperature-->"
                    + (o instanceof IndoorTemperature));
        }
    }
}

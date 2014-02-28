package org.ybygjy.pattern.eventsys.jbmulticastevent;
/**
 * �¼�����-->JavaBeanģʽ-->Multicast Event
 * @author WangYanCheng
 * @version 2010-2-20
 */
public class Thermostat implements TemperatureChangeListener {
    /**is debug*/
    private boolean debug = false;
    /**
     * Constructor
     * @param debug debug
     */
    public Thermostat(boolean debug) {
        this.debug = debug;
    }
    /**
     * update Temperature
     * @param tceInst tceInst
     */
    public void updateTemperature(TemperatureChangeEvent tceInst) {
        if (debug) {
            String debugTmpl = "�¼�Դ{@ES@},PropagationId{@PI@},��ֵ{@NV@},��ֵ{@OV@}";
            System.out.println(debugTmpl.replaceAll("@ES@", tceInst.getSource().toString())
                    .replaceAll("@NV@", tceInst.getNewValue().toString())
                    .replaceAll("@OV@", tceInst.getOldValue().toString())
                    );
        }
    }
}

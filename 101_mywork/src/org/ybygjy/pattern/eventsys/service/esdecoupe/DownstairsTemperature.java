package org.ybygjy.pattern.eventsys.service.esdecoupe;
/**
 * �¼��������-->JavaBean�¼�ģʽ-->���¼������������<br>
 * �½��¶ȿ���
 * @author WangYanCheng
 * @version 2010-2-20
 */
public class DownstairsTemperature extends TemperatureGauge {
    /**uid*/
    private static final long serialVersionUID = -3076358185041446755L;
    /**
     * Constructor
     * @param debug debug
     */
    public DownstairsTemperature(boolean debug) {
        super(debug);
    }
    /* (non-Javadoc)
     * @see org.ybygjy.pattern.eventsys.service.basic.TemperatureGauge#getLocation()
     */
    @Override
    public String getLocation() {
        return "DownstairsTemperature";
    }
    /* (non-Javadoc)
     * @see org.ybygjy.pattern.eventsys.service.basic.TemperatureGauge#getMaxinum()
     */
    @Override
    public int getMaxinum() {
        return 70;
    }
    /* (non-Javadoc)
     * @see org.ybygjy.pattern.eventsys.service.basic.TemperatureGauge#getMininum()
     */
    @Override
    public int getMininum() {
        return 60;
    }
}

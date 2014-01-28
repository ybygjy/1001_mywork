package org.ybygjy.ui.listener;

import java.util.EventObject;

/**
 * �����¼�����
 * @author WangYanCheng
 * @version 2012-9-7
 */
public class UIEvent extends EventObject {
    /**
     * serial number
     */
    private static final long serialVersionUID = 831280961473656832L;
    /**����ʼִ��*/
    private static final int EVE_WORKSTART = 2000;
    /**����ִ�����*/
    private static final int EVE_WORKFINISH = 2001;
    /***/
    private static final int UI_EVE_C = 2002;
    /** �¼�ID */
    private int eventType;

    /**
     * ���캯��
     * @param source �¼�Դ
     */
    public UIEvent(Object source) {
        super(source);
    }

    /**
     * ���캯��
     * @param source �¼�Դ
     * @param eventId �¼�����
     * @param arg ����
     */
    public UIEvent(Object source, int eventId, Object arg) {
        super(source);
    }

    /**
     * ȡ���¼�ID
     * @return eventType {@linkplain UIEvent#eventType}
     */
    public int getEventType() {
        return this.eventType;
    }
}

package org.ybygjy.gui.springswing.comp;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * ������չJButton���
 * @author WangYanCheng
 * @version 2011-2-16
 */
@SuppressWarnings("serial")
public class ActionListenerButton extends JButton {
    /** �¼������� */
    private ActionListener actionListener;

    /**
     * ����¼�������
     * @param actionListener �¼�������
     */
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * ��ʼ��
     */
    public void init() {
        this.addActionListener(this.actionListener);
    }
}

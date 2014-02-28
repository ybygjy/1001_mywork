package org.ybygjy.gui.springswing.comp;

import java.awt.Component;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * ������Ӧ�ó����ĳ������BoxLayout���ֵ����
 * @author WangYanCheng
 * @version 2011-2-16
 */
@SuppressWarnings("serial")
public class BoxLayoutPanel extends JPanel {
    /** boxlayout������չ��ز��� {��X/Y����չ}*/
    private int axis;
    /** ��ǰ���Ԫ�ؼ��� */
    private List panelComponents;

    /**
     * setter Axis
     * @param axis axis
     */
    public void setAxis(int axis) {
        this.axis = axis;
        this.repaint();
    }

    /**
     * setter panelComponents
     * @param panelComponents �������
     */
    public void setPanelComponents(List panelComponents) {
        this.panelComponents = panelComponents;
    }

    /**
     * ��ʼ��
     */
    public void init() {
        setLayout(new BoxLayout(this, axis));
        for (Iterator iterator = panelComponents.iterator(); iterator.hasNext();) {
            Component comp = (Component) iterator.next();
            add(comp);
        }
    }
}

package org.ybygjy.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * ��װ�������������ڼ��й���
 * @author WangYanCheng
 * @version 2012-9-4
 */
public class UIUtils {
    /**
     * ��������������JLabel����
     * @param container ��������
     * @param jLabelName ��ǩ����
     * @param gblInst {@link GridBagLayout}
     * @param gbcInst {@link GridBagConstraints}
     */
    public static JLabel makeJLabel(Container container, String jLabelName, GridBagLayout gblInst,
                            GridBagConstraints gbcInst) {
        gbcInst.weightx = 0;
        JLabel jLabel = new JLabel(jLabelName);
        gblInst.setConstraints(jLabel, gbcInst);
        container.add(jLabel);
        return jLabel;
    }

    /**
     * ��������������JTextField����
     * @param container ��������
     * @param defValue Ĭ��ֵ
     * @param gblInst {@link GridBagLayout}
     * @param gbcInst {@link GridBagConstraints}
     */
    public static JTextField makeJTextField(Container container, String defValue, GridBagLayout gblInst,
                                GridBagConstraints gbcInst) {
        gbcInst.ipadx = 150;
        gbcInst.weightx = 0.3;
        JTextField jTextField = new JTextField(defValue);
        gblInst.setConstraints(jTextField, gbcInst);
        container.add(jTextField);
        gbcInst.ipadx = 0;
        return jTextField;
    }

    /**
     * �������������Ӹ��������ʹ��GridBagLayout����
     * @param container {@link Container}
     * @param compInst ���{@link JComponent}
     * @param gblInst {@link GridBagLayout}
     * @param gbcInst {@link GridBagConstraints}
     */
    public static void addGBLComp(Container container, Container compInst,
                                  GridBagLayout gblInst, GridBagConstraints gbcInst) {
        gblInst.setConstraints(compInst, gbcInst);
        container.add(compInst);
    }

    /**
     * ��������������JButton���
     * @param container ��������
     * @param btnText ��ť��ʾ����
     * @param gblInst {@link GridBagLayout}
     * @param gbcInst {@link GridBagConstraints}
     * @return rtnBtn {@link JButton}
     */
    public static JButton makeJButton(Container container, String btnText, GridBagLayout gblInst,
                                   GridBagConstraints gbcInst) {
        JButton rtnBtn = new JButton(btnText);
        gblInst.setConstraints(rtnBtn, gbcInst);
        container.add(rtnBtn);
        return rtnBtn;
    }
}

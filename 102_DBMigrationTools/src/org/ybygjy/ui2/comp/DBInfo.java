package org.ybygjy.ui2.comp;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * ���ݿ���Ϣ¼��ģ��
 * <p>
 * 1���ṩ���ݼ���Ϣ¼��UI
 * <p>
 * 2���ṩ�洢���ݿ���Ϣ��ģ��
 * @author WangYanCheng
 * @version 2012-10-29
 */
public class DBInfo {
    /** ���� */
    private String catalogName;

    public String getCatalogName() {
        return this.catalogName;
    }

    public void setterCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    /**
     * �������ݿ���Ϣ¼��UI���
     * @return
     */
    public JPanel createUI() {
        JPanel rtnJPanel = new JPanel();
        rtnJPanel.setBorder(new TitledBorder(getCatalogName()));
        rtnJPanel.setLayout(new GridBagLayout());
        int gridy = 0, weighty = 0;
        addItem(rtnJPanel, "��     ����", createDriverBox(), gridy++, weighty);
        addItem(rtnJPanel, "�û�����", new JTextField(), gridy++, weighty);
        addItem(rtnJPanel, "��     �", new JPasswordField(), gridy++, weighty);
        addItem(rtnJPanel, "���Ӵ���", new JTextField(), gridy++, weighty);
        return rtnJPanel;
    }

    /**
     * ����ѡ��
     * @return jcbInst {@link JComboBox}
     */
    private JComboBox createDriverBox() {
        JComboBox jcbInst = new JComboBox();
        jcbInst.addItem("oracle.jdbc.driver.OracleDriver");
        jcbInst.addItem("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return jcbInst;
    }

    /**
     * ������
     * @param panel ������
     * @param labelName ���ֵ�Ԫ������
     * @param comp Ԫ�ض���
     * @param gridy {@link GridBagLayout} �������������
     * @param weighty {@link GridBagLayout} ����ռ䲼����չȨ��
     * @return rtnGBC {@link GridBagLayout}
     */
    private GridBagConstraints addItem(JPanel panel, String labelName, Component comp, int gridy,
                                       int weighty) {
        GridBagConstraints gbcInst = new GridBagConstraints();
        gbcInst.gridwidth = 1;
        gbcInst.gridheight = 1;
        gbcInst.gridx = 0;
        gbcInst.gridy = gridy;
        gbcInst.weightx = 0;
        gbcInst.fill = GridBagConstraints.NORTHEAST;
        panel.add(new JLabel(labelName), gbcInst);
        gbcInst.gridx = 1;
        gbcInst.weightx = 1;
        if (weighty > 0) {
            gbcInst.weighty = weighty;
        }
        gbcInst.fill = GridBagConstraints.BOTH;
        panel.add(comp, gbcInst);
        return gbcInst;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame jframe = new JFrame();
                jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jframe.setSize(300, 400);
                jframe.setLayout(new FlowLayout());
                jframe.getContentPane().add(new DBInfo().createUI());
                jframe.setVisible(true);
            }
        });
    }
}

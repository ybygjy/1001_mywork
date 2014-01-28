package org.ybygjy.ui.comp;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.ybygjy.ui.UIUtils;

/**
 * ���ݿ�������Ϣ
 * @author WangYanCheng
 * @version 2012-9-4
 */
public class DBInfo extends JPanel implements ComponentValue{
    /**
     * serial number
     */
    private static final long serialVersionUID = -7909433433427122813L;
    /** ����Դ���ݿ����� */
    private int srcType;
    /** Դ���ݿ����Ӵ� */
    private JTextField srcConnURL;
    /** Ŀ�����ݿ����Ӵ� */
    private JTextField tarConnURL;
    /** ����Ŀ�����ݿ����� */
    private int tarType;

    /**
     * ���캯��
     */
    public DBInfo() {
        this.setBorder(new TitledBorder("���ݿ���Ϣ"));
        GridBagLayout gblInst = new GridBagLayout();
        this.setLayout(gblInst);
        GridBagConstraints gbcInst = new GridBagConstraints();
        //gbcInst.insets = new Insets(1, 1, 1, 1);
        gbcInst.fill = GridBagConstraints.HORIZONTAL;
        gbcInst.gridx = 0;
        gbcInst.gridy = 0;
        gbcInst.weightx = 10;
        UIUtils.makeJLabel(this, "Դ���ݿ⣺", gblInst, gbcInst);
        gbcInst.gridx = 1;
        gbcInst.gridy = 0;
        gbcInst.weightx = 90;
        this.srcConnURL = UIUtils.makeJTextField(this, "���Ӵ�", gblInst, gbcInst);
        gbcInst.gridx = 0;
        gbcInst.gridy = 1;
        gbcInst.weightx = 10;
        UIUtils.makeJLabel(this, "Ŀ�����ݿ⣺", gblInst, gbcInst);
        gbcInst.gridx = 1;
        gbcInst.gridy = 1;
        gbcInst.weightx = 90;
        this.tarConnURL = UIUtils.makeJTextField(this, "���Ӵ�", gblInst, gbcInst);
        //this.srcConnURL.setText("jdbc:sqlserver://192.1.116.97:1433;databaseName=NsAg;user=sa;password=11111111");
        this.srcConnURL.setText("jdbc:sqlserver://192.168.0.16;databaseName=nsag;user=nsag;password=11111111;instanceName=sql2005");
        //this.tarConnURL.setText("jdbc:oracle:thin:NSTCSMS825/6316380@192.1.116.204:1521/NSDEV");
        this.tarConnURL.setText("jdbc:oracle:thin:NSTCSA1421/475656@192.168.0.143:1521/NSDEV");
    }

    @Override
    public Map<String, String> getValues() {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("dbinfo.srcConnURL", this.srcConnURL.getText());
        rtnMap.put("dbinfo.tarConnURL", this.tarConnURL.getText());
        rtnMap.put("dbinfo.tarType", String.valueOf(this.tarType));
        rtnMap.put("dbinfo.srcType", String.valueOf(this.srcType));
        return rtnMap;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new FlowLayout());
        jframe.getContentPane().add(new DBInfo());
        jframe.setSize(1024, 768);
        jframe.setVisible(true);
    }
}

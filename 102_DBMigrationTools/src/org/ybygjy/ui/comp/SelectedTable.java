package org.ybygjy.ui.comp;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.ybygjy.ui.UIUtils;

/**
 * ѡ�����Ǩ�Ƶ����ݿ��
 * @author WangYanCheng
 * @version 2012-9-4
 */
public class SelectedTable extends JPanel implements ComponentValue {
    /**
     * serial number
     */
    private static final long serialVersionUID = 5582917091579408448L;
    /** ��Ŀ¼ */
    private JTextField tablesDirField;
    /** �ֹ�¼������ƣ�������Զ��ŷָ� */
    private JTextField inputTableNames;
    /** �ļ�ѡ��� */
    private JFileChooser jfcComp;
    private JTextField columnName;
    private JTextField pkColumn;

    /**
     * Constructor
     */
    public SelectedTable() {
        this.setBorder(new TitledBorder("ѡ�����ݿ��"));
        GridBagLayout gblInst = new GridBagLayout();
        this.setLayout(gblInst);
        GridBagConstraints gbcInst = new GridBagConstraints();
        gbcInst.fill = GridBagConstraints.BOTH;
        gbcInst.insets = new Insets(1, 1, 1, 1);
        gbcInst.gridx = 0;
        gbcInst.gridy = 0;
        UIUtils.makeJLabel(this, "��Ŀ¼��", gblInst, gbcInst);
        gbcInst.gridx = 1;
        tablesDirField = UIUtils.makeJTextField(this, "ѡ������Ŀ¼", gblInst, gbcInst);
        gbcInst.weightx = 0;
        JButton selectFileDirBtn = new JButton("Ŀ¼");
        selectFileDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOpenDialog();
            }
        });
        gbcInst.gridx = 2;
        UIUtils.addGBLComp(this, selectFileDirBtn, gblInst, gbcInst);
        gbcInst.gridx = 0;
        gbcInst.gridy = 1;
        UIUtils.makeJLabel(this, "�ֹ�¼�룺", gblInst, gbcInst);
        gbcInst.gridx = 1;
        this.inputTableNames = UIUtils.makeJTextField(this, "EDC_MESSAGE", gblInst, gbcInst);
        gbcInst.gridx = 0;
        gbcInst.gridy = 2;
        UIUtils.makeJLabel(this, "�����ƣ�", gblInst, gbcInst);
        gbcInst.gridx = 1;
        this.columnName = UIUtils.makeJTextField(this, "CONTENT", gblInst, gbcInst);
        gbcInst.gridx = 2;
        UIUtils.makeJLabel(this, "�����У�", gblInst, gbcInst);
        gbcInst.gridx = 3;
        this.pkColumn = UIUtils.makeJTextField(this, "ID", gblInst, gbcInst);
    }

    /**
     * �����ļ�ѡ��Ի���
     * @return rtnFilePath �ļ�/�ļ�Ŀ¼·��
     */
    private String showOpenDialog() {
        if (null == jfcComp) {
            jfcComp = new javax.swing.JFileChooser(".");
            jfcComp.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        String rtnStr = null;
        int flag = jfcComp.showOpenDialog(this.getParent());
        if (JFileChooser.APPROVE_OPTION == flag) {
            rtnStr = jfcComp.getSelectedFile().getAbsolutePath();
        }
        this.tablesDirField.setText(rtnStr);
        return rtnStr;
    }

    @Override
    public Map<String, String> getValues() {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("tableinfo.tableNames", this.inputTableNames.getText());
        rtnMap.put("tableinfo.columnName", this.columnName.getText());
        rtnMap.put("tableinfo.pkColumn", this.pkColumn.getText());
        rtnMap.put("tableinfo.tableDir", this.tablesDirField.getText());
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
        jframe.getContentPane().add(new SelectedTable());
        jframe.pack();
        jframe.setSize(jframe.getPreferredSize());
        jframe.setVisible(true);
    }
}

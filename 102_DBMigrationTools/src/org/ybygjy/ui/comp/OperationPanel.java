package org.ybygjy.ui.comp;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.ybygjy.example.MigrationSpecialType;
import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.ui.UIUtils;
import org.ybygjy.util.DBUtils;

/**
 * ������壬�����ṩ������ť
 * @author WangYanCheng
 * @version 2012-9-4
 */
public class OperationPanel extends JPanel implements ActionListener {
    /**
     * serial Number
     */
    private static final long serialVersionUID = 4506953241502078333L;
    /**ִ��Btn*/
    private JButton execBtn;
    /**ֹͣBtn*/
    private JButton stopBtn;
    /**ִ������*/
    private static String execCommand = "execBtn";
    /**ֹͣ����*/
    private static String stopCommand = "stopBtn";
    /**���û���*/
    private MainPanel mainPanel;
    /**
     * Constructor
     */
    public OperationPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setBorder(new TitledBorder("������"));
        GridBagLayout gblInst = new GridBagLayout();
        setLayout(gblInst);
        GridBagConstraints gbcInst = new GridBagConstraints();
        gbcInst.gridx = 0;
        gbcInst.gridy = 0;
        gbcInst.insets = new Insets(1, 5, 1, 5);
        UIUtils.makeJLabel(this, "�������ͣ�", gblInst, gbcInst);
        gbcInst.gridx = 1;
        JRadioButton insertType = new JRadioButton("����", true);
        JRadioButton updateType = new JRadioButton("����");
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(updateType);
        radioGroup.add(insertType);
        UIUtils.addGBLComp(this, insertType, gblInst, gbcInst);
        gbcInst.gridx = 2;
        UIUtils.addGBLComp(this, updateType, gblInst, gbcInst);
        gbcInst.gridx = 0;
        gbcInst.gridy = 1;
        execBtn = UIUtils.makeJButton(this, "ִ��", gblInst, gbcInst);
        execBtn.setActionCommand(execCommand);
        execBtn.addActionListener(this);
        gbcInst.gridx = 1;
        stopBtn = UIUtils.makeJButton(this, "ֹͣ", gblInst, gbcInst);
        stopBtn.setActionCommand(stopCommand);
        stopBtn.addActionListener(this);
    }
    /**�ڲ��߳�*/
    private InnerThread innerThread;
    @Override
    public void actionPerformed(ActionEvent e) {
        String commandStr = e.getActionCommand();
        if (execCommand.equals(commandStr) && null != mainPanel) {
            this.execBtn.setEnabled(false);
            innerThread = new InnerThread(
                mainPanel.getComponentValue("dbinfo.srcConnURL"),
                mainPanel.getComponentValue("dbinfo.tarConnURL"),
                mainPanel.getComponentValue("tableinfo.tableNames"),
                mainPanel.getComponentValue("tableinfo.columnName"),
                mainPanel.getComponentValue("tableinfo.pkColumn"));
            innerThread.addCallback(new CallBack() {
                @Override
                public void doCallBack() {
                    SwingUtilities.invokeLater(new Runnable(){
                        public void run() {
                            execBtn.setEnabled(true);
                            releaseThread();
                        }
                    });
                }
            });
            innerThread.start();
        } else if (stopCommand.equals(commandStr)) {
            if (null != innerThread) {
                releaseThread();
                this.execBtn.setEnabled(true);
            }
        }
    }

    /**
     * ���������߳�
     */
    private void releaseThread() {
        if (null != innerThread) {
            if (innerThread.isAlive()) {
                this.innerThread.interrupt();
            }
            this.innerThread = null;
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        JFrame jframeInst = new JFrame();
        jframeInst.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframeInst.setLayout(new FlowLayout());
        jframeInst.getContentPane().add(new OperationPanel(null));
        jframeInst.pack();
        jframeInst.setVisible(true);
    }

    /**
     * �������߳�
     * @author WangYanCheng
     * @version 2012-9-11
     */
    class InnerThread extends Thread {
        private String tarConnURL;
        private String srcConnURL;
        private String tableNames;
        private String columnName;
        private String pkColumn;
        private Logger logger;
        private CallBack callBackInst;
        /**
         * Constructor
         * @param srcConnURL Դ���ݿ��ַ
         * @param tarConnURL Ŀ�����ݿ��ַ
         * @param tableNames ������
         * @param columnName ��
         * @param pkColumn ����
         */
        public InnerThread(String srcConnURL, String tarConnURL, String tableNames, String columnName,
            String pkColumn) {
            this.srcConnURL = srcConnURL;
            this.tarConnURL = tarConnURL;
            this.tableNames = tableNames;
            this.columnName = columnName;
            this.pkColumn = pkColumn;
            this.logger = LoggerFactory.getInstance().getLogger();
        }

        /**
         * ִ������
         */
        public void run() {
            Connection conn4Ora = null;
            Connection conn4SqlServer = null;
            try {
                logger.info("������Oracle�����ӣ�".concat(this.tarConnURL));
                conn4Ora = DBUtils.createConn4Oracle(this.tarConnURL);
                if (null == conn4Ora) {
                    return;
                }
                logger.info("������SqlServer�����ӣ�".concat(this.srcConnURL));
                conn4SqlServer = DBUtils.createConn4MSSql(this.srcConnURL);
                if (null == conn4SqlServer) {
                    return;
                }
                MigrationSpecialType.doWork(conn4Ora, conn4SqlServer, this.tableNames, this.tableNames, columnName, this.pkColumn);
                logger.info("������ɡ�����");
            } catch (Exception e) {
                this.logger.warning(e.getMessage());
            } finally {
                DBUtils.close(conn4SqlServer);
                DBUtils.close(conn4Ora);
                this.callBackInst.doCallBack();
            }
        }
        /**
         * ��ӻص�
         * @param callBack
         */
        public void addCallback(CallBack callBack) {
            this.callBackInst = callBack;
        }
    }
    interface CallBack {
        /**
         * �ص�
         */
        void doCallBack();
    }
}

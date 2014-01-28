package org.ybygjy.ui.comp;

import java.awt.Container;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.Box;
import javax.swing.JFrame;

import org.ybygjy.MessageListener;
import org.ybygjy.logger.LoggerFactory;

/**
 * ������
 * @author WangYanCheng
 * @version 2012-9-4
 */
public class MainPanel extends JFrame implements MessageListener {
    /**
     * serial Number
     */
    private static final long serialVersionUID = -5483775747332699499L;
    /** Logger Instance */
    private Logger logger;
    /** ��־�ļ� */
    private String logFilePath = "./DataMigrationLog%g.log";
    /** LogInfoPanel */
    private LogInfoPanel logInfoPanel;
    /** ���ֵ�����飬�洢֧��ȡֵ��������� */
    private Set<ComponentValue> componentValueArray = new HashSet<ComponentValue>();

    /**
     * Constructor
     */
    public MainPanel() {
        initLogger();
        initPane();
        logger.info("��������Ǩ�ƹ��ߡ����������־�ļ���".concat(logFilePath));
    }

    /**
     * ��ʼ��־������
     */
    private void initLogger() {
        // ע��LogHandler
        LoggerFactory.getInstance().addLoggerHandler(new InnerLogHandler());
        try {
            Handler fileHandler = new FileHandler(logFilePath, false);
            fileHandler.setFormatter(new SimpleFormatter());
            LoggerFactory.getInstance().addLoggerHandler(fileHandler);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger = LoggerFactory.getInstance().getLogger(this.getClass().getName());
    }

    /**
     * ��ʼ
     */
    private void initPane() {
        JFrame jframeInst = new JFrame();
        jframeInst.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Box box = Box.createVerticalBox();
        Container contentPane = jframeInst.getContentPane();
        contentPane.add(box);
        box.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                Object tmpObj = e.getChild();
                if (tmpObj instanceof ComponentValue) {
                    addComponentValue((ComponentValue) tmpObj);
                }
            }
        });
        box.add(new DBInfo());
        box.add(new SelectedTable());
        box.add(new OperationPanel(this));
        this.logInfoPanel = new LogInfoPanel();
        box.add(this.logInfoPanel);
        jframeInst.pack();
        jframeInst.setVisible(true);
    }

    /**
     * InnerLogHandler
     * @author WangYanCheng
     * @version 2012-4-16
     */
    class InnerLogHandler extends Handler {
        @Override
        public void publish(final LogRecord record) {
            logInfoPanel.append(record.getMessage().concat("\n"));
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }

        @Override
        public void setEncoding(String encoding) throws SecurityException, UnsupportedEncodingException {
            super.setEncoding("GB18030");
        }
    }

    @Override
    public void beforeListener() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        logger.info("��ʼִ������Ǩ�ơ�����");
    }

    @Override
    public void afterListener() {
        logger.info("�������Ǩ�ơ�����");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * ���֧��ȡֵ���
     * @param cvInst {@link ComponentValue}
     */
    public void addComponentValue(ComponentValue cvInst) {
        this.componentValueArray.add(cvInst);
    }

    /**
     * ȡ���ֵ
     * @param fieldName �ֶ�����
     * @return rtnValue/null
     */
    public String getComponentValue(String fieldName) {
        String rtnStr = null;
        for (Iterator<ComponentValue> iterator = this.componentValueArray.iterator(); iterator.hasNext();) {
            rtnStr = iterator.next().getValues().get(fieldName);
            if (null != rtnStr) {
                break;
            }
        }
        return rtnStr;
    }

    /**
     * ȡ��ǰʵ��Logger����
     * @return rtnLogger {@link Logger}
     */
    protected Logger getLogger() {
        return this.logger;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        new MainPanel();
    }
}

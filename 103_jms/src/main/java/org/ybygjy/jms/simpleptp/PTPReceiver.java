package org.ybygjy.jms.simpleptp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * ���������Ϣ
 * @author WangYanCheng
 * @version 2012-3-1
 */
public class PTPReceiver extends Thread {
    /** ����ʵ�� */
    private QueueConnection queueConn;
    /** destination */
    private String queueDesc;
    /** transacted flag */
    private boolean transacted;
    /** acknowledge model */
    private int acknowledge;
    /** session */
    private QueueSession queueSession;
    /** queue */
    private Queue queue;
    /** queueReceiver */
    private QueueReceiver receiver;
    /** �߳�������� */
    private volatile boolean isDown;

    /**
     * ���캯��
     * @param queueConn ����ʵ��
     * @param queueDesc ��ϢĿ�ĵ�����
     * @param transacted �Ƿ�����
     * @param acknowledge ��Ϣȷ��ģʽ
     */
    public PTPReceiver(QueueConnection queueConn, String queueDesc, boolean transacted, int acknowledge) {
        this.isDown = true;
        this.queueConn = queueConn;
        this.queueDesc = queueDesc;
        this.transacted = transacted;
        this.acknowledge = acknowledge;
        initContext();
    }

    /**
     * �������ݳ�ʼ��
     */
    private void initContext() {
        try {
            queueConn.start();
            queueSession = this.queueConn.createQueueSession(this.transacted, this.acknowledge);
            queue = queueSession.createQueue(this.queueDesc);
            receiver = queueSession.createReceiver(queue);
        } catch (JMSException e) {
            e.printStackTrace();
            isDown = false;
        }
    }

    /**
     * �߳����
     */
    public void run() {
        int innerCount = 0;
        while (isDown) {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                innerCount ++;
                Message srcMessage = receiver.receive();
                if (srcMessage instanceof TextMessage) {
                    TextMessage message = (TextMessage) srcMessage;
                    System.out.println("���յ���Ϣ".concat(String.valueOf(innerCount)).concat("\n").concat(message.toString()));
                    if (null == message.getText()) {
                        System.out.println("���յ�������Ϣ��׼�������߳�.");
                        this.isDown = false;
                    }
                } else {
                    System.out.println("δ֪���͵�Message\n".concat(srcMessage.toString()));
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        try {
            this.queueConn.close();
            System.out.println("�ر�����=>>" + this.queueConn.toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * �������
     * @param args �����б�
     * @throws JMSException JMSException
     */
    public static void main(String[] args) throws JMSException {
        QueueConnectionFactory qcfInst = new ActiveMQConnectionFactory(JMSConstant.JMS_URL);
        QueueConnection queueConn = qcfInst.createQueueConnection();
        PTPReceiver ptpInst = new PTPReceiver(queueConn, JMSConstant.JMS_QUEUE, false, Session.AUTO_ACKNOWLEDGE);
        ptpInst.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

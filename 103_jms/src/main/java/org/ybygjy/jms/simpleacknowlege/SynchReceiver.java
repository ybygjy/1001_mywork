package org.ybygjy.jms.simpleacknowlege;

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
 * ������Ϣ��ʹ��CLIENT_ACKNOWLEDGEģʽ�Ự
 * @author WangYanCheng
 * @version 2012-3-8
 */
public class SynchReceiver extends Thread {
    /** Connection */
    private QueueConnection queueConn;
    /** Session */
    private QueueSession queueSession;
    /** Destination */
    private Queue queue;
    /** MessageConsumer */
    private QueueReceiver queueReceiver;
    /** Queue Name */
    private String queueName;
    /** �̱߳�� */
    private volatile boolean isDone;

    /**
     * Constructor
     * @param queueConn ����ʵ��
     * @param queueName ��������
     */
    public SynchReceiver(QueueConnection queueConn, String queueName) {
        this.queueConn = queueConn;
        this.queueName = queueName;
        this.initContext();
    }

    /**
     * ��ʼ��
     */
    public void initContext() {
        try {
            this.queueSession = this.queueConn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
            this.queue = this.queueSession.createQueue(this.queueName);
            this.queueReceiver = this.queueSession.createReceiver(queue);
            this.queueConn.start();
        } catch (JMSException e) {
            this.isDone = true;
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int count = 0;
        while (!isDone) {
            try {
                count++;
                Message message = this.queueReceiver.receive();
                if (message instanceof TextMessage) {
                    System.out.println("�յ���".concat(String.valueOf(count)).concat("����Ϣ\n\t".concat(message.toString())));
                } else {
                    this.isDone = true;
                    System.out.println("�յ�������Ϣ��׼���˳�\n\t".concat(message.toString()));
                }
                message.acknowledge();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isDone && null != this.queueConn) {
            try {
                this.queueConn.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    }
    /**
     * �������
     * @param args �����б�
     * @throws JMSException JMSException
     */
    public static void main(String[] args) throws JMSException {
        QueueConnectionFactory queueConnFact = new ActiveMQConnectionFactory();
        QueueConnection queueConn = queueConnFact.createQueueConnection();
        SynchReceiver synchReceiver = new SynchReceiver(queueConn, JMSConstant.JMS_QUEUE);
        synchReceiver.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

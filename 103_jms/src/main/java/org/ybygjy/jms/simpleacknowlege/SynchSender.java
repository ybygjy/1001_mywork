package org.ybygjy.jms.simpleacknowlege;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * ���𴴽�һ��CLIENT_ACKNOWLEDGEģʽ��session
 * <p>CLIENT_ACKNOWLEDGEģʽ����
 * @author WangYanCheng
 * @version 2012-3-8
 */
public class SynchSender extends Thread {
    /**����ʵ��*/
    private QueueConnection queueConn;
    /**�Ựʵ��*/
    private QueueSession queueSession;
    /**Destination*/
    private Queue queue;
    /**Message Producer*/
    private QueueSender queueSender;
    /**Queue Name*/
    private String queueName;
    /**�߳����б��*/
    private volatile boolean isDone;
    /**
     * Constructor
     * @param queueConn {@link QueueConnection}
     */
    public SynchSender(QueueConnection queueConn, String queueName) {
        this.queueConn = queueConn;
        this.queueName = queueName;
        initContext();
    }
    /**
     * �����ʼ������ʵ��
     */
    private void initContext() {
        try {
            this.queueSession = this.queueConn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
            this.queue = this.queueSession.createQueue(queueName);
            this.queueSender = this.queueSession.createSender(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        int count = 0;
        while (!isDone) {
            //ÿ��1�뷢��һ����Ϣ
            try {
                count ++;
                this.queueSender.send(createMessage());
                System.out.println("���͵�".concat(String.valueOf(count)).concat("����Ϣ��"));
                if (count >= 15) {
                    this.isDone = true;
                    this.queueSender.send(this.queueSession.createMessage());
                    System.out.println("�˳����񡣡���");
                }
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isDone) {
            if (null != this.queueConn) {
                try {
                    this.queueConn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.exit(0);
                }
            }
        }
    }
    /**
     * 
     * @return
     * @throws JMSException
     */
    private Message createMessage() throws JMSException {
        Message rtnMessage = this.queueSession.createTextMessage("�߳����ƣ�".concat(getName()).concat("\t����ţ�").concat(String.valueOf(Math.random())));
        return rtnMessage;
    }
    /**
     * �������
     * @param args �����б�
     * @throws JMSException JMSException
     */
    public static void main(String[] args) throws JMSException {
        QueueConnectionFactory queueConnFact = new ActiveMQConnectionFactory();
        QueueConnection queueConn = queueConnFact.createQueueConnection();
        SynchSender synchSender = new SynchSender(queueConn, JMSConstant.JMS_QUEUE);
        synchSender.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package org.ybygjy.jms.chapter03;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * ������Ϣ������(ʹ��)
 * @author WangYanCheng
 * @version 2012-2-29
 */
public class PTPConsumer extends Thread {
    /** ���ӹ��� */
    private QueueConnectionFactory qcfInst;

    /**
     * ���캯��
     */
    public PTPConsumer() {
        qcfInst = new ActiveMQConnectionFactory(JMSConstant.JMS_URL);
    }

    public void run() {
        while (true) {
            QueueConnection queueConnection = null;
            try {
                queueConnection = qcfInst.createQueueConnection();
                QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination queueInst = queueSession.createQueue(JMSConstant.JMS_QUEUE);
                MessageConsumer messageConsumer = queueSession.createConsumer(queueInst);
                queueConnection.start();
                Message message = messageConsumer.receive();
                System.out.println("���յ�һ����Ϣ��" + message);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != queueConnection) {
                    try {
                        queueConnection.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new PTPConsumer().start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

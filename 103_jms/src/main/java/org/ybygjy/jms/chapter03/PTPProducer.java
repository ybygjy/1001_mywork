package org.ybygjy.jms.chapter03;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * It's a producer that used Point-to-Point Pattern.
 * @author WangYanCheng
 * @version 2012-2-28
 */
public class PTPProducer extends Thread {
    /**���ӹ���_Administered Object*/
    private QueueConnectionFactory queueConnFactory;
    /**
     * ���캯��<br>
     * ��ʼ�����ӹ���
     */
    public PTPProducer() {
        queueConnFactory = new ActiveMQConnectionFactory(JMSConstant.JMS_URL);
    }
    /**
     * �߼��������
     */
    public void run() {
        while (true) {
            //��������
            QueueConnection queueConnection = null;
            try {
                queueConnection = queueConnFactory.createQueueConnection();
                //�����Ự
                QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                //����Destination
                Destination queueInst = queueSession.createQueue(JMSConstant.JMS_QUEUE);
                //����Producer
                MessageProducer queueSender = queueSession.createSender((Queue) queueInst);
                queueConnection.start();
                //����Message
                TextMessage textMessage = queueSession.createTextMessage("������Ϣ�ṩ�ߡ�����");
                queueSender.send(textMessage);
                System.out.println("������һ����Ϣ��" + textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            } finally {
                if (null != queueConnection) {
                    try {
                        queueConnection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("ִ�����.");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        new PTPProducer().start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

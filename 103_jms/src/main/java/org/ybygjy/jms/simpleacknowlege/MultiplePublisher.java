package org.ybygjy.jms.simpleacknowlege;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * ������Ϣ����
 * @author WangYanCheng
 * @version 2012-3-16
 */
public class MultiplePublisher extends Thread {
    /** ����ʵ�� */
    private TopicConnection conn;
    /** �Ựʵ�� */
    private TopicSession session;
    /** ����ʵ�� */
    private Topic topic;
    /** ��Ϣ����ʵ�� */
    private javax.jms.TopicPublisher topicPublisher;
    /** �������� */
    private String topicName;
    /** �̱߳�� */
    private volatile boolean isDone;
    /** ������Ϣ���� */
    private static int JMS_MESS_MAX = 5;

    /**
     * ���췽��
     * @param conn ����ʵ��
     * @param topicName ��������
     */
    public MultiplePublisher(TopicConnection conn, String topicName) {
        this.conn = conn;
        this.topicName = topicName;
        isDone = false;
        initContext();
    }

    /**
     * ��ʼ��
     */
    private void initContext() {
        try {
            this.session = this.conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            this.topic = this.session.createTopic(topicName);
            this.topicPublisher = this.session.createPublisher(topic);
            this.conn.start();
        } catch (JMSException e) {
            e.printStackTrace();
            isDone = true;
            System.err.println("��ʼ������������������");
        } finally {
        }
    }

    @Override
    public void run() {
        int msgCount = 0;
        while (!isDone) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Message messInst = this.createMessage(session);
                this.topicPublisher.publish(messInst);
                System.out.println("������[".concat(String.valueOf(msgCount ++)).concat("]����Ϣ������"));
                if (msgCount >= JMS_MESS_MAX) {
                    this.topicPublisher.publish(session.createMessage());
                    System.out.println("��������������Ϣ��׼���˳�������");
                    this.isDone = true;
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if (isDone) {
            try {
                this.conn.close();
                System.out.println("�ر�ʵ��\n\n".concat(this.conn.toString()));
            } catch (JMSException e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    }

    /**
     * ������Ϣ
     * @param session �Ựʵ��
     * @return rtnMsg ��Ϣʵ��
     * @throws JMSException �׳��쳣��Log
     */
    private Message createMessage(Session session) throws JMSException {
        Message rtnMsg = session.createTextMessage(String.valueOf(Math.random()));
        return rtnMsg;
    }
    /**
     * �������
     * @param args �����б�
     * @throws JMSException JMSException
     */
    public static void main(String[] args) throws JMSException {
        TopicConnectionFactory topicConnFactory = new ActiveMQConnectionFactory();
        MultiplePublisher multipleInst = new MultiplePublisher(topicConnFactory.createTopicConnection(), JMSConstant.JMS_TOPIC);
        multipleInst.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

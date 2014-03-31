package org.ybygjy.jms.simpleps;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * ������Ϣ����
 * @author WangYanCheng
 * @version 2012-3-1
 */
public class TopicPublisher extends Thread {
    /** ���� */
    private TopicConnection topicConn;
    /** transacted flag */
    private boolean transacted;
    /** acknowledge mode */
    private int acknowledgeMode;
    /** topic session */
    private TopicSession topicSession;
    /** ���� */
    private String topicName;
    /** ���ݷ���ʵ�� */
    private javax.jms.TopicPublisher topicPublisher;
    /** �߳����б�� */
    private volatile boolean isDone;
    /** ������Ϣ��ֵ */
    public static final int MAX_PUBLISH_MESS = 10;

    /**
     * ���캯��
     * @param topicConn ����ʵ��
     * @param topicName ��������
     * @param transacted ����ģʽ
     * @param acknowledgeMode ��Ϣȷ��ģʽ
     */
    public TopicPublisher(TopicConnection topicConn, String topicName, boolean transacted,
        int acknowledgeMode) {
        this.isDone = false;
        this.topicConn = topicConn;
        this.topicName = topicName;
        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        this.initContext();
    }

    /**
     * ��ʼ��
     */
    public void initContext() {
        try {
            topicSession = topicConn.createTopicSession(transacted, acknowledgeMode);
            Topic topic = topicSession.createTopic(topicName);
            topicPublisher = topicSession.createPublisher(topic);
            topicConn.start();
        } catch (JMSException e) {
            System.err.println("��ʼ��JMS����".concat(e.getMessage()));
            isDone = true;
        }
    }

    public void run() {
        int messCount = 0;
        while (!isDone) {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                messCount++;
                this.topicPublisher.send(createMessage(this.topicSession));
                System.out.println("����".concat(String.valueOf(messCount)).concat("����Ϣ"));
                if (MAX_PUBLISH_MESS == messCount) {
                    isDone = true;
                    /* Message Control */
                    this.topicPublisher.send(this.topicSession.createTextMessage());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        try {
            this.topicConn.close();
            System.out.println("�ر�����==>".concat(this.topicConn.toString()));
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * ������Ϣ
     * @param topicSession �Ự
     * @return rtnMessage ��Ϣ
     * @throws JMSException JMSException
     */
    private Message createMessage(TopicSession topicSession) throws JMSException {
        TextMessage rtnMessage = topicSession.createTextMessage();
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(getName()).append("\n\t").append(String.valueOf(Math.random()));
        rtnMessage.setText(sbuf.toString());
        return rtnMessage;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        TopicConnectionFactory tcfInst = new ActiveMQConnectionFactory(JMSConstant.JMS_URL);
        TopicConnection topicConn = null;
        try {
            topicConn = tcfInst.createTopicConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        new TopicPublisher(topicConn, JMSConstant.JMS_TOPIC, false, Session.AUTO_ACKNOWLEDGE).start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("�����˳�������");
    }
}

package org.ybygjy.jms.simpleacknowlege;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * �첽����Ϣ����
 * @author WangYanCheng
 * @version 2012-3-8
 */
public class AsynchSubscriber extends Thread {
    /** TopicConnection */
    private TopicConnection topicConn;
    /** TopicSession */
    private TopicSession topicSession;
    /** Topic */
    private Topic topic;
    /** TopicSubscriber */
    private TopicSubscriber topicSubscriber;
    /** Topic Name */
    private String topicName;
    /** isDone */
    private volatile boolean isDone;
    /** ʵ����� */
    private String subIdentify;

    /**
     * Constructor
     * @param topicConn ����ʵ��
     * @param topicName ��������
     */
    public AsynchSubscriber(TopicConnection topicConn, String topicName) {
        this.topicConn = topicConn;
        this.topicName = topicName;
        this.subIdentify = "DurableSubscriber_".concat(getName()).concat(String.valueOf(Math.random()));
        initContext();
    }

    /**
     * ��ʼ��
     */
    private void initContext() {
        try {
            this.topicConn.setClientID(this.subIdentify);
            this.topicSession = this.topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            this.topic = this.topicSession.createTopic(this.topicName);
            this.topicSubscriber = this.topicSession.createDurableSubscriber(topic, this.subIdentify);
            this.topicSubscriber.setMessageListener(new MessageListener() {
                /** ���� */
                private int ctxCount = 0;

                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        System.out.println("�յ�".concat(String.valueOf(ctxCount++)).concat("����Ϣ\n\t")
                            .concat(((TextMessage) message).toString()));
                    } else {
                        isDone = true;
                        System.out.println("�յ������Ϣ��׼���˳�!\n\t".concat(message.toString()));
                    }
                }
            });
            this.topicConn.start();
        } catch (JMSException e) {
            this.isDone = true;
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!this.isDone) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.isDone && null != this.topicConn) {
            try {
                this.topicSubscriber.close();
                this.topicSession.unsubscribe(this.subIdentify);
                this.topicConn.close();
                System.out.println("�ر����ӣ�\n\t".concat(this.topicConn.toString()));
            } catch (JMSException e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        TopicConnectionFactory topicConnFacory = new ActiveMQConnectionFactory();
        try {
            AsynchSubscriber asynchSubscriberInst = new AsynchSubscriber(topicConnFacory.createTopicConnection(), JMSConstant.JMS_TOPIC);
            asynchSubscriberInst.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

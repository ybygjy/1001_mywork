package org.ybygjy.jms.simpleps;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * ��������Ϣ
 * @author WangYanCheng
 * @version 2012-3-1
 */
public class TopicSubscriber extends Thread {
    /** ���� */
    private TopicConnection topicConn;
    /** �Ự */
    private TopicSession session;
    /** ������ */
    private javax.jms.TopicSubscriber topicSubscriber;
    /** ��Ϣ���� */
    private Topic topic;
    /** �����ʶ */
    private boolean transacted;
    /** ��Ϣ����ȷ��ģʽ */
    private int acknowledgeMode;
    /** ��Ϣ���� */
    private String topicName;
    /** �߳�ִ�б�� */
    private volatile boolean isDone;

    /**
     * ���캯��
     * @param topicConn ����
     * @param topicName ��Ϣ����
     * @param transacted �����ʶ
     * @param acknowledgeMode ��Ϣ����ȷ��ģʽ
     */
    public TopicSubscriber(TopicConnection topicConn, String topicName, boolean transacted,
        int acknowledgeMode) {
        this.isDone = false;
        this.topicConn = topicConn;
        this.topicName = topicName;
        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        initContext();
    }

    /**
     * ��ʼ��
     */
    private void initContext() {
        try {
            session = this.topicConn.createTopicSession(transacted, acknowledgeMode);
            topic = session.createTopic(topicName);
            topicSubscriber = session.createSubscriber(topic);
            this.topicSubscriber.setMessageListener(new MessageListener() {
                /** Message Count */
                private int messageCount = 0;

                @Override
                public void onMessage(Message message) {
                    messageCount++;
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("���յ���".concat(String.valueOf(messageCount)).concat("����Ϣ==>")
                            .concat(textMessage.toString()));
                        try {
                            if (null == textMessage.getText()) {
                                System.out.println("�յ��ر���Ϣ��׼���˳�������");
                                TopicSubscriber.this.isDone = true;
                            }
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("�������͵���Ϣ==>".concat(message.toString()));
                    }
                }
            });
            this.topicConn.start();
        } catch (JMSException e) {
            this.isDone = true;
            e.printStackTrace();
        }
    }

    /**
     * �߳�ִ�����
     */
    @Override
	public void run() {
        while (!isDone) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            this.topicConn.close();
            System.out.println("�ر�����==>".concat(this.topicConn.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
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
        new TopicSubscriber(topicConn, JMSConstant.JMS_TOPIC, false, Session.AUTO_ACKNOWLEDGE).start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("�����˳�������");
    }
}

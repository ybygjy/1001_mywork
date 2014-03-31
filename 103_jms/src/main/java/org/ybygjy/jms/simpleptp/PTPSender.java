package org.ybygjy.jms.simpleptp;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.ybygjy.jms.JMSConstant;

/**
 * ������Ϣ���ͣ���Ҫ������״̬���̣�<br>
 * 1��ȡ���ӹ���<br>
 * 2��ʹ�����ӹ�����������<br>
 * 3��ʹ�����Ӵ�������<br>
 * 4��ʹ�ûỰ����Producer��Destination��Message<br>
 * �߼���������<br>
 * 1�����������߳�,ÿ��һ��ʱ�䴫��һ����Ϣ<br>
 * 2��������ĳ������ʱ����һ��Control Message��ʾ�˴λỰ����
 * @author WangYanCheng
 * @version 2012-3-1
 */
public class PTPSender extends Thread {
    /** ���� */
    private QueueConnection queueConn;
    /** transacted Flag */
    private boolean transacted;
    /** acknowledge Model */
    private int acknowledge;
    /** queue Description */
    private String queueDesc;
    /** Session */
    private QueueSession queueSession;
    /** Destination */
    private Queue queue;
    /** message producer */
    private QueueSender queueSender;
    /** ����20����Ϣ֮���̹߳ر� */
    public static final int MAX_SEND_MESSAGE = 20;
    /**�߳��������*/
    private volatile boolean isDown;
    /**
     * ���캯������ʼ������
     * @param queueConn ����ʵ��
     * @param queueDesc Ŀ�ĵر��
     * @param transacted ������
     * @param acknowledge ��Ϣȷ��ģʽ
     */
    public PTPSender(QueueConnection queueConn, String queueDesc, boolean transacted, int acknowledge) {
        this.isDown = true;
        this.queueConn = queueConn;
        this.queueDesc = queueDesc;
        this.transacted = transacted;
        this.acknowledge = acknowledge;
        initContext();
    }

    /**
     * ��ʼ��
     */
    private void initContext() {
        try {
            queueConn.start();
            queueSession = queueConn.createQueueSession(transacted, acknowledge);
            queue = queueSession.createQueue(this.queueDesc);
            queueSender = queueSession.createSender(queue);
        } catch (JMSException e) {
            System.err.println("���������������ʧ��==>".concat(this.queueConn.toString()));
            isDown = false;
        }
    }

    /**
     * �߳����
     */
    public void run() {
        int innerMesCount = 0;
        while (isDown) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                innerMesCount++;
                queueSender.send(createTextMessage(queueSession));
                System.out.println("������".concat(String.valueOf(innerMesCount)).concat("����Ϣ������"));
                if (innerMesCount == MAX_SEND_MESSAGE) {
                    /* ��ν��ControlMessageָ�ľ�������ͨMessage��Щ�����Message */
                    queueSender.send(queueSession.createTextMessage());
                    isDown = false;
                }
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
        /* ��queueConn�Ĺ�������ȱ��,Ŀǰֻ���ǲ��Ի��� */
        try {
            queueConn.close();
            System.out.println("�ر�Connection>>".concat(this.queueConn.toString()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * ������Ϣʵ��
     * @param session �Ựʵ��
     * @return rtnMessage ��Ϣʵ��
     * @throws JMSException �쳣��Ϣ
     */
    private TextMessage createTextMessage(Session session) throws JMSException {
        TextMessage rtnMessage = session.createTextMessage();
        StringBuffer sbud = new StringBuffer();
        sbud.append(this.getName()).append("\n\t").append(String.valueOf(Math.random()));
        rtnMessage.setText(sbud.toString());
        return rtnMessage;
    }

    /**
     * �������
     * @param args �����б�
     * @throws JMSException JMSException
     */
    public static void main(String[] args) throws JMSException {
        QueueConnectionFactory qcfInst = new ActiveMQConnectionFactory(JMSConstant.JMS_URL);
        QueueConnection queueConn = qcfInst.createQueueConnection();
        PTPSender ptpInst = new PTPSender(queueConn, JMSConstant.JMS_QUEUE, false, Session.AUTO_ACKNOWLEDGE);
        ptpInst.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package org.ybygjy.jcip.chap4;

import java.util.ArrayList;
import java.util.List;

/**
 * ʵ�����
 * <p>�����ݷ�װ�ڶ����ڲ������Խ����ݵķ��������ڶ���ķ����ϣ��Ӷ�������ȷ���߳��ڷ�������ʱ���ܳ�����ȷ������</p>
 * <p>ͨ������ջ�������ʵļ������Խ�ϣ�����ȷ�����̰߳�ȫ�ķ�ʽ��ʹ�÷��̰߳�ȫ�Ķ���</p>
 * <p>Decoratorģʽ</p>
 * @author WangYanCheng
 * @version 2014-7-21
 */
public class InstanceConfinement {
    /**
     * �������
     * @param args
     */
    public static void main(String[] args) {
        InnerInstanceConfinement instanceConf = new InnerInstanceConfinement();
        new WriteInnerThread("WriteThread", instanceConf).start();
        new ReadInnerThread("ReadThread", instanceConf).start();
        new MonitorThread("MonitorThread", instanceConf).start();
    }
    /**
     * д�߳�
     * @author WangYanCheng
     * @version 2014-7-21
     */
    static class WriteInnerThread extends Thread {
        private InnerInstanceConfinement instanceConfinement;
        public WriteInnerThread(String threadName, InnerInstanceConfinement instanceConfinement) {
            super(threadName);
            this.instanceConfinement = instanceConfinement;
        }
        public void run() {
            while (true) {
                Point point = new Point((int)(Math.random() * 100), (int)(Math.random() * 100));
                this.instanceConfinement.addPoint(point);
                System.out.println(getName() + ">>" + point);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ���߳�
     * @author WangYanCheng
     * @version 2014-7-21
     */
    static class ReadInnerThread extends Thread {
        private InnerInstanceConfinement instanceConfinement;
        public ReadInnerThread(String threadName, InnerInstanceConfinement instanceConfinement) {
            super(threadName);
            this.instanceConfinement = instanceConfinement;
        }
        public void run() {
            while (true) {
                Point[] points = this.instanceConfinement.getAllPoints();
                for (Point point : points) {
                    System.out.println(getName() + ">>" + point);
                    this.instanceConfinement.removePoint(point);
                    //��֤List#toArray]���˴�ʵ�����޸Ĳ���Ӱ�쵽����յķ��̰߳�ȫ�����еĶ���
                    //��Ϊ{@link Point}�ǲ��ɱ���������޷��޸Ķ�������ԣ������Ըı���������
//                    point.x = 0;
//                    point.y = 0;
//                    ����
//                    String s1 = "abc";
//                    String s2 = "abc";
//                    s2==s1?s2="abcd":s1;
                    point = new Point(0, 0);
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ������ʵ��״̬���
     * @author WangYanCheng
     * @version 2014-7-21
     */
    static class MonitorThread extends Thread {
        private InnerInstanceConfinement instanceConfinement;
        public MonitorThread(String threadName, InnerInstanceConfinement instanceConfinement) {
            super(threadName);
            this.instanceConfinement = instanceConfinement;
        }
        public void run() {
            while (true) {
                System.out.println(getName() + ">>" + this.instanceConfinement.getAllPoints().length);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
/**
 * ����ͨ����ջ���ȷ���̰߳�ȫ
 * <p>1����շ��̰߳�ȫ�ļ��϶���</p>
 * <p>2���ɼ�ͬ������ȷ���Է��̰߳�ȫ���϶���Ĳ������̰߳�ȫ��</p>
 * @author WangYanCheng
 * @version 2014-7-21
 */
class InnerInstanceConfinement {
    private final List<Point> pointList = new ArrayList<Point>();
    public synchronized void addPoint(Point point) {
        this.pointList.add(point);
    }
    public synchronized boolean containsPoint(Point point) {
        return pointList.contains(point);
    }
    public synchronized boolean removePoint(Point point) {
        //����
        if (containsPoint(point)) {
            return pointList.remove(point);
        } else {
            System.out.println("Not contains=>" + point);
        }
        return false;
    }
    /**
     * deepCopy����
     * @return rtnPoint {@link Point}
     */
    public Point[] getAllPoints() {
        return pointList.toArray(new Point[pointList.size()]);
    }
}

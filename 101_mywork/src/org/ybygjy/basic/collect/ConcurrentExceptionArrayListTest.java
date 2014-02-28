package org.ybygjy.basic.collect;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ConcurrentModificationException�쳣�����쳣ͨ����һ������Ϊ����߳�ʹ�õĳ����С�
 * <p><strong>����취��</strong></p>
 * <p>
 * 1��ʹ��copy�취����<pre>
 * String[] tmpStrArr = new String[arrayListInst.size()];
 * for (String tmp : tmpStrArr) {
 *  System.out.println(tmp);
 * }
 * </pre>
 * 2��ʹ���ض��ļ���ʵ������Ҫ�������ܴ���
 * </p>
 * @author WangYanCheng
 * @version 2010-11-10
 */
public class ConcurrentExceptionArrayListTest {
    /** ���� */
    private List<String> rtnList = /*new ArrayList<String>();*/new CopyOnWriteArrayList<String>();/*Collections.synchronizedList(new ArrayList<String>());*/
    /**
     * ����������
     * @author WangYanCheng
     * @version 2011-6-16
     */
    class ProducterThread extends Thread {
        private boolean startFlag = true;
        private Random randInst;

        /**
         * Constructor
         */
        public ProducterThread() {
            randInst = new Random();
        }
        /**
         * �߳�ֹͣ
         * @param startFlag
         */
        public void stopThread(boolean startFlag) {
            this.startFlag = startFlag;
        }
        /**
         * runnable
         */
        public void run() {
            while (this.startFlag) {
                rtnList.add(String.valueOf(randInst.nextDouble()));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ����������
     * @author WangYanCheng
     * @version 2011-6-16
     */
    class ConsumerThread extends Thread {
        private boolean startFlag = true;
        /**
         * �߳�ֹͣ
         * @param startFlag
         */
        public void stopThread(boolean startFlag) {
            this.startFlag = startFlag;
        }
        public void run() {
            while (this.startFlag) {
                doPrintList(rtnList.toArray(new String[rtnList.size()]));
                doPrintList(rtnList.iterator());
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void doPrintList(Iterator<String> iterator) {
        for (;iterator.hasNext();) {
            System.out.print(iterator.next() + "\t");
        }
        System.out.println();
    }
    public void doPrintList(String[] listArr) {
        for (String str : listArr) {
            System.out.print(str.concat("\t"));
        }
        System.out.println();
    }
    /**
     * �����������̶߳���
     * @return {@link ConsumerThread}
     */
    public ConsumerThread getConsumerThread() {
        return new ConsumerThread();
    }
    /**
     * �����������̶߳���
     * @return {@link ProducterThread}
     */
    public ProducterThread getProducterThread() {
        return new ProducterThread();
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        ConcurrentExceptionArrayListTest cowALT = new ConcurrentExceptionArrayListTest();
        cowALT.getProducterThread().start();
        cowALT.getConsumerThread().start();
        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

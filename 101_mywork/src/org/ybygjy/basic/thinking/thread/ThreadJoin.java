package org.ybygjy.basic.thinking.thread;
/**
 * {@link Thread#join()}ѧϰ
 * <p>
 * {@link Thread#join()}ʹ�ó����������߳����������̣߳����̸߳�����д��ģ���㡣��ʱ���߳���Ҫ
 * ���̼߳��������ɴ˵ó����߳���Ҫ�ȴ����߳�������ϵó������������߳���һ������������塣
 * </p>
 * @author WangYanCheng
 * @version 2011-6-1
 */
public class ThreadJoin {
    public void doTest() {
        Thread tjOne = new ThreadJoinOne();
        Thread tjTwo = new ThreadJoinTwo();
        tjOne.start();
        tjTwo.start();
        try {
            tjOne.join();
            tjTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
    }
    class ThreadJoinOne extends Thread {
        private int i = 0;
        public void run() {
            String threadName = (Thread.currentThread().getName());
            while (i < 5) {
                i++;
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(threadName + "  over������");
        }
    }
    class ThreadJoinTwo extends Thread {
        private int i = 0;
        public void run() {
            String threadName = (Thread.currentThread().getName());
            while (i < 5) {
                i++;
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(threadName + "  over������");
        }
    }
    public static void main(String[] args) {
        ThreadJoin threadJoin = new ThreadJoin();
        threadJoin.doTest();
    }
}
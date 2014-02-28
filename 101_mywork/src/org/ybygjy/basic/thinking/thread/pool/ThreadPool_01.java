package org.ybygjy.basic.thinking.thread.pool;

import java.util.LinkedList;

/**
 * �̳߳�ʵ��
 * @author WangYanCheng
 * @version 2012-11-31
 */
public class ThreadPool_01 {
    // ��һ���������
    private final LinkedList<Runnable> queue;
    // ��һ�������߳�����
    private final int workerThreadNums;
    // ��һ�������̼߳���
    private final ThreadWorker[] threadWorkers;
    // ʵ�ʹ����߳�����
    private int thNums;

    /**
     * Constructor
     * @param workerThreadNums ��ʼ�����߳�����
     */
    public ThreadPool_01(int workerThreadNums) {
        this.queue = new LinkedList<Runnable>();
        this.workerThreadNums = workerThreadNums;
        this.threadWorkers = new ThreadWorker[this.workerThreadNums];
        for (ThreadWorker tw : this.threadWorkers) {
            tw = new ThreadWorker(ThreadWorker.THNAMEPREFIX.concat(String.valueOf(thNums++)));
            tw.start();
        }
    }

    /**
     * �ṩ���ͻ��˵�API����������߼����񣬲�����ִ��
     * @param r ��ִ������
     */
    public void execute(Runnable r) {
        synchronized (queue) {
            queue.add(r);
            // �����ڹ�����Դ��ģʽ��ֻ֪ͨһ���ȴ��̼߳���
            queue.notify();
        }
    }

    /**
     * �̳߳��еĹ����̣߳����������ڱ��ض�����ȫ���ƣ�����ִ������
     * @author WangYanCheng
     * @version 2012-11-31
     */
    private class ThreadWorker extends Thread {
        private static final String THNAMEPREFIX = "TW_";

        /**
         * Constructor
         * @param thName
         */
        public ThreadWorker(String thName) {
            super(thName);
        }
        @Override
        public void run() {
            Runnable run;
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    run = queue.removeFirst();
                }
                // �����߼������쳣һ����ҪӰ�쵽�����߳�
                try {
                    run.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * �䵱�ͻ��ˣ��������ִ�е�����
     * @author WangYanCheng
     * @version 2012-11-31
     */
    private class ThreadTester extends Thread {
        public ThreadTester() {
            super();
            setName("ThreadTester");
            start();
        }

        @Override
        public void run() {
            int taskCount = 1;
            while (true) {
                execute(new VirtualTask(VirtualTask.TASKPREFIX.concat(String.valueOf(taskCount++))));
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ����ʵ��
     * @author WangYanCheng
     * @version 2012-11-31
     */
    private class VirtualTask implements Runnable {
        private static final String TASKPREFIX = "CT_";
        /** ����Id */
        private String taskId;

        /**
         * Constructor
         * @param taskId ����Id
         */
        public VirtualTask(String taskId) {
            this.taskId = taskId;
        }

        /**
         * �����߼����
         */
        public void run() {
            System.out.println(this);
            try {
                Thread.sleep((long)(Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "VirtualTask [taskId=" + taskId + "]";
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        ThreadPool_01 tp01 = new ThreadPool_01(2);
        tp01.new ThreadTester();
    }
}

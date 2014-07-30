package org.ybygjy.jcip.chap5;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * ConcurrentHashMap#�ֶ���ѧϰʵ��
 * <p>1���ֶ���Ӧ��ʵ��</p>
 * <p>2���ֶ�����ʵ�ַ���</p>
 * @author WangYanCheng
 * @version 2014��7��30��
 */
public class ConcurrentMapLockStriping {
    /**
     * �������
     */
    public void doTest() {
        //���ö��߳���֤ConcurrentHashMap��HashMap������
        //10���̸߳������������
        doTestConcurrentHashMap(10);
        doTestHashMap(10);
    }
    /**
     * ִ�����
     * @param args �����б�
     */
    public static void main(String[] args) {
        ConcurrentMapLockStriping cmlsInst = new ConcurrentMapLockStriping();
        cmlsInst.doTest();
    }
    private void doTestConcurrentHashMap(int nThreads) {
        /** ����_��ʼ��*/
        final CountDownLatch startGate = new CountDownLatch(1);
        /** ����_������*/
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        final ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<String, Object>();
        for (int i = 1; i <= nThreads; i++) {
            new Thread("ConcurrentTestThread_" + i){
                public void run() {
                    try {
                        startGate.await();
                        try {
                            new ConcurrentHashMapTest(getName(), concurrentHashMap).run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        try {
            long startTime = System.currentTimeMillis();
            startGate.countDown();
            endGate.await();
            System.out.println("org.ybygjy.jcip.chap5.ConcurrentMapLockStriping.doTestConcurrentHashMap(int)#��ʱ��" + (new Double(System.currentTimeMillis() - startTime) / 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void doTestHashMap(int nThreads) {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        final Map<String, Object> unSynchData = Collections.synchronizedMap(new HashMap<String, Object>());
//        final Map<String, Object> unSynchData = (new HashMap<String, Object>());
        for (int i = 1; i <= nThreads; i++) {
            new Thread("SyncHashMapTestThread_" + i){
                public void run() {
                    try {
                        startGate.await();
                        try {
                            new HashMapTest(unSynchData, getName()).run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        try {
            long startTime = System.currentTimeMillis();
            startGate.countDown();
            endGate.await();
            System.out.println("org.ybygjy.jcip.chap5.ConcurrentMapLockStriping.doTestHashMap(int)#��ʱ��" + new Double(System.currentTimeMillis() - startTime) / 1000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    class ConcurrentHashMapTest implements Runnable {
        /** ѭ������*/
        private static final int MAX_CYCLECOUNT = 10;
        private ConcurrentHashMap<String, Object> concurrentHashMap;
        private String threadName;
        public ConcurrentHashMapTest(String threadName, ConcurrentHashMap<String, Object> concurrentHashMap) {
            this.threadName = threadName;
            this.concurrentHashMap = concurrentHashMap;
        }
        @Override
        public void run() {
            int count = 1;
            while (count <= MAX_CYCLECOUNT) {
                String key = threadName + String.valueOf((int) (Math.random() * 1000));
                this.concurrentHashMap.put(key, String.valueOf(Math.random()));
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class HashMapTest implements Runnable {
        /** ѭ������*/
        private static final int MAX_CYCLECOUNT = 10;
        private Map<String, Object> hashMap;
        private String threadName;
        public HashMapTest(Map<String, Object> hashMap, String threadName) {
            this.hashMap = hashMap;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            int count = 1;
            while (count <= MAX_CYCLECOUNT) {
                String key = threadName + String.valueOf((int) (Math.random() * 1000));
                this.hashMap.put(key, String.valueOf(Math.random()));
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

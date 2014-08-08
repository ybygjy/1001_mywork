package org.ybygjy.jcip.chap5;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
=======
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a

/**
 * ����ʵ��{@link HashMap}��{@link ConcurrentHashMap}���������ܲ���
 * @author WangYanCheng
 * @version 2014��7��31��
 */
public class HashMapPerformanceTesting {
    /** �������贴�����߳�����*/
    private int threadNums;
    /** ���߳�ѭ������*/
    private int loopNums;
    /** ����Ŀ�������Ķ�/д����*/
    private float rwRatio;
    /** ����������*/
    private final Map<Object, Object> targetContainer;
<<<<<<< HEAD
=======
    /** ��¼�ܺ�ʱ*/
    private volatile long totalSpendTime;
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
    /**
     * ���췽��
     * @param threadNums �߳�����
     * @param loopNums ѭ������
     * @param rwRatio ��/д����
     * @param targetContainer ����������
     */
    public HashMapPerformanceTesting(int threadNums, int loopNums, float rwRatio, Map<Object, Object> targetContainer) {
        this.threadNums = threadNums;
        this.loopNums = loopNums;
        this.rwRatio = rwRatio;
        this.targetContainer = targetContainer;
<<<<<<< HEAD
    }
    /**
     * �����������
     */
    public void doTest() {
        
=======
        this.totalSpendTime = 0L;
    }
    /**
     * �����������
     * @throws InterruptedException �߳��ж��쳣
     */
    public void doTest() throws InterruptedException {
        final CountDownLatch beginGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(threadNums);
        final Random random = new Random((int)(this.threadNums * this.loopNums * this.rwRatio));
        for (int i = 1; i <= threadNums; i++) {
            new Thread("HMTThread_" + targetContainer.getClass().getSimpleName() + "_" + i){
                @Override
                public void run() {
                    try {
                        beginGate.await();
                        for (int t = 0; t < loopNums; t++) {
                            int key = random.nextInt();
                            if (targetContainer.get(key) == null) {
                                targetContainer.put(key, key);
                            }
                        }
                        endGate.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        long startTime = System.currentTimeMillis();
        beginGate.countDown();
        endGate.await();
        totalSpendTime = System.currentTimeMillis() - startTime;
System.out.println(targetContainer.getClass() + "#" + toString());
    }
    
    @Override
    public String toString() {
        return "HashMapPerformanceTesting [threadNums=" + threadNums + ", loopNums=" + loopNums
            + ", rwRatio=" + rwRatio + ", totalSpendTime="
            + totalSpendTime + "]";
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        //���������ĳ���
        //�������#��ʼ��
        //�������#������
        //�����ʼ��ڣ����𴴽��߳�
<<<<<<< HEAD
=======
        /*�����ڽ��в��Ե�ʱ��������⣬HashMap���̰߳�ȫ���ڲ��Թ����г�������������������ԭ��δ��δ�ҵ���*/
        Map[] mapArray = {
//            new HashMap<Object, Object>(),
            new ConcurrentHashMap<Object, Object>(),
            Collections.synchronizedMap(new HashMap<Object, Object>()),
            new Hashtable<Object, Object>()
            };
        for (Map<Object, Object> targetMap : mapArray) {
            HashMapPerformanceTesting hmptInst = new HashMapPerformanceTesting(10, 100, 1F, targetMap);
            try {
                hmptInst.doTest();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
    }
}

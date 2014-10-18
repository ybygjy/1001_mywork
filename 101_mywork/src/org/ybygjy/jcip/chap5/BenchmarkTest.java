package org.ybygjy.jcip.chap5;

import java.util.concurrent.CountDownLatch;

import org.ybygjy.jcip.chap5.bct.BenchmarkMapWrapperImpl4ConcurrentMap;
import org.ybygjy.jcip.chap5.bct.BenchmarkMapWrapperImpl4HashTable;
import org.ybygjy.jcip.chap5.bct.BenchmarkMapWrapperImpl4RWLock;
import org.ybygjy.jcip.chap5.bct.BenchmarkMapWrapperImpl4ReentLock;
import org.ybygjy.jcip.chap5.bct.BenchmarkMapWrapperImpl4SynchroFactoryMap;
import org.ybygjy.jcip.chap5.bct.BenchmarkMapWrapperImpl4WriteLock;

/**
 * ���������˶�Java�������Ļ������ܲ��ԣ���Ҫע��ԭ���ߵ����˼·�ǳ�����ͨ����
 * <p>Դ��ַ��http://www.kafka0102.com/2010/08/298.html</p>
 * <p>1��ʹ�ñ�������ȴ�����߳�ȫ��ִ����ɵ�����</p>
 * <p>2�����屻���������ӿ�ͳһ�������</p>
 * @version 2014��7��31��
 */
public class BenchmarkTest {
    /** �ܺ�ʱ*/
    public volatile long totalTime;
    /** ����_���ڿ��ƶ�������߳�ͬʱ��ʼ���ȴ�ͬʱ������ʶ*/
    private CountDownLatch latch;
    /** ���̶߳Բ�����Եļ���ִ�ж���дѭ���Ĵ���*/
    private final int loop;
    /** ����ʱͬʱ���е��߳�����*/
    private final int threads;
    /** Ƶ�����ӣ����ڿ��Ʋ�����������Ķ�/дƵ��*/
    private final float ratio;

    /**
     * ���캯��
     * @param loop ѭ������
     * @param threads �����߳�����
     * @param ratio ����������/дƵ������
     */
    public BenchmarkTest(final int loop, final int threads, final float ratio) {
        this.loop = loop;
        this.threads = threads;
        this.ratio = ratio;
    }
    /**
     * ����
     * @param mapWrapper
     */
    private void doTest(final BenchmarkMapWrapper mapWrapper) {
        final float maxSize = loop * threads * ratio;
        totalTime = 0;
        //ִ��4��ȡƽ����ʱ
        for (int k = 0; k < 4; k++) {
            latch = new CountDownLatch(threads);
            for (int i = 0; i < threads; i++) {
            	BenchmarkRunnable bmrInst = new BenchmarkRunnable(mapWrapper, (int) maxSize, loop, latch, new BenchmarkRunnableCallback() {
					@Override
					public void callback(BenchmarkRunnable benchmarkRunnable) {
						totalTime += benchmarkRunnable.getTimeConsume();
					}
				});
                new Thread(bmrInst, mapWrapper + "_" + i).start();
//System.out.println(mapWrapper.getName() + ":" + this.totalTime);
            }
            try {
                latch.await();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            mapWrapper.clear();
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            try {
                Thread.sleep(1000);
            } catch (final Exception e) {
            	e.printStackTrace();
            }
        }
        System.out.println("[" + mapWrapper.getName() + "]�߳���[" + threads + "]��/д����[" + this.ratio + "]ƽ��ʱ��(��)[" + (totalTime / 4.0)/(1000*1000*1000) + "]");
    }

    /**
     * �������
     * @param loop ѭ������
     * @param threads �߳�����
     * @param ratio R/W����
     */
    public static void doTest(final int loop, final int threads, final float ratio) {
        final BenchmarkTest benchMark = new BenchmarkTest(loop, threads, ratio);
        final BenchmarkMapWrapper[] wrappers = new BenchmarkMapWrapper[] {
	        new BenchmarkMapWrapperImpl4SynchroFactoryMap(),
	        new BenchmarkMapWrapperImpl4HashTable(),
	        new BenchmarkMapWrapperImpl4ReentLock(),
	        new BenchmarkMapWrapperImpl4WriteLock(),
	        new BenchmarkMapWrapperImpl4ConcurrentMap(),
	        new BenchmarkMapWrapperImpl4RWLock()
        };
        for (final BenchmarkMapWrapper wrapper : wrappers) {
            benchMark.doTest(wrapper);
        }
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(final String[] args) {
        doTest(100, 10, 1);// r:w 1:1
        /*
        doTest(100, 10, 0.1f);// r:w 10:1
        doTest(100, 10, 0.01f);// r:w 100:1
        doTest(100, 10, 0.001f);// r:w 1000:1
        // ///
        doTest(100, 50, 0.1f);// r:w 10:1
        doTest(100, 50, 0.01f);// r:w 100:1
        doTest(100, 50, 0.001f);// r:w 1000:1
        // ///
        doTest(100, 100, 1f);// r:w 1:1
        doTest(100, 100, 0.1f);// r:w 10:1
        doTest(100, 100, 0.01f);// r:w 100:1
        doTest(100, 100, 0.001f);// r:w 1000:1
        // //
        */
    }
}

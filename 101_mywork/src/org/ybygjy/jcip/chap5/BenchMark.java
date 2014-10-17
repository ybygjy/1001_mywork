package org.ybygjy.jcip.chap5;

import java.util.concurrent.CountDownLatch;

import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4ConcurrentMap;
import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4HashTable;
import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4RWLock;
import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4ReentLock;
import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4SynchroFactoryMap;
import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4WriteLock;

/**
 * ���������˶�Java�������Ļ������ܲ��ԣ���Ҫע���������˼��ǳ�����˼
 * <p>Դ��ַ��http://www.kafka0102.com/2010/11/405.html</p>
 * <p>1��ʹ�ñ�������ȴ�����߳�ȫ��ִ����ɵ�����</p>
 * <p>2�����屻���������ӿ�ͳһ���Է���</p>
 * @version 2014��7��31��
 */
public class BenchMark {
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
    public BenchMark(final int loop, final int threads, final float ratio) {
        this.loop = loop;
        this.threads = threads;
        this.ratio = ratio;
    }
    /**
     * ����
     * @param mapWrapper
     */
    private void doTest(final BenchMarkMapWrapper mapWrapper) {
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
        final BenchMark benchMark = new BenchMark(loop, threads, ratio);
        final BenchMarkMapWrapper[] wrappers = new BenchMarkMapWrapper[] {
	        new BenchMarkMapWrapperImpl4SynchroFactoryMap(),
	        new BenchMarkMapWrapperImpl4HashTable(),
	        new BenchMarkMapWrapperImpl4ReentLock(),
	        new BenchMarkMapWrapperImpl4WriteLock(),
	        new BenchMarkMapWrapperImpl4ConcurrentMap(),
	        new BenchMarkMapWrapperImpl4RWLock()
        };
        for (final BenchMarkMapWrapper wrapper : wrappers) {
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

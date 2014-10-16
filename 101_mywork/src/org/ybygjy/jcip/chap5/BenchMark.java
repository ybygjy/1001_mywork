package org.ybygjy.jcip.chap5;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4HashTable;
import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4ReentLock;
import org.ybygjy.jcip.chap5.bct.BenchMarkMapWrapperImpl4SynchroFactoryMap;

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
        final float size = loop * threads * ratio;
        totalTime = 0;
        for (int k = 0; k < 3; k++) {
            latch = new CountDownLatch(threads);
            for (int i = 0; i < threads; i++) {
                new Thread(new BenchMarkRunnable(mapWrapper, (int) size, loop)).start();
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
            }
        }
        final int rwratio = (int) (1.0 / ratio);
        System.out.println("[" + mapWrapper.getName() + "]�߳���[" + threads + "]��/д����[" + rwratio + "]ƽ��ʱ��[" + totalTime / 3 + "]");
    }

    public static void doTest(final int loop, final int threads, final float ratio) {
        final BenchMark benchMark = new BenchMark(loop, threads, ratio);
        final BenchMarkMapWrapper[] wrappers = new BenchMarkMapWrapper[] {
	        new BenchMarkMapWrapperImpl4SynchroFactoryMap(),
	        new BenchMarkMapWrapperImpl4HashTable(),
	        new BenchMarkMapWrapperImpl4ReentLock(),
	        new RWLockMapWrapper(),
	        new ConcurrentMapWrapper(),
	        new WriteLockMapWrapper()
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
    }
}

/**
 * ����������ֻ����д��������Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class WriteLockMapWrapper implements BenchMarkMapWrapper {
    private final Map<Object, Object> map;
    private final Lock lock;
    public WriteLockMapWrapper() {
        map = new HashMap<Object, Object>();
        lock = new ReentrantLock();
    }
    @Override
    public void clear() {
        lock.lock();
        try {
            map.clear();
        } catch (final Exception e) {
        } finally {
            lock.unlock();
        }
    }
    @Override
    public Object get(final Object key) {
        return map.get(key);
    }
    @Override
    public void put(final Object key, final Object value) {
        lock.lock();
        try {
            map.put(key, value);
        } catch (final Exception e) {
        } finally {
            lock.unlock();
        }
    }
    @Override
    public String getName() {
        return "writelock";
    }
}

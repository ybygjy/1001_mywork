package org.ybygjy.jcip.chap5;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ���ฺ���Java�������Ļ������ܲ��ԣ�ע���������˼��߱��ƹ��ֵ
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
    /** ���̶߳Բ�����Լ���ִ�ж���дѭ���Ĵ���*/
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
     * �ڲ��ฺ��ִ����������Ļ�׼����
     * @author WangYanCheng
     * @version 2014��8��8��
     */
    class BenchMarkRunnable implements Runnable {
        /** ����������*/
        private final MapWrapper mapWrapper;
        /** */
        private final int size;
        /**
         * Constructor
         * @param mapWrapper ����������
         * @param mapSize �����������洢Ԫ�ص�Ŀ���С 
         */
        public BenchMarkRunnable(final MapWrapper mapWrapper, final int mapSize) {
            this.mapWrapper = mapWrapper;
            this.size = mapSize;
        }
        public void benchmarkRandomReadPut(final MapWrapper mapWrapper, final int loop) {
            final Random random = new Random();
            int writeTime = 0;
            for (int i = 0; i < loop; i++) {
                final int n = random.nextInt(size);
                if (mapWrapper.get(n) == null) {
                    mapWrapper.put(n, n);
                    writeTime++;
                }
            }
            System.out.println(Thread.currentThread().getName() + "_WriteTime=" + writeTime);
        }
        @Override
        public void run() {
            final long start = System.currentTimeMillis();
            benchmarkRandomReadPut(mapWrapper, loop);
            final long end = System.currentTimeMillis();
            totalTime += end - start;
            latch.countDown();
        }
    }
    public void doTest(final MapWrapper mapWrapper) {
        final float size = loop * threads * ratio;
        totalTime = 0;
        for (int k = 0; k < 3; k++) {
            latch = new CountDownLatch(threads);
            for (int i = 0; i < threads; i++) {
                new Thread(new BenchMarkRunnable(mapWrapper, (int) size)).start();
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
        final MapWrapper[] wrappers = new MapWrapper[] { new HashTableMapWrapper(),
        new SyncMapWrapper(),
        new LockMapWrapper(),
        new RWLockMapWrapper(),
        new ConcurrentMapWrapper(),
        new WriteLockMapWrapper()
        };
        for (final MapWrapper wrapper : wrappers) {
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
        doTest(100, 100, 1f);// r:w 10:1
        doTest(100, 100, 0.1f);// r:w 10:1
        doTest(100, 100, 0.01f);// r:w 100:1
        doTest(100, 100, 0.001f);// r:w 1000:1
        // //
    }
}

/**
 * HashTable����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class HashTableMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;

    public HashTableMapWrapper() {
        map = new Hashtable<Object, Object>();
    }
    @Override
    public void clear() {
        map.clear();
    }
    @Override
    public Object get(final Object key) {
        return map.get(key);
    }
    @Override
    public void put(final Object key, final Object value) {
        map.put(key, value);
    }
    @Override
    public String getName() {
        return "hashtable";
    }
}

/**
 * ����ʵ������ͬ����Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class SyncMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;
    public SyncMapWrapper() {
        map = new HashMap<Object, Object>();
    }
    @Override
    public synchronized void clear() {
        map.clear();
    }
    @Override
    public synchronized Object get(final Object key) {
        return map.get(key);
    }
    @Override
    public synchronized void put(final Object key, final Object value) {
        map.put(key, value);
    }
    @Override
    public String getName() {
        return "synclock";
    }
}

/**
 * �������������ƶ�/д������Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class LockMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;
    private final Lock lock;
    public LockMapWrapper() {
        map = new HashMap<Object, Object>();
        lock = new ReentrantLock();
    }
    @Override
    public void clear() {
        lock.lock();
        try {
            map.clear();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    @Override
    public Object get(final Object key) {
        lock.lock();
        try {
            return map.get(key);
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
    @Override
    public void put(final Object key, final Object value) {
        lock.lock();
        try {
            map.put(key, value);
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    @Override
    public String getName() {
        return "mutexlock";
    }
}

/**
 * ���п������д�����Ʋ�����Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class RWLockMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;
    private final Lock readLock;
    private final Lock writeLock;
    public RWLockMapWrapper() {
        map = new HashMap<Object, Object>();
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            map.clear();
        } catch (final Exception e) {
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Object get(final Object key) {
        readLock.lock();
        try {
            return map.get(key);
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return null;
    }

    @Override
    public void put(final Object key, final Object value) {
        writeLock.lock();
        try {
            map.put(key, value);
        } catch (final Exception e) {
        } finally {
            writeLock.unlock();
        }
    }
    @Override
    public String getName() {
        return "rwlock";
    }
}
/**
 * JDK1.5����Ĳ�������Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class ConcurrentMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;
    public ConcurrentMapWrapper() {
        map = new ConcurrentHashMap<Object, Object>();
    }
    @Override
    public void clear() {
        map.clear();
    }
    @Override
    public Object get(final Object key) {
        return map.get(key);
    }
    @Override
    public void put(final Object key, final Object value) {
        map.put(key, value);
    }
    @Override
    public String getName() {
        return "concrrent";
    }
}

/**
 * ����������ֻ����д��������Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class WriteLockMapWrapper implements MapWrapper {
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
/**
 * ���屻����Map���ϵ���Ϊ�����������Ե����������������Ϊ����put/get
 * @author WangYanCheng
 * @version 2014��8��8��
 */
interface MapWrapper {
    /**
     * ֵ�洢
     * @param key �ؼ���
     * @param value ֵ
     */
    void put(Object key, Object value);

    /**
     * ���ݹؼ���ȡֵ
     * @param key �ؼ���
     * @return rtnVal ֵ
     */
    Object get(Object key);

    /**
     * ������ɺ�����ͷ�ռ�õ��ڴ�ռ�
     */
    void clear();

    /**
     * ���ز���ʵ��ı�ʶ
     * @return rtnName
     */
    String getName();
}
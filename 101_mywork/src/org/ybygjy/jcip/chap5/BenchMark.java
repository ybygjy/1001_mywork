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
<<<<<<< HEAD
 * <p>1��ʹ�ñ�������ȴ�����߳�ȫ��ִ����ɵ�����</p>
 * <p>2����װ��</p>
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
=======
 * @version 2014��7��31��
 */
public class BenchMark {

    public volatile long totalTime;

    private CountDownLatch latch;

    private final int loop;

    private final int threads;

    private final float ratio;

    public BenchMark(final int loop, final int threads, final float ratio) {
        super();
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
        this.loop = loop;
        this.threads = threads;
        this.ratio = ratio;
    }
<<<<<<< HEAD
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
=======
    class BenchMarkRunnable implements Runnable {

        private final MapWrapper mapWrapper;

        private final int size;

        public BenchMarkRunnable(final MapWrapper mapWrapper, final int size) {
            this.mapWrapper = mapWrapper;
            this.size = size;
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
    public void doTest(final MapWrapper mapWrapper) {
=======
    public void benchmark(final MapWrapper mapWrapper) {
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
        System.out.println("[" + mapWrapper.getName() + "]�߳���[" + threads + "]��/д����[" + rwratio + "]ƽ��ʱ��[" + totalTime / 3 + "]");
    }

    public static void doTest(final int loop, final int threads, final float ratio) {
=======
        System.out.println("[" + mapWrapper.getName() + "]threadnum[" + threads + "]ratio[" + rwratio
            + "]avgtime[" + totalTime / 3 + "]");
    }

    public static void benchmark2(final int loop, final int threads, final float ratio) {
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
        final BenchMark benchMark = new BenchMark(loop, threads, ratio);
        final MapWrapper[] wrappers = new MapWrapper[] { new HashTableMapWrapper(),
        new SyncMapWrapper(),
        new LockMapWrapper(),
        new RWLockMapWrapper(),
        new ConcurrentMapWrapper(),
        new WriteLockMapWrapper()
        };
        for (final MapWrapper wrapper : wrappers) {
<<<<<<< HEAD
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
=======
            benchMark.benchmark(wrapper);
        }
    }
    public static void test() {
        benchmark2(100, 10, 1);// r:w 1:1
        benchmark2(100, 10, 0.1f);// r:w 10:1
        benchmark2(100, 10, 0.01f);// r:w 100:1
        benchmark2(100, 10, 0.001f);// r:w 1000:1
        // ///
        benchmark2(100, 50, 0.1f);// r:w 10:1
        benchmark2(100, 50, 0.01f);// r:w 100:1
        benchmark2(100, 50, 0.001f);// r:w 1000:1
        // ///
        benchmark2(100, 100, 1f);// r:w 10:1
        benchmark2(100, 100, 0.1f);// r:w 10:1
        benchmark2(100, 100, 0.01f);// r:w 100:1
        benchmark2(100, 100, 0.001f);// r:w 1000:1
        // //
    }
    public static void main(final String[] args) {
        test();
    }
}

>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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

<<<<<<< HEAD
/**
 * ����ʵ������ͬ����Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class SyncMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;
=======
class SyncMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;

>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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

<<<<<<< HEAD
/**
 * �������������ƶ�/д������Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
=======
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
            e.printStackTrace();
=======
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
            e.printStackTrace();
=======
            // TODO: handle exception
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
            e.printStackTrace();
=======
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
        } finally {
            lock.unlock();
        }
    }
    @Override
    public String getName() {
        return "mutexlock";
    }
}

<<<<<<< HEAD
/**
 * ���п������д�����Ʋ�����Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
class RWLockMapWrapper implements MapWrapper {
    private final Map<Object, Object> map;
    private final Lock readLock;
    private final Lock writeLock;
=======
class RWLockMapWrapper implements MapWrapper {

    private final Map<Object, Object> map;

    private final Lock readLock;

    private final Lock writeLock;

>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
            e.printStackTrace();
=======
            // TODO: handle exception
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
/**
 * JDK1.5����Ĳ�������Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
=======
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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

<<<<<<< HEAD
/**
 * ����������ֻ����д��������Map����
 * @author WangYanCheng
 * @version 2014��8��8��
 */
=======
>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
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
<<<<<<< HEAD
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
=======
interface MapWrapper {

    void put(Object key, Object value);

    Object get(Object key);

    void clear();

>>>>>>> 158d2ea208de16f43c5005313a35bb9c01caa13a
    String getName();
}
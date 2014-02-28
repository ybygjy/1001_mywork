package org.ybygjy.memcached;

import java.util.Date;
import java.util.Map;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * MemCached���ʵ��
 * @author WangYanCheng
 * @version 2011-5-6
 */
public class MemCachedMgr {
    /** MemCached��������ַ */
    private String[] serverAddr = {"172.16.0.75:11211", "localhost:11211"};
    /** Ȩ�� */
    private Integer[] weights = {2, 3};
    /** MemCached���� */
    private MemCachedClient mccInst;
    /** ���ӳ� */
    private SockIOPool sockPool;
    /** ��Ĭ���������� */
    private int initConn = 5;
    /** ����С�������� */
    private int minConn = 5;
    /** ������������� */
    private int maxConn = 100;
    /** �����������ʱ��(����) */
    private long maxIdle = 1000 * 60 * 60 * 6;
    /** �ص�ά���߳���ѭʱ�� */
    private int maintSleep = 30;
    /** �����ֽڻ����ǣ������漰��һ������Nagle���㷨 */
    private boolean nagle = false;
    /** ���糬ʱʱ�� */
    private int socketTo = 3000;
    /** �������ӳ�ʱʱ�� */
    private int connectTo = 0;

    /**
     * ���췽�����ṩ��ʼ��MemCached��������
     * @param serverAddr MemCached��������ַ��
     * @param weights MemCached��������Ӧ��Ȩ������
     */
    public MemCachedMgr(String[] serverAddr, Integer[] weights) {
        this.serverAddr = serverAddr == null ? this.serverAddr : serverAddr;
        this.weights = weights;
        initializeSocketPool();
        mccInst = new MemCachedClient();
    }

    /**
     * ��ʼ�����ӳػ���
     */
    private void initializeSocketPool() {
        sockPool = SockIOPool.getInstance();
        sockPool.setServers(serverAddr);
        sockPool.setWeights(weights);
        sockPool.setInitConn(initConn);
        sockPool.setMinConn(minConn);
        sockPool.setMaxConn(maxConn);
        sockPool.setMaxIdle(maxIdle);
        sockPool.setMaintSleep(maintSleep);
        sockPool.setNagle(nagle);
        sockPool.setSocketTO(socketTo);
        sockPool.setSocketConnectTO(connectTo);
        sockPool.initialize();
    }

    /**
     * ɾ��MemCached����ָ����ƥ�����
     * @param key ָ����
     * @return ɾ����� true(�ɹ�ɾ��)/false(ʧ��)
     */
    public boolean delete(String key) {
        return mccInst.delete(key);
    }

    /**
     * ��MemCached����ϵͳ��ȡ����
     * @param key ��
     * @return value/null
     */
    public Object get(String key) {
        return mccInst.get(key);
    }

    /**
     * ������MemCached��ȡ��һ������
     * @param keys ָ�����������<strong>����</strong>�ָ�
     * @return dataArray ���ݼ�
     */
    public Map<String, Object> getMulti(String... keys) {
        if (null == keys || keys.length == 0) {
            return null;
        }
        return mccInst.getMulti(keys);
    }

    /**
     * ���������Ƿ�����MemCached��
     * @param key ָ����
     * @return true/false
     */
    public boolean isContain(String key) {
        return mccInst.keyExists(key);
    }

    /**
     * ��������
     * <p>
     * <strong>ע�⣺</strong>�÷����Ḳ�ǵ���������ͬkey������
     * </p>
     * @param key ��
     * @param value ֵ
     * @return �洢�����true(�ɹ�)/false(ʧ��)
     */
    public boolean set(String key, Object value) {
        return set(key, value, true);
    }

    /**
     * ��������
     * @param key ��
     * @param value ֵ
     * @param overwrite ���Ǳ��
     * @return �洢�����true(�ɹ�)/false(ʧ��)
     */
    public boolean set(String key, Object value, boolean overwrite) {
        if (!overwrite && isContain(key)) {
            return false;
        }
        return mccInst.set(key, value);
    }

    /**
     * ��������
     * @param key ��
     * @param value ֵ
     * @return �洢�����true(�ɹ�)/false(ʧ��)
     */
    public boolean add(String key, Object value) {
        return mccInst.add(key, value);
    }

    /**
     * ��������
     * @param key ��
     * @param value ֵ
     * @param expiry ʧЧʱ��
     * @return �洢�����true(�ɹ�)/false(ʧ��)
     */
    public boolean add(String key, Object value, Date expiry) {
        return mccInst.add(key, value, expiry);
    }

    /**
     * ��ȥ����Ӧ���������ֵ���������Ӧ��������������Զ�������
     * @param key ָ����
     * @param value �ݼ���
     * @return ������ֵ
     */
    public long addOrDecr(String key, long value) {
        long tmpV = mccInst.addOrDecr(key, value);
        return tmpV;
    }

    /**
     * ���Ӽ���Ӧ���������ֵ���������Ӧ��������������Զ�������
     * @param key ָ����
     * @param value ����
     * @return ������ֵ
     */
    public long addOrIncr(String key, long value) {
        return mccInst.addOrIncr(key, value);
    }

    /**
     * ����Ӧ���������ֵ����ָ����ֵ
     * <p>
     * <strong>ע�⣺</strong>�����ֵ���Ͳ�����Ҫ�����Զ�תΪ0�������أ�
     * </p>
     * @param key ָ����
     * @param value ָ����ֵ
     * @return �������ֵ/-1(��ʾ����δ�ɹ�)
     */
    public long incr(String key, long value) {
        return mccInst.incr(key, value);
    }

    /**
     * ����Ӧ���������ֵ��ȥָ����ֵ
     * <p>
     * <strong>ע�⣺</strong>�����ֵ���Ͳ�����Ҫ�����Զ�תΪ0�������أ�
     * </p>
     * @param key ָ����
     * @param value ָ����ֵ
     * @return �������ֵ/-1(��ʾ����δ�ɹ�)
     */
    public long decr(String key, long value) {
        return mccInst.decr(key, value);
    }

    /**
     * ˢ��ָ��MemCached�������Ļ�������
     * <p>
     * <strong>ע�⣺</strong>ˢ�¼�������Ӧ�������ϵ����ݣ������أ�
     * </p>
     * @param serverAddr MemCached��������ַ
     * @return ������ true(�ɹ�)/false(ʧ��)
     */
    public boolean flushAll(String... serverAddr) {
        if (null == serverAddr || serverAddr.length == 0) {
            return false;
        }
        return mccInst.flushAll(serverAddr);
    }

    /**
     * �����������ݼ�
     * @param size �������ݼ���С
     * @return testAttr �������ݼ�
     */
    public static String[][] getTestData(int size) {
        String[][] testAttr = new String[size][2];
        for (int i = 0; i < size; i++) {
            testAttr[i][0] = "KEY_" + i;
            testAttr[i][1] = "VALUE_" + Math.random();
        }
        return testAttr;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        MemCachedMgr mcmInst = new MemCachedMgr(null, null);
        String[][] dataStr = mcmInst.getTestData(10);
        for (int i = dataStr.length - 1; i >= 0; i--) {
            System.out.println(mcmInst.add(dataStr[i][0], dataStr[i][1]));
        }
    }
}

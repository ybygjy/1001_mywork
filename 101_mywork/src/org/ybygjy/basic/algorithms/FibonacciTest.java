package org.ybygjy.basic.algorithms;

import java.util.HashMap;
import java.util.Map;

/**
 * ��֤���ֵݹ�ʵ��Fibonacci�ķ���
 * <p>
 * 1��ֱ��ʹ�ö�ջ
 * <p>
 * 2�����뻺��ĸ���
 * @author WangYanCheng
 * @version 2012-9-5
 */
public class FibonacciTest {
    /** ���� */
    private Map<Integer, Long> cacheMap = new HashMap<Integer, Long>();

    /**
     * ��һ�ַ�ʽ
     * @param i Ҫ�����fibonacci��
     * @return rtnNum fibonacciֵ
     */
    private long fibonacciOne(int i) {
        if (i == 0 || i == 1) {
            return i;
        }
        return fibonacciOne(i - 1) + fibonacciOne(i - 2);
    }

    /**
     * �ڶ��ַ�ʽ_ʹ�û���
     * @param i Ҫ�����Fibonacci��
     * @return rtnNum fibonacciֵ
     */
    private long fibonacciTwo(int i) {
        if (cacheMap.containsKey(i)) {
            return cacheMap.get(i);
        }
        if (i == 0 || i == 1) {
            cacheMap.put(i, (long) i);
        } else {
            cacheMap.put(i, fibonacciTwo(i - 1) + fibonacciTwo(i - 2));
        }
        return cacheMap.get(i);
    }

    /**
     * �������
     * @param n fibonacci����
     */
    public void doWork(int n) {
        long beginTime = System.currentTimeMillis();
        long rtnValue = this.fibonacciOne(n);
        long endTime = System.currentTimeMillis();
        double diff = endTime - beginTime;
        System.out.println("��һ�֣���ʱ��".concat(String.valueOf(diff / (1000*60))).concat("�룬ֵ��".concat(String.valueOf(rtnValue))));
        beginTime = System.currentTimeMillis();
        rtnValue = this.fibonacciTwo(n);
        endTime = System.currentTimeMillis();
        diff = endTime - beginTime;
        System.out.println("�ڶ��֣���ʱ��".concat(String.valueOf(diff / (1000*60))).concat("�룬ֵ��".concat(String.valueOf(rtnValue))));
    }

    /**
     * �������
     * @param args
     */
    public static void main(String[] args) {
        FibonacciTest ftInst = new FibonacciTest();
        ftInst.doWork(50);
    }
}

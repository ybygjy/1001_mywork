package org.ybygjy.basic.ch_110_generic;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * ���巺����
 * @author WangYanCheng
 * @version 2014-6-13
 */
public class Pair <T> implements Serializable {
    private T first;
    private T second;
    public Pair() {
    }
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }
    public T getFirst() {
        return first;
    }
    public T getSecond() {
        return second;
    }
    public void setFirst(T first) {
        this.first = first;
    }
    public void setSecond(T second) {
        this.second = second;
    }
    /**
     * ���ͷ���
     * @param <T> ���ͱ���
     * @param t ����
     * @return rtnValue T
     */
    public <T1 extends T> T1 getMiddle(T1[] t) {
        return t[t.length / 2];
    }
    /**
     * �������ͱ������޶�
     * @param <T> ���ͱ����������޶����ͱ���ʵ����{@link Comparable}�ӿ�
     * @param a ����
     * @return rtnT
     */
    public <T extends Comparable<T>> T min(T[] a) {
        if (a == null || a.length == 0) {
            return null;
        }
        T smallest = a[0];
        for (int i = 1; i < a.length; i++) {
            if (smallest.compareTo(a[i]) > 0) {
                smallest = a[i];
            }
        }
        return smallest;
    }
    /**
     * ���ͷ���#Ѱ�����������Сֵ
     * @param <T> ���ͱ���������ʵ��{@link Comparable}�ӿ�
     * @param a ����
     * @return rtnT
     */
    public static <T1 extends Comparable> Pair<T1> minMax(T1[] a) {
        if (null == a) {
            return null;
        }
        T1 min = a[0];
        T1 max = min;
        for (int i = 1; i < a.length; i++) {
            if (min.compareTo(a[i]) > 0) {
                min = a[i];
            }
            if (max.compareTo(a[i]) < 0) {
                max = a[i];
            }
        }
        return new Pair<T1>(max, min);
    }
    /**
     * ���쳣������ʹ�����ͱ���
     * @param <T> ���ͱ���
     * @throws T �޶��˲�������Ϊ{@link Throwable}��������
     */
    public static <T extends Throwable> void throwException(T t) throws T {
        int b = 10;
        try {
            b = b/0;
        } catch (Exception e) {
            t.initCause(e.fillInStackTrace());
            throw t;
        }
    }
    /**
     * ��������
     */
    public static void genericTypeArray() {
        Pair<String>[] t = new Pair[10];
        for (int i = 0; i < t.length; i++) {
            t[i] = new Pair<String>("" + i, "" + i);
        }
        for (Pair<String> tItem : t) {
            System.out.println(tItem);
        }
    }
    @Override
    public String toString() {
        return "Pair [first=" + first + ", second=" + second + "]";
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        testDoWork1();
        testDoWork2();
        testThrowException();
        testGenericArray();
    }
    /**
     * ����1
     */
    public static void testDoWork1() {
        String[] words = {"Mary", "had", "a", "little", "lamb"};
        String min = words[0];
        String max = min;
        for (int i = 0; i < words.length; i++) {
            if (min.compareTo(words[i]) > 0) {
                min = words[i];
            }
            if (max.compareTo(words[i]) < 0) {
                max = words[i];
            }
        }
        Pair<String> minMax = new Pair<String>(min, max);
        String middleName = minMax.<String>getMiddle(words);
        System.out.println("Max:" + minMax.getFirst());
        System.out.println("Min:" + minMax.getSecond());
        System.out.println("MiddleName:" + middleName);
    }
    /**
     * ����2
     */
    public static void testDoWork2() {
        GregorianCalendar[] gregorianCalendar = {
            new GregorianCalendar(2008, Calendar.MARCH, 20),
            new GregorianCalendar(2011, Calendar.APRIL, 11),
            new GregorianCalendar(2013, Calendar.MAY, 23)
        };
        Pair<GregorianCalendar> minMaxPair = minMax(gregorianCalendar);
        System.out.println("Max:" + minMaxPair.getFirst());
        System.out.println("Min:" + minMaxPair.getSecond());
    }
    /**
     * ����3
     * ���쳣������ʹ���˷��ͱ���
     */
    public static void testThrowException() {
        try {
            Pair.throwException(new RuntimeException());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ����4
     * ��������
     * ����ʵ�������������͵�����
     */
    public static void testGenericArray() {
        Pair.genericTypeArray();
    }
}

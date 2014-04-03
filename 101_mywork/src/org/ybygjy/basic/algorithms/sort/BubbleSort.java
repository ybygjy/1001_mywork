package org.ybygjy.basic.algorithms.sort;

import java.util.Arrays;

/**
 * ð������
 * @author WangYanCheng
 * @version 2011-8-3
 */
public class BubbleSort {
    /**
     * ����
     * <p>��������򣬱�����������Сֵ���ϸ������Ͻ��ⲻ��ð������</p>
     * @param array �������������
     * @return ������ɵ�����
     */
    public int[] sortA(int[] a) {
        int[] array = a;
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                }
            }
        }
        return array;
    }
    /**
     * ˼·����
     * @param a �������������
     * @return ������ɵ�����
     */
    public int[] sortB(int[] a) {
        int[] array = a;
        for (int i = array.length - 1; i > 1; i--) {
            for (int j = 0; j < i; j++) {
                if (array[j] > array[i]) {
                    int tmp = array[j];
                    array[j] = array[i];
                    array[i] = tmp;
                }
            }
        }
        return array;
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        int[] arr = {0,2,1,3,7,7,1,5,4,6};
        BubbleSort bubbleSort = new BubbleSort();
        System.out.println(Arrays.toString(bubbleSort.sortA(arr)));
        System.out.println(Arrays.toString(bubbleSort.sortB(new int[]{4,3,2,1})));
    }
}

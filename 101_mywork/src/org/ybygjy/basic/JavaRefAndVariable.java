package org.ybygjy.basic;

/**
 * <h3>Java��ֵ������</h3>
 * <p>
 * 1�������Ͱ�ֵ����(pass by value)���ɸı�ֵ
 * </p>
 * <p>
 * 2���������ʹ��ݿɸı�ֵ
 * </p>
 * <p>
 * 3��String������Ĳ��ɱ���󣬴�����ֵ���ɸı�ֵ
 * </p>
 * <p>
 * 4�����鴫�����ã��ɸı������ڲ�ֵ�������ɸı���������
 * </p>
 * @author WangYanCheng
 * @version 2011-2-28
 */
public class JavaRefAndVariable {
    /**
     * �ַ������ò���
     * @param srcStr Դ�ַ���
     */
    public static void simpleType(String srcStr) {
        srcStr = srcStr.concat(srcStr).concat(srcStr);
        System.out.println("�����ڲ��ı����ֵ==>" + srcStr);
    }

    /**
     * ������������
     * @param i i
     */
    public static void simpleType(int i) {
        i = i++;
        System.out.println("�ı����������ֵ==>" + i);
    }

    /**
     * ������������
     * @param srcStr Դ�ַ���
     */
    public static void objectType(StringBuffer srcStr) {
        srcStr.append("�����ã��ɸı�ֵ");
    }

    /**
     * ������������
     * <p>
     * 1���ɸı�����ֵ
     * </p>
     * <p>
     * 2�����ɸı����鱾������
     * </p>
     * @param a ����
     */
    public static void arraySimpleType(int[] a) {
        a[2] = 10;
        a = new int[10];
    }

    /**
     * �������
     * @param arg �����б�
     */
    public static void main(String[] arg) {
        String srcStr = "ABCDEF";
        int i = 10;
        JavaRefAndVariable.simpleType(srcStr);
        JavaRefAndVariable.simpleType(i);
        StringBuffer sbufStr = new StringBuffer("Hello");
        int[] array = {1, 2, 3, 4};
        JavaRefAndVariable.objectType(sbufStr);
        JavaRefAndVariable.arraySimpleType(array);
    }
}

package org.ybygjy.basic.bit;
/**
 * λ����_����
 * @author WangYanCheng
 * @version 2010-3-31
 */
public class BitArithmetic {
    /**
     * ʹ�������ɲ���Ҫ��������,������ֵ�Ľ���
     * @param x x
     * @param y y
     */
    public void doSwap(int x, int y) {
        System.out.println("before swap==>" + x + ":" + y);
        x = x ^ y;
        y = x ^ y;
        x = x ^ y;
        System.out.println("after swap==>" + x + ":" + y);
    }
    /**
     * ȡ������
     * @param x x
     */
    public void notArith(int x) {
        short y = (short) x;
        byte[] byteInst = new byte[32];
        byteInst = Short.toString(y).getBytes();
        for (int index = 0; index < byteInst.length; index++) {
            System.out.println(Integer.toBinaryString(byteInst[index]));
        }
    }
    /**
     * �������
     * @param args arguments
     */
    public static void main(String[] args) {
        BitArithmetic brInst = new BitArithmetic();
        brInst.doSwap(10, 20);
        brInst.notArith(1);
    }
}

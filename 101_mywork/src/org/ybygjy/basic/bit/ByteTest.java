package org.ybygjy.basic.bit;

import java.util.Random;

/**
 * ��byte��صĲ���
 * @author WangYanCheng
 * @version 2010-6-12
 */
public class ByteTest {
    /**
     * ��ͨ�ֽڴ�ӡ
     */
    public void doPrintByte() {
        byte b1 = 127;
        p(b1);
    }
    /**
     * ��λ����
     * @param inParam inParam
     */
    public void doShiftTest(int[] inParam) {
        for (int tmpInt : inParam) {
            p("BEGIN " + tmpInt + ":" + Integer.toBinaryString(tmpInt));
            tmpInt = tmpInt >> 16;
            p("END " + tmpInt + ":" + Integer.toBinaryString(tmpInt));
        }
    }
    /**
     * �����������������
     * @param byteArr byteArr
     */
    public void doDomainTest(byte[] byteArr) {
        Random rand = new Random();
        for (int i = byteArr.length - 1; i >= 0; i--) {
            byteArr[i] = (byte) rand.nextInt();
        }
    }
    /**
     * ��������������������
     */
    public void doDomainTestMain() {
        byte[] byteArr = new byte[16];
        doDomainTest(byteArr);
        System.out.println(new String(byteArr));
    }
    /**
     * ���Զ�������������
     */
    public void doTest() {
        int flag = 0xF;
        p(Integer.toBinaryString(flag));
        //�ҵ�2λ���1111-->1011
        flag = flag & ~(1 << 2);
        p("�ҵ�3λ���0:" + Integer.toBinaryString(flag));
        flag = flag ^ (1 << 2);
        p("�ҵ�3λȡ��   :" + Integer.toBinaryString(flag));
        flag = flag & ((1 << 3) - 1);
        p("ȡδ3λ        :" + Integer.toBinaryString(flag));
        flag = flag & ((1 << 2) - 1);
        p("ȡδkλk=2:" + Integer.toBinaryString(flag));
        flag = (flag >> (2 - 1)) & 1;
        p("ȡ����kλk=2:" + Integer.toBinaryString(flag));
        flag = 0xF;
        flag = flag | ((1 << 3) - 1);
        p("��kλ��Ϊ1,k=3:" + Integer.toBinaryString(flag));
        flag = 0xF;
        flag = flag & (~((1 << 3) - 1));
        p("��kλ��Ϊ0,k=3:" + Integer.toBinaryString(flag));
        flag = 0xF;
        flag = flag ^ ((1 << 3) - 1);
        p("��kλȡ��,k=3:" + Integer.toBinaryString(flag));
        flag = 0xF;
        flag = flag & (flag + 1);
        p("������1��Ϊ0,k=3:" + Integer.toBinaryString(flag));
        flag = 0xF;
        flag = flag | (flag + 1);
        p("��һ��0��Ϊ1,k=3:" + Integer.toBinaryString(flag));
        flag = 0xF;
        flag = flag | (flag - 1);
        p("������0��Ϊ1,k=3:" + Integer.toBinaryString(flag));
        flag = (flag ^ (flag + 1)) >> 1;
        p("ȡ������1,k=3:" + Integer.toBinaryString(flag));
        flag = 0xF;
        flag = flag & (flag ^ (flag - 1));
        p("ȡ��һ��1�ұ�,k=3:" + Integer.toBinaryString(flag));
    }
    /**
     * ���Թ���,��ӡ
     * @param obj obj
     */
    public static void p(Object obj) {
        System.out.println(obj);
    }
    public void doPrintStr4Binary(String str) {
        StringBuffer sbuf = new StringBuffer();
        byte[] byteArr = str.getBytes();
        for (int i : byteArr) {
            sbuf.append(Integer.toBinaryString(i)).append(" ");
        }
        System.out.println(sbuf.toString());
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        ByteTest btInst = new ByteTest();
//        btInst.doShiftTest(new int[]{1024, 65536, 65535});
//        btInst.doPrintByte();
//        btInst.doDomainTestMain();
//        btInst.doTest();
        btInst.doPrintStr4Binary("HelloWorld");
    }
}

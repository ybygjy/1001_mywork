package org.ybygjy.basic.bit;

import java.util.Random;

/**
 * �����ѧϰ_����
 * @author WangYanCheng
 * @version 2014-6-30
 */
public class RandomNumberTest {
    /**
     * ͨ��ģ��ȡ��������������
     * @param min
     * @param max
     * @return
     */
    public int nextIntF(int min, int max) {
        Random random = new Random();
        int tmp = Math.abs(random.nextInt());
        tmp = tmp % (max - min + 1) + min;
        return tmp;
    }
    /**
     * ͨ����������λ
     * @return
     */
    public int nextIntS() {
        Random random = new Random();
        int x = random.nextInt(899999);
        return x + 100000;
    }
    /**
     * ͨ��������˷�������ʵ�ֲ�λ
     * @return
     */
    public int nextIntT() {
        int n = 0;
        while (n < 100) {
            n = (int) (Math.random() * 1000000);
        }
        return n;
    }
    /**
     * С���Ŵ��������Ŵ�ȡ��
     * @return rtnC
     */
    public int nextIntFor() {
    	double tmpV = Math.random();
    	tmpV = tmpV * 9;
    	System.out.println(tmpV + '\t');
    	tmpV = tmpV + 1;
    	System.out.println(tmpV + '\t');
    	tmpV = tmpV * 1000000;
    	System.out.println(tmpV + '\t');
    	int rtnV = (int) tmpV;
    	System.out.println(rtnV + '\t');
        return rtnV;
    }
    /**
     * ȡ����λ�������(����2~8λ)
     * @param radix
     * @return rtnValue
     */
    public int getRandomValue(int radix) {
        return ((int) ((Math.random() + 1) * (radix < 2 ? 100 : radix > 9 ? 100 : Math.pow(10D, radix))));
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        RandomNumberTest rntInst = new RandomNumberTest();
        for (int i = 0; i < 100; i++) {
//            System.out.println(rntInst.nextIntF(100, 200));
//            System.out.println(rntInst.nextIntS());
//            System.out.println(rntInst.nextIntT());
            System.out.println(rntInst.nextIntFor());
//            System.out.println(rntInst.nextIntFor());
//            Long batchNo = Long.parseLong(String.valueOf(rntInst.getRandomValue(8)) + "" + String.valueOf(rntInst.getRandomValue(8)));
//            System.out.println(batchNo);
        }
    }
}

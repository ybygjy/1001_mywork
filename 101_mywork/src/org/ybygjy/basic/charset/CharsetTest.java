package org.ybygjy.basic.charset;


/**
 * Charsetѧϰ
 * @author WangYanCheng
 * @version 2014-5-20
 */
public class CharsetTest {
    /**
     * �����Ƹ�ʽ��ӡASCII�ַ�������
     */
    public void doPrintAscii() {
        int count = 1;
        for (int i = 0x00; i <= 0x7F; i++) {
            System.out.print(doFillInStr(Integer.toBinaryString(i)));
            System.out.print(' ');
            if (count ++ % 32 == 0) {
                System.out.println();
            }
        }
    }
    /**
     * ����,�����ַ�����֤�ַ���,����8���ַ���߲���,�������ַ����ַ�����8�������
     * @param str Դ�ַ���
     * @return str �������ַ���
     */
    public static String doFillInStr(String str) {
        int len = str.length();
        if (len >= 8) {
            return str;
        }
        int spaceCount = Math.abs(8 - len);
        StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < spaceCount; i++) {
            sbuf.append("0");
        }
        return sbuf.toString().concat(str);
    }
    /**
     * �������
     * @param args
     */
    public static void main(String[] args) {
        CharsetTest charsetTest = new CharsetTest();
        charsetTest.doPrintAscii();
    }
}

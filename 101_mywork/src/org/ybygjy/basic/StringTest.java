package org.ybygjy.basic;

/**
 * <h3>�ַ�����ز���</h3>
 * @author WangYanCheng
 * @version 2011-2-28
 */
public class StringTest {
    /**
     * ���Ը����ַ����Ƿ����<strong>�ݹ鷽ʽ</strong>���磺<br>
     * <code>String str = "abcdcba</code>
     * @param str Դ�ַ���
     * @return true/false
     */
    public boolean isLoopStr(String str) {
        char[] charStr = str.toCharArray();
        return innerIsLoopStr(charStr, 0, charStr.length - 1);
    }

    /**
     * ���ľ�����֤ <h4>ȷ���߽�</h4>
     * <p>
     * 1���ַ��鳤��Ϊ1��
     * <p>
     * <p>
     * 2���ַ��鳤��Ϊ2��
     * </p>
     * <p>
     * 3���ַ�����β��ͬ��
     * </p>
     * @param charStr Դ�ַ���
     * @param start ��ʼ���λ
     * @param end �������λ
     * @return true/false
     */
    private boolean innerIsLoopStr(char[] charStr, int start, int end) {
        if (end == start || (end - start) == 1) {
            return true;
        }
        return charStr[start] == charStr[end] ? innerIsLoopStr(charStr, ++start, --end) : false;
    }
    /**
     * ����3λ�Զ�����
     */
    public static void testFillupZero() {
        int value =(int)(Math.random() * 1000);
        System.out.println("ԭֵ:".concat(String.valueOf(value)) + ",ת��ֵ" + String.format("-%03d", value));
    }
    
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
//        String str = "abcdcba";
//        System.out.println(new StringTest().isLoopStr(str));
        for (int i = 0; i < 100; i++) {
            StringTest.testFillupZero();
        }
    }
}

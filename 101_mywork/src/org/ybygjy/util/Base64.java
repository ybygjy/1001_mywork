package org.ybygjy.util;

/**
 * Base64����
 * <p>1�����ַ���ת�����ֽ�����</p>
 * <p>2��ÿ3���ֽ�Ϊһ��</p>
 * <p>3��ÿ�����λ������3�鹹���4��</p>
 * <p>4��4���е��ֽ�ת��10����</p>
 * <p>5��ȥBase64�������Ӧ���ַ�</p>
 * @author WangYanCheng
 * @version 2013-5-6
 */
public class Base64 {
    private static String baseStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    public static String encode(String plainText) {
        String encoded = "";
        int paddingCount = plainText.length() % 3;
        String paddingStr = "";
        if (paddingCount > 0) {
            for (; paddingCount < 3; paddingCount++) {
                paddingStr += "=";
                plainText += "\0";
            }
        }
        for (paddingCount = 0; paddingCount < plainText.length(); paddingCount += 3) {
            if (paddingCount > 0 && (paddingCount / 3 * 4) % 76 == 0) {
                encoded += "\r\n";
            }
            int n = (plainText.charAt(paddingCount) << 16) + (plainText.charAt(paddingCount + 1) << 8) + (plainText.charAt(paddingCount + 2));
            int n1 = (n >> 18) & 63, n2 = (n >> 12) & 63, n3 = (n >> 6) & 63, n4 = n & 63;
            encoded += "" + baseStr.charAt(n1) + baseStr.charAt(n2) + baseStr.charAt(n3) + baseStr.charAt(n4);
        }
        return encoded.substring(0, encoded.length() - paddingStr.length()) + paddingStr;
    }
    public static void main(String[] args) {
        String str = Base64.encode("AB");
        System.out.println(str);
    }
}

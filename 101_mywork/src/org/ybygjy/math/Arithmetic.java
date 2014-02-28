package org.ybygjy.math;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ���������㷨
 * @author WangYanCheng
 * @version 2011-3-8
 */
public class Arithmetic {
    /**
     * �������
     * @param args �����б�
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tmpStr = reader.readLine();
        if (null == tmpStr) {
            return;
        }
        String[] strArr = tmpStr.split(",");
        int a = Integer.parseInt(strArr[0]), b = Integer.parseInt(strArr[1]);
        Arithmetic arithObj = new Arithmetic();
        System.out.printf("%d��%d�����Լ��Ϊ:%d", a, b, arithObj.gcd(b, a));
    }

    /**
     * շת�����(�����Լ��)
     * @param a a
     * @param b b
     * @return value
     */
    public int gcd(int a, int b) {
        if (a < b) {
            a = a ^ b;
            b = a ^ b;
            a = a ^ b;
        }
        return b == 0 ? a : gcd(b, a % b);
    }
}

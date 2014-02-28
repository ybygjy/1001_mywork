package org.ybygjy.basic.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ��װLDAP��ȫ������ز���
 * @author WangYanCheng
 * @version 2011-5-20
 */
public class SecurityUtils {
    /** Base64ӳ��� */
    private static final char emap[] = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // A-H;0-7
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // I-P; 8 -15
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // Q-X; 16-23
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // YZ, a-f; 24-31
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // g-n; 32-39
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // o-v; 40-47
            'w', 'x', 'y', 'z', '0', '1', '2', '3', // w-z, 0-3; 48-55
            '4', '5', '6', '7', '8', '9', '+', '/'}; // 4-9, + /; 56-63

    /**
     * ���ַ���ת��ΪMD5�ֽ���
     * @param srcStr Դ�ַ���
     * @return rtnByte MD5�ֽ���
     */
    public static final byte[] encodeMD5(String srcStr) {
        MessageDigest md = null;
        byte[] rtnByte = null;
        try {
            md = MessageDigest.getInstance("MD5");
            rtnByte = md.digest(srcStr.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rtnByte;
    }
    
    /**
     * ���ֽ�����ת��Ϊʮ�������ַ�����ʽ
     * @param byteArr �ֽ�����
     * @return rtnStr ʮ�����Ʊ�ʾ���ַ���
     */
    public static String toHexStr(byte[] byteArr) {
        StringBuilder rtnStr = new StringBuilder();
        String tmpStr = null;
        for (int i = 0; i < byteArr.length; i++) {
            tmpStr = (Integer.toHexString(byteArr[i] & 0xFF));
            rtnStr.append(tmpStr.length() == 2 ? tmpStr : "0".concat(tmpStr));
        }
        return rtnStr.toString();
    }

    /**
     * ���ֽ�����ת��ΪBase64����
     * @param inputBytes �ֽ�����
     * @return rtnStr Base64������ɺ���ַ���
     * @see com.novell.ldap.util.Base64
     */
    public static final String encodeBase64(byte[] inputBytes) {
        int i, j, k;
        int t, t1, t2;
        int ntb;
        boolean onePadding = false, twoPaddings = false;
        char[] encodedChars;
        int len = inputBytes.length;
        if (len == 0) {
            return new String("");
        }
        ntb = len%3 == 0 ? (len / 3) : ((len / 3) + 1);
        if ((len % 3) == 1) {
            twoPaddings = true;
        } else if ((len % 3) == 2) {
            onePadding = true;
        }
        encodedChars = new char[ntb * 4];
        for (i = 0, j = 0, k = 1; i < len; i += 3, j += 4, k++) {
            t = 0x00ff & inputBytes[i];
            encodedChars[j] = emap[t >> 2];
            if ((k == ntb) && twoPaddings) {
                encodedChars[j + 1] = emap[(t & 0x03) << 4];
                encodedChars[j + 2] = '=';
                encodedChars[j + 3] = '=';
                break;
            } else {
                t1 = 0x00ff & inputBytes[i + 1];
                encodedChars[j + 1] = emap[((t & 0x03) << 4) + ((t1 & 0xf0) >> 4)];
            }
            if ((k == ntb) && onePadding) {
                encodedChars[j + 2] = emap[(t1 & 0x0f) << 2];
                encodedChars[j + 3] = '=';
                break;
            } else {
                t2 = 0x00ff & inputBytes[i + 2];
                encodedChars[j + 2] = (emap[(t1 & 0x0f) << 2 | (t2 & 0xc0) >> 6]);
            }
            encodedChars[j + 3] = (emap[(t2 & 0x3f)]);
        }
        return new String(encodedChars);
    }

    /**
     * �������
     * @param args
     */
    public static void main(String[] args) {
        String passwd = "123456";
        byte[] md5Bytes = SecurityUtils.encodeMD5(passwd);
        String base64Str = encodeBase64(md5Bytes);
        String hexStr = "{MD5}".concat(base64Str);
        System.out.println(md5Bytes.length + "\n" + SecurityUtils.toHexStr(md5Bytes) + "\nBase64��" + base64Str );
    }
}

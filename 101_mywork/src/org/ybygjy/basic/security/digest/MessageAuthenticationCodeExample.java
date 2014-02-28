package org.ybygjy.basic.security.digest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * ��Ϣ��֤����֤
 * <p>�����Կ��������ϢժҪ���ɹ��̵�һ���֣��򽫸��㷨��Ϊ��Ϣ��֤��</p>
 * <p>������Ϣ��֤���㷨��֧�֣�HMAC/SHA-1��HMAC/MD5</p>
 * @author WangYanCheng
 * @version 2011-7-4
 */
public class MessageAuthenticationCodeExample {
    /**
     * HMAC/MD5�㷨������Ϣ��֤��
     * @param srcPlain ������Ϣ��
     * @throws UnsupportedEncodingException δ��֧�ֵ��ַ���
     * @throws NoSuchAlgorithmException δ��֧�ֵ��㷨
     * @throws InvalidKeyException �Ƿ���keyֵ����
     */
    public void mac4MD5(String srcPlain) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");
        //�㷨֧����Ϣ
        String sysInfo = keyGenerator.getProvider().getInfo();
System.out.println("�㷨֧����Ϣ��".concat(sysInfo));
        //����HmacMD5����Key
        SecretKey secretMD5Key = keyGenerator.generateKey();
System.out.println("HmacMD5�����Key��".concat(secretMD5Key.toString()));
        //������Ϣ��֤��������
        Mac mac = Mac.getInstance("HmacMD5");
        byte[] srcPlainByte = srcPlain.getBytes("UTF-8");
        //��ʼ����������Ϣ��֤��
        mac.init(secretMD5Key);
        mac.update(srcPlainByte);
        //�㷨֧����Ϣ
        sysInfo = mac.getProvider().getInfo();
System.out.println("�㷨֧����Ϣ��".concat(sysInfo));
        //��ȡ���
        String resultStr = new String(mac.doFinal(), "utf-8");
        System.out.println("ԭʼ��Ϣ��".concat(srcPlain).concat("\n��Ϣ��֤�룺").concat(resultStr));
    }
    /**
     * �������
     * @param args �����б�
     * @throws InvalidKeyException �Ƿ���Keyֵ����
     * @throws UnsupportedEncodingException δ��֧�ֵ��ַ���
     * @throws NoSuchAlgorithmException δ��֧�ֵ��㷨
     */
    public static void main(String[] args) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageAuthenticationCodeExample maceInst = new MessageAuthenticationCodeExample();
        String srcPlain = "I'm a Software Engineer.";
        maceInst.mac4MD5(srcPlain);
    }
}

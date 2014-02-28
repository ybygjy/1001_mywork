package org.ybygjy.basic.security.digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ������֤��ϢժҪ����ժҪ�Ĵ������������ȡ
 * <p>���ȴ�����ϢժҪ������ض���MessageDigest������Ǽ�����ϢժҪҲ���Ǵ������ڼ�������ģ�Ȼ���ȡ��������</p>
 * @author WangYanCheng
 * @version 2011-7-4
 */
public class MessageDigestExample {
    /**
     * ����MD5�㷨������ϢժҪ
     * @param planText ԭʼ��Ϣ
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public void messageDigest4MD5(byte[] planText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //��ϢժҪģ��֧����Ϣ
        String providerInfo = messageDigest.getProvider().getInfo();
        System.out.println("ժҪģ��֧����Ϣ��".concat(providerInfo));
        //����ժҪ
        messageDigest.update(planText);
        //��ȡժҪ
        providerInfo = new String(messageDigest.digest(), "UTF-8");
        System.out.println("ԭʼ��Ϣ��".concat(new String(planText, "UTF-8")).concat("\n��ϢժҪ��").concat(providerInfo));
    }
    /**
     * �������
     * @param args �����б�
     * @throws UnsupportedEncodingException δ֧�ֵ��ַ���
     * @throws NoSuchAlgorithmException δ��֧�ֵ��㷨
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigestExample mdeInst = new MessageDigestExample();
        String srcPlain = "I'm a Software Engineer.";
        mdeInst.messageDigest4MD5(srcPlain.getBytes());
    }
}

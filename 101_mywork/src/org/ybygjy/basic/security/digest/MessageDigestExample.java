package org.ybygjy.basic.security.digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ybygjy.util.Base64;

import com.sun.jndi.toolkit.chars.BASE64Encoder;
import com.sun.mail.util.BASE64EncoderStream;

/**
 * ������֤��ϢժҪ����ժҪ�Ĵ������������ȡ
 * <p>���ȴ�����ϢժҪ������ض���MessageDigest������Ǽ�����ϢժҪҲ���Ǵ������ڼ�������ģ�Ȼ���ȡ��������</p>
 * @author WangYanCheng
 * @version 2011-7-4
 */
public class MessageDigestExample {
	/**ʮ������*/
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
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
//        System.out.println("ժҪģ��֧����Ϣ��".concat(providerInfo));
        //����ժҪ
        messageDigest.update(planText);
        //��ȡժҪ
//        providerInfo = new String(messageDigest.digest(), "UTF-8");
        System.out.println(new String(encodeHex(messageDigest.digest())));
//        System.out.println("ԭʼ��Ϣ��".concat(new String(planText, "UTF-8")).concat("\n��ϢժҪ��").concat(providerInfo));
    }

    /**
     * ������תʮ�������ַ���
     * @param data ��������
     * @return rtnCharArr
     */
    private String encodeHex(byte[] data) {
    	StringBuffer sbuf = new StringBuffer();
    	for (byte byteValue : data) {
    		sbuf.append(Integer.toHexString((0xF0 & byteValue)>>> 4));
    		sbuf.append(Integer.toHexString(0x0F & byteValue));
    	}
    	return sbuf.toString();
    }
    /**
     * �������
     * @param args �����б�
     * @throws UnsupportedEncodingException δ֧�ֵ��ַ���
     * @throws NoSuchAlgorithmException δ��֧�ֵ��㷨
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigestExample mdeInst = new MessageDigestExample();
        String srcPlain = "0f13970157d9c0d026126f375bf066bbaccountNo=6237000013790830168&accountType=1&applyNo=151609367235604021&bankCode=CCB&busiTypeId=HIGO&curId=CNY&customerName=�й���������&customerNo=MLS_D_00258228&customerType=0&merchantId=MLS_I_00000008&openAccName=�й���������&version=20131111";
//        String srcPlain = "ea559fdaeb5dbb7668436f87df16e38eaccountNo=6237000013790830168&accountType=1&applyNo=151609367235604021&bankCode=CCB&busiTypeId=HIGO&curId=CNY&customerName=�й���������&customerNo=MLS_D_00258228&customerType=0&merchantId=MLS_I_00000008&openAccName=�й���������&version=20131111";
        mdeInst.messageDigest4MD5(srcPlain.getBytes("UTF-8"));
    }
}

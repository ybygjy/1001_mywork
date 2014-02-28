package org.ybygjy.pattern.proxy.virtual.simple;



/**
 * Virtual Proxy
 * <p>ʹ��ProxySubject������Client��RealSubject</p>
 * <p><b>ProxySubject</b>����׼��RealSubjectʵ������RealSubject�ǳ���ʱʱ��ǳ����á�</p>
 * @author WangYanCheng
 * @version 2009-12-22
 */
public class Client {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        Subject subjectInst = new ProxySubClass();
        subjectInst.doRequest();
    }
}

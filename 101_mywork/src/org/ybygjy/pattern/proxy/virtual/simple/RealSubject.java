package org.ybygjy.pattern.proxy.virtual.simple;
/**
 * ��ʵ����ʵ��
 * @author WangYanCheng
 * @version 2009-12-22
 */
public class RealSubject implements Subject {
    /**
     * {@inheritDoc}
     */
    public void doRequest() {
        System.out.println("doRequest--->RealSubject");
    }
}

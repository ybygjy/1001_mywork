package org.ybygjy.pattern.factory.simplefac;

/**
 * �����Ʒ
 * @author WangYanCheng
 * @version 2009-12-2
 */
public abstract class AbstractProduct {
    /**
     * ��ʾ��Ʒ��Ϣ
     * @return productInfo
     */
    protected abstract String showInfo();
    /**
     * ��ƽ׶�
     */
    protected abstract void desinging();
    /**
     * �����׶�
     */
    protected abstract void manufacturing();
    /**
     * ����
     */
    protected abstract void sale();
}

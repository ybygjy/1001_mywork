package org.ybygjy.pattern.visitor;

/**
 * ���屻������
 * <p>��ǰ�����Ǽ�����豸�ĳ���
 * @author WangYanCheng
 * @version 2013-2-6
 */
public interface Equipment {
    public void accept(Visitor visitor);
    public double price();
}

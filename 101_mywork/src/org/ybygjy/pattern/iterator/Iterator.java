package org.ybygjy.pattern.iterator;

/**
 * ���������Ϊ�ӿ�
 * @author WangYanCheng
 * @version 2012-11-26
 */
public interface Iterator {
    public void first();
    public void next();
    public boolean isDone();
    public Object currentItem();
}

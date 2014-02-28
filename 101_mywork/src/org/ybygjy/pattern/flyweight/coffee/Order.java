package org.ybygjy.pattern.flyweight.coffee;
/**
 * ����
 * @author WangYanCheng
 * @version 2010-11-22
 */
public abstract class Order {
    /**
     * ��coffee��������
     * @param table {@link Table}
     */
    public abstract void serve(Table table);
    /**
     * ȡcoffee����
     * @return coffeeName
     */
    public abstract String getFlavor();
}

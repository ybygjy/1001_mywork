package org.ybygjy.pattern.factory.abstractfac.impl;

import org.ybygjy.pattern.factory.abstractfac.AbstractProductB;

/**
 * �����Ʒʵ��
 * @author WangYanCheng
 * @version 2010-10-25
 */
public class ProductB2 implements AbstractProductB {
    /**
     * {@inheritDoc}
     */
    public String getDesc() {
        return this.toString();
    }
}

package org.ybygjy.pattern.factory.abstractfac.impl;

import org.ybygjy.pattern.factory.abstractfac.AbstractProductA;

/**
 * �����Ʒ�淶ʵ��
 * @author WangYanCheng
 * @version 2010-10-25
 */
public class ProductA2 implements AbstractProductA {
    /**
     * {@inheritDoc}
     */
    public String getDesc() {
        return this.toString();
    }

}

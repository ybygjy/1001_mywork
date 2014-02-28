package org.ybygjy.pattern.factory.abstractfac;

import org.ybygjy.pattern.factory.abstractfac.impl.ConcreteFactoryA;
import org.ybygjy.pattern.factory.abstractfac.impl.ConcreteFactoryB;

/**
 * ���󹤳�ģʽ�������
 * @author WangYanCheng
 * @version 2010-10-25
 */
public class FactoryTestDrive {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        AbstractFactory af = new ConcreteFactoryA();
        af.doCreateProductA().toString();
        af.doCreateProductB().toString();
        af = new ConcreteFactoryB();
        af.doCreateProductA().toString();
        af.doCreateProductB().toString();
    }
}

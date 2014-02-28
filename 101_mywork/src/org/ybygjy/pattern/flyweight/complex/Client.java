package org.ybygjy.pattern.flyweight.complex;
/**
 * ����Ը�����Ԫģʽ�Ĳ���
 * @author WangYanCheng
 * @version 2010-11-22
 */
public class Client {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        FlyweightFactory ffInst = FlyweightFactory.getInstance();
        AbstractFlyweight afInst = ffInst.flyweight('A');
        afInst.doSomething("Instrinstic State A");
        ffInst.flyweight('A').doSomething("Instrinstic State A");
        afInst = ffInst.flyweight("ABCDEFABCDEF");
        afInst.doSomething("Composite Flyweight");
    }
}

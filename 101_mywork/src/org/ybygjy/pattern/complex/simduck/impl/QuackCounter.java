package org.ybygjy.pattern.complex.simduck.impl;

import org.ybygjy.pattern.complex.simduck.Observer;
import org.ybygjy.pattern.complex.simduck.Quackable;

/**
 * ����Duck�������ܣ�װ��
 * @author WangYanCheng
 * @version 2011-1-24
 */
public class QuackCounter implements Quackable {
    private Quackable quackInst;
    private static int numberOfQuack;
    public QuackCounter(Quackable quackInst) {
        this.quackInst = quackInst;
    }

    public void quack() {
        quackInst.quack();
        numberOfQuack++;
    }

    public static int getNumberOfQuack() {
        return numberOfQuack;
    }

    public void notifyObservers() {
        quackInst.notifyObservers();
    }

    public void registerObserver(Observer observer) {
        quackInst.registerObserver(observer);
    }
}

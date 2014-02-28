package org.ybygjy.pattern.state.impl;

import org.ybygjy.pattern.state.GumballMachine;
import org.ybygjy.pattern.state.State;

/**
 * ����
 * @author WangYanCheng
 * @version 2010-11-13
 */
public class SoldOutState implements State {
    /**Context instance*/
    private GumballMachine gumballMachine;
    /**
     * Constructor
     * @param gumballMachine {@link GumballMachine}
     */
    public SoldOutState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    /**
     * {@inheritDoc}
     */
    public void dispense() {
        System.out.println("�Բ����ǹ��Ѿ�����");
    }

    /**
     * {@inheritDoc}
     */
    public void enjectQuarter() {
        System.out.println("����û��Ͷ��Ǯ��.");
    }

    /**
     * {@inheritDoc}
     */
    public void insertQuarter() {
        System.out.println("�Բ����ǹ��Ѿ�����");
    }

    /**
     * {@inheritDoc}
     */
    public void turnCrank() {
        System.out.println("�Բ����ǹ��Ѿ�����");
    }
}

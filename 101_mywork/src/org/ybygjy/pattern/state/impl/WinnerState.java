package org.ybygjy.pattern.state.impl;

import org.ybygjy.pattern.state.GumballMachine;
import org.ybygjy.pattern.state.State;

/**
 * ����״̬
 * @author WangYanCheng
 * @version 2010-11-13
 */
public class WinnerState implements State {
    /** �������� */
    private GumballMachine gumballMachine;

    /**
     * Constructor
     * @param gumballMachine {@link GumballMachine}
     */
    public WinnerState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    /**
     * {@inheritDoc}
     */
    public void dispense() {
        System.out.println("You're a winner! You get two gumballs for your quarter.");
        gumballMachine.releaseBall();
        if (gumballMachine.getCount() == 0) {
            gumballMachine.setCurrState(gumballMachine.getSoldOutState());
        } else {
            gumballMachine.releaseBall();
            if (gumballMachine.getCount() > 0) {
                gumballMachine.setCurrState(gumballMachine.getNoQuarterState());
            } else {
                System.out.println("Oops, out of gumballs.");
                gumballMachine.setCurrState(gumballMachine.getSoldOutState());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void enjectQuarter() {
        System.out.println("�ǹ��Ѿ����ţ������˱�.");
    }

    /**
     * {@inheritDoc}
     */
    public void insertQuarter() {
        System.out.println("���Եȡ�");
    }

    /**
     * {@inheritDoc}
     */
    public void turnCrank() {
        System.out.println("����Ҫ�ظ�ת���ֱ�.");
    }

}

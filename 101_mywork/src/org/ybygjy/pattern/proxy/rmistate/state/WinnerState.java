package org.ybygjy.pattern.proxy.rmistate.state;

import java.rmi.RemoteException;

import org.ybygjy.pattern.proxy.rmistate.GumballMachine;
import org.ybygjy.pattern.proxy.rmistate.IState;

/**
 * WinnerState
 * @author WangYanCheng
 * @version 2010-12-24
 */
public class WinnerState implements IState {
    /**
     * serial
     */
    private static final long serialVersionUID = -2788659924721859279L;
    /** gumballInst */
    private transient GumballMachine gumballInst;

    /**
     * Constructor
     * @param gumballInst gumballInst
     */
    public WinnerState(GumballMachine gumballInst) {
        this.gumballInst = gumballInst;
    }

    /**
     * {@inheritDoc}
     */
    public void dispense() {
        System.out.println("You are a winner! You get two gumballs for your quarter.");
        gumballInst.releaseBall();
        try {
            if (gumballInst.getCount() == 0) {
                gumballInst.setCurrState(gumballInst.getSoldOutState());
            } else {
                gumballInst.releaseBall();
                if (gumballInst.getCount() > 0) {
                    gumballInst.setCurrState(gumballInst.getNoQuarterState());
                } else {
                    gumballInst.setCurrState(gumballInst.getSoldOutState());
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void ejectQuarter() {
        System.out.println("�Ѿ������ǹ��������˱ҡ�");
    }

    /**
     * {@inheritDoc}
     */
    public void insertQuarter() {
        System.out.println("���ڷ����ǹ������Ժ򡣡�");
    }

    /**
     * {@inheritDoc}
     */
    public void turnCrank() {
        System.out.println("�ظ�ת���ֱ�Ҳ�����ö���ǹ�����");
    }

}

package org.ybygjy.basic.thinking.thread.testframework.consume;

import org.ybygjy.basic.thinking.thread.testframework.Invariant;
import org.ybygjy.basic.thinking.thread.testframework.InvariantWatcher;
import org.ybygjy.basic.thinking.thread.testframework.state.InvariantFailure;
import org.ybygjy.basic.thinking.thread.testframework.state.InvariantOK;
import org.ybygjy.basic.thinking.thread.testframework.state.InvariantState;

/**
 * AlwaysEven
 * @author WangYanCheng
 * @version 2010-9-30
 */
public class EvenGenerator implements Invariant {
    /** inner variable */
    private int i;

    /**
     * �ɳ��԰�synchronized�ؼ���ȥ��
     * self increment variable
     */
    public synchronized void next() {
        i++;
        i++;
    }

    /**
     * getValue
     * @return i i
     */
    public synchronized int getValue() {
        return i;
    }

    /**
     * {@inheritDoc}
     */
    public InvariantState invariant() {
        int val = getValue();
        if (val % 2 == 0) {
            return new InvariantOK();
        } else {
            return new InvariantFailure(val);
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        EvenGenerator egInst = new EvenGenerator();
        new InvariantWatcher(egInst, 10000);
        while (true) {
            egInst.next();
        }
    }
}

package org.ybygjy.jcip.chap3;

/**
 * ��������ʵ��
 * @author WangYanCheng
 * @version 2014-7-17
 */
public class VolatileVO {
    private volatile int step;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}

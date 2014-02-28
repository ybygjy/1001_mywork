package org.ybygjy.basic.thinking.thread;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * �����synchronized���������������иı�Ϊ��synchronized����
 * @author WangYanCheng
 * @version 2012-11-12
 */
public class SynExtends {
    public static void main(String[] args) {
        ParentClass pcInst = new SubClass();//new ParentClass();
        Set<Integer> resultSets =Collections.synchronizedSet(new TreeSet<Integer>());
        createThread(pcInst, resultSets);
        createThread(pcInst, resultSets);
        createThread(pcInst, resultSets);
        createThread(pcInst, resultSets);
    }
    public static void createThread(final ParentClass pcInst, final Set<Integer> valueSet) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    Integer tmpI = pcInst.doWork();
                    if (!valueSet.add(tmpI)) {
                        throw new IllegalStateException("����״̬����ȷ�������ظ���".concat(tmpI.toString()));
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
class ParentClass {
    private volatile int index;
    public synchronized Integer doWork() {
        return index++;
    }
}
class SubClass extends ParentClass {
    private int index;
    @Override
    public Integer doWork() {
        return index++;
    }
}
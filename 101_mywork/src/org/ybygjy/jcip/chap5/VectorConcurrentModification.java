package org.ybygjy.jcip.chap5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;


/**
 * JCIP#5002_���������е��������Ĳ�������
 * <p>�����ڽ��е����ڼ������߳��޸�������</p>
 * 1������취:
 * <p>1.1�����������ĵ������޸ĳ���Ϊһ�鸴�ϲ������ڿͻ��˶Բ������м�����</p>
 * <p>1.2������CopyOnWriter��ʽ(��¡)���ڸ����Ͻ��е�����</p>
 * <p>1.3��ע����ʽ�������⣬����������toString()��hashCode()���෽��Ϊ���������ݽ��е�����</p>
 * <p>1.4��ͨ�����ò����������������</p>
 * 2������:
 * <p>2.1��ͬ�����������ж�����״̬�ķ��ʶ����л�����ʵ�����ǵ��̰߳�ȫ�ԡ�</p>
 * <p>2.2��ͬ�������������������ؽ����˲����ԣ����߳̾�����������������ؼ��͡�</p>
 * @author WangYanCheng
 * @version 2014-7-24
 */
public class VectorConcurrentModification {
    public static void main(String[] args) {
        List<String> dataList = Collections.synchronizedList(new ArrayList<String>());
        for (int i = 0; i < 10; i++) {
            dataList.add("List#" + i);
        }
        new IteratorThread("IteratorThread", dataList).start();
        new UpdateThread("UpdateThread", dataList).start();
    }
    /**
     * ������������е���
     * @author WangYanCheng
     * @version 2014��7��25��
     */
    static class IteratorThread extends Thread {
        private Iterable<String> iterableInstance;
        public IteratorThread(String threadName, Iterable<String> iterableInstance) {
            super(threadName);
            this.iterableInstance = iterableInstance;
        }
        public void run() {
            while (true) {
                try {
                    Iterator<String> iterator = this.iterableInstance.iterator();
                    while (iterator.hasNext()) {
                        System.out.println(getName() + ":" + iterator.next());
                    }
                } catch (ConcurrentModificationException cmeInst) {
                    cmeInst.printStackTrace();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupt();
                }
            }
        }
    }
    /**
     * ����
     * @author WangYanCheng
     * @version 2014��7��25��
     */
    static class UpdateThread extends Thread {
        private List<String> dataList;
        public UpdateThread(String threadName, List<String> dataList) {
            super(threadName);
            this.dataList = dataList;
        }
        public void run() {
            while (true) {
                for (int i = 0; i < 10; i++) {
                    String itemStr = getName() + "_" + i;
                    dataList.add(itemStr);
                    System.out.println("Added Item:" + itemStr);
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    interrupt();
                    e.printStackTrace();
                }
            }
        }
    }
}

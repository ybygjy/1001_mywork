package org.ybygjy.jcip.chap5;

import java.util.Vector;


/**
 * JCIP#5001_ͬ�������ĸ��ϲ�����������⣬��ͬ����Ҫ����Ŀͻ��˴������
 * <p>1���ڿͻ��˲�����������¶���������и��ϲ���ʱ���������</p>
 * <p>2�����������ԭ���ǿͻ����ڶ��̰߳�ȫ�������и��ϲ�������ԭ�ӵģ����뵱get/setͬʱ�����������������Ȼ�����ڲ���֤��������̰߳�ȫ�ԣ������ⲿ������ȴ�õ��˷�Ԥ�ڵĽ����</p>
 * ���
 * <p>1���ͻ��˼���</p>
 * <p>2��ע��ͻ���������������ʹ�õ�����ͬ������û���κ����壬���ҰѴ������صĸ��</p>
 * @author WangYanCheng
 * @version 2014-7-24
 */
public class VectorComplexOperation {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        Vector<String> vector = new Vector<String>();
        for (int i = 0; i < 10; i++) {
            vector.add("VectorItem_" + (i));
        }
        new GetThread("GetThread", vector).start();
        new DeleteThread("DeleteThread", vector).start();
    }
    static class GetThread extends Thread {
        private Vector<String> vector;
        public GetThread(String threadName, Vector<String> vector) {
            super(threadName);
            this.vector = vector;
        }
        public void run() {
            while (true) {
                System.out.println(getName() + ":" + (vector.size() - 1) + "#" + vector.get(vector.size() - 1));
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    interrupt();
                    e.printStackTrace();
                }
                
            }
        }
    }
    static class DeleteThread extends Thread {
        private Vector<String> vector;
        public DeleteThread(String threadName, Vector<String> vector) {
            super(threadName);
            this.vector = vector;
        }
        public void run() {
            while (true) {
                int lastIndex = this.vector.size() - 1;
                System.out.println(getName() + ":" + lastIndex + "#" + this.vector.remove(lastIndex));
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

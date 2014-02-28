package org.ybygjy.basic.thinking;

import java.util.ArrayList;
import java.util.List;

/**
 * �������˳��
 * @author WangYanCheng
 * @version 2010-3-20
 * <br>
 * <i>����:</i>�߳��ڹ��������е�����˳��ǳ��ؼ�����Ϊ�߳��Ѿ�������ʵ���������������
 */
public class ObjectExecuteSeq4Thread {
    /**
     * execute portal
     * @param args args
     */
    public static void main(String[] args) {
        new ObjectExecuteSeq4Thread.InnerTestClass(new ArrayList());
    }

    /**
     * InnerTestClass
     * @author WangYanCheng
     * @version 2010-3-20
     */
    static class InnerTestClass implements Runnable {
        List<String> filePaths = null;
        private boolean runFlag = true;
        /**
         * Constructor
         * @param filePaths filePaths
         */
        public InnerTestClass(List<String> filePaths) {
            if (null != filePaths) {
                new Thread(this).start();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            this.filePaths = filePaths;
            System.out.println("Contructor method.");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            this.runFlag = false;
        }

        /**
         * {@inheritDoc}
         */
        public void run() {
            while (runFlag) {
                System.out.println("Invoked Run Method%d" + this.filePaths);
            }
        }
    }
}

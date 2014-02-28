package org.ybygjy.basic.finaltest;
/**
 * final���͹ؼ��ֲ���
 * @author WangYanCheng
 * @version 2010-8-12
 */
public class BasicTest {
    /**
     * getInnerClass
     * @return innerClass innerClass
     */
    public InnerClass getInnerClass() {
        return new InnerClass();
    }
    /**
     * ע�����������final
     * <p>1����ʶ�ú��������޸Ĵ��ݲ���ʵ�����ڴ��ַ
     * <p>2���������޸ĸò����ڲ��������õ��ڴ��ַ
     * @param innerClass
     */
    public void doWork(final InnerClass innerClass) {
        innerClass.setFinalIntValue(5);
        innerClass.setFinalObjValue(new Object());
    }
    /**
     * InnerCompiler
     * @author WangYanCheng
     * @version 2010-8-12
     */
    static final class InnerClass {
        /**countFlag*/
        static transient int countIndex = 0;
        private int finalIntValue;
        private Object finalObjValue;
        /**
         * getCountFlag
         * @return countFlag countFlag
         */
        public static int getCountIndex() {
            countIndex++;
            return countIndex;
        }
        /**
         * @param finalIntValue the finalIntValue to set
         */
        public void setFinalIntValue(int finalIntValue) {
            this.finalIntValue = finalIntValue;
        }
        /**
         * @param finalObjValue the finalObjValue to set
         */
        public void setFinalObjValue(Object finalObjValue) {
            this.finalObjValue = finalObjValue;
        }
        /**
         * @return the finalIntValue
         */
        public int getFinalIntValue() {
            return finalIntValue;
        }
        /**
         * @return the finalObjValue
         */
        public Object getFinalObjValue() {
            return finalObjValue;
        }
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getId() + ":" + InnerClass.getCountIndex());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getId() + ":" + InnerClass.getCountIndex());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

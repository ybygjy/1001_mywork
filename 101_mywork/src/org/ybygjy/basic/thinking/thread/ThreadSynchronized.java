package org.ybygjy.basic.thinking.thread;

/**
 * �̣߳��̲߳������߳�ʹ�õ����߼�ʵ���Ĺ�ϵ
 * <p>����߳�ʹ�õ�ͬһ��ʵ��ʱ������Ҫ��Դͬ���ĳ���</p>
 * <p>����߳�ʹ�õ�ʵ��һһ��Ӧʱ����Ҫ����Դͬ��</p>
 * @author WangYanCheng
 * @version 2013-1-10
 */
public class ThreadSynchronized {
    public static void main(String[] args) {
        new InnerThreadTest(new ClassInstance("Thread_A")).start();
        new InnerThreadTest(new ClassInstance("Thread_B")).start();
    }
}
class InnerThreadTest extends Thread {
    private ClassInstance clazzInstance;
    public InnerThreadTest(ClassInstance ciInst) {
        this.clazzInstance = ciInst;
    }
    public void run() {
        while (true) {
            try {
                Thread.sleep((long)(Math.random()*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clazzInstance.sayHello();
        }
    }
}
class ClassInstance {
    private long counter = 0L;
    private String className;
    public ClassInstance(String className) {
        this.className = className;
    }
    public synchronized void sayHello() {
        System.out.println("Hello>>".concat(className).concat("_").concat(String.valueOf(counter++)).concat(Thread.currentThread().getName()));
    }
}
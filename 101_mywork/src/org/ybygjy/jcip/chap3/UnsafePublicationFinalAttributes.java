package org.ybygjy.jcip.chap3;

/**
 * ��֤���캯��δִ����ɣ������final�������Ѿ��������������ڲ�������»���ֵ�Bug
 * �ο�����
 * <ol>
 * <li><a href="http://docs.oracle.com/javase/specs/jls/se5.0/html/memory.html#65124/memory.html#17.5">Java Language Specification</a>
 * <li><a href="http://stackoverflow.com/questions/3705425/java-reference-escape">StackOverflow</a></li>
 * </ol>
 * ��֤���
 * <p>1��δ�ܸ�������</p>
 * <p>2����������س�ʼ��˳������ϵ���������������������static���������Ըñ����ĳ�ʼʱ��Ӧ���ڹ��캯��֮ǰ</p>
 * @author WangYanCheng
 * @version 2014-7-22
 */
public class UnsafePublicationFinalAttributes {
    public static void main(String[] args) {
        final InnerUnsafe[] leak = new InnerUnsafe[1];
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new InnerUnsafe(leak);
            }
        }).start();
        while (true) {
            if (leak[0] != null) {
                if (leak[0].foo == 42) {
//                    System.out.println("OK");
                } else {
                    System.out.println("Error");
                }
            }
        }
    }
}
class InnerUnsafe {
    public final int foo = 42;
    {
        System.out.println("ʵ���������ʼ����");
    }
    public InnerUnsafe (InnerUnsafe[] upfaInst) {
        System.out.println("���캯��Begin");
        upfaInst[0] = this; //unsafe
        for (int i = 0; i < 20 * 1000000; i++) {
            doSomething();
        }
        System.out.println("���캯��End");
    }
    static void doSomething() {
    }
}

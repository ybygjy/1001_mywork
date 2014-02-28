package org.ybygjy.basic.thinking.innerclass;
/**
 * �ڲ���̳в���
 * <i>���ۼ�ע������:</i>
 * <li>1���̳�Ƕ���ڲ���,ע�⹹���ʼ������</li>
 * <li>2�����಻�����޲ι���</li>
 * <li>3����ʵ����̳е�ĳ������ڲ�����Ҫ�����Ŷ԰��������ⲿ������ã�
 * �������������Ҫ��ʼ��(ע�⴫��WithInner����)</li>
 * @author WangYanCheng
 * @version 2010-6-7
 */
public class InheritInner extends WithInner.Inner {
    /*public InheritInner() {
    }*/
    public InheritInner(WithInner wi) {
        wi.super();
        System.out.println("Hello InheritInnerClass");
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        //new InheritInner(new WithInner());
        WithInner wi = new WithInner();
    }
}
class WithInner {
    public WithInner() {
        System.out.println("Hello WithInnerClass.");
    }
    class Inner {
        Inner() {
            System.out.println("Hello super InnerCompiler.");
        }
    }
}

package org.ybygjy.basic.reflection.test2;

import java.lang.reflect.Method;

/**
 * CSDN���ѹ��ڷ�������
 * @author WangYanCheng
 * @version 2010-8-5
 */
public class ReflectionTest4CSDN {
    /**
     * ȡ������Ǽ̳�Method
     * @param classInst ����ʵ��
     */
    private void refChildClassMethod(Class classInst) {
        Method[] methodArr = classInst.getDeclaredMethods();
        for (Method tmpM : methodArr) {
            System.out.println(tmpM.getName());
        }
    }
    /**
     * �������
     * @param arg �����б�
     */
    public static void main(String[] arg) {
        ReflectionTest4CSDN rt4cInst = new ReflectionTest4CSDN();
        rt4cInst.refChildClassMethod(ClassB.class);
    }
    /**
     * classA
     * @author WangYanCheng
     * @version 2010-8-5
     */
    class ClassA {
        /**
         * sayHello
         */
        protected void sayHello() {
            System.out.println("HelloWorld!I'm classA.");
        }
    }
    /**
     * ClassB
     * @author WangYanCheng
     * @version 2010-8-5
     */
    class ClassB extends ClassA {
        /**
         * sayGoodBye
         */
        protected void sayGoodBye() {
            System.out.println("HelloWorld!I'm classB.");
        }
    }
}

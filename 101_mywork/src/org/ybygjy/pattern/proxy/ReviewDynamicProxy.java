package org.ybygjy.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;

/**
 * ��ϰJava�Դ��Ķ�̬����
 * <p>1�����ô������ܻ������ʵ����</p>
 * <p>2�����ô������ṩ����ʵ������Ϊ�ļ�������չ���</p>
 * @author WangYanCheng
 * @version 2011-2-24
 */
public class ReviewDynamicProxy {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        Object[] elements = new Object[1000];
        for (int i = 0; i < elements.length; i++) {
            InvocationHandler ihInst = new TraceHandler(i);
            elements[i] = Proxy.newProxyInstance(null, new Class[]{Comparable.class}, ihInst);
        }
        int key = new Random().nextInt(elements.length);
        int result = Arrays.binarySearch(elements, key);
        System.out.println(result);
    }
}
/**
 * �ص����մ�����
 * @author WangYanCheng
 * @version 2011-2-24
 */
class TraceHandler implements InvocationHandler {
    /**ԴĿ�����*/
    private Object target;
    /**
     * Constructor
     * @param target Ŀ�����
     */
    public TraceHandler(Object target) {
        this.target = target;
    }
    /**
     * {@inheritDoc}
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args != null) {
            System.out.print(target + ":" + Arrays.toString(args) + "\n");
        }
        return method.invoke(target, args);
    }
}

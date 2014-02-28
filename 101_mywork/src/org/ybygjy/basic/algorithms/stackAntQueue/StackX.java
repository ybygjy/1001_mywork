package org.ybygjy.basic.algorithms.stackAntQueue;

/**
 * ջ
 * <p>����ȳ�(LIFO)</p>
 * @author WangYanCheng
 * @version 2011-8-5
 */
public class StackX {
    private int maxSize;
    private int[] stackArray;
    private int top;

    /**
     * Constructor
     * @param size size
     */
    public StackX(int size) {
        this.maxSize = size;
        this.stackArray = new int[maxSize];
        this.top = -1;
    }

    /**
     * ��ջ
     * @return rtnValue
     */
    public int pop() {
        return this.stackArray[top--];
    }

    /**
     * ��ջ
     * @param i rtnValue
     */
    public void push(int i) {
        this.stackArray[++top] = i;
    }

    /**
     * �ж�ջ��
     * @return true/false
     */
    public boolean isFull() {
        return (top == (this.maxSize - 1));
    }

    /**
     * �ж�ջ��
     * @return true/false
     */
    public boolean isEmpty() {
        return (this.top == -1);
    }

    /**
     * �鿴��ǰջ��������
     * @return ������
     */
    public int peek() {
        return this.stackArray[top];
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        StackX stackX = new StackX(10);
        stackX.push(20);
        stackX.push(40);
        stackX.push(60);
        stackX.push(80);
        while (!stackX.isEmpty()) {
            System.out.print(stackX.pop() + ",");
        }
    }
}

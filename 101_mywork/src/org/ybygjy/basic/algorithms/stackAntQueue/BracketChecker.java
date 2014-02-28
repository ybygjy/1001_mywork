package org.ybygjy.basic.algorithms.stackAntQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * �ַ����ָ����֤
 * @author WangYanCheng
 * @version 2011-8-8
 */
public class BracketChecker {
    private Stack stack;

    public BracketChecker() {
        this.stack = new Stack(50);
    }

    public boolean innerChecker(char c, int j) {
        switch (c) {
            case '{':
            case '[':
            case '(':
                stack.push(c);
                break;
            case ')':
            case ']':
            case '}':
                if (!this.stack.isEmpty()) {
                    char chx = this.stack.pop();
                    if ((chx == '(' && c == ')') || (chx == '{' && c == '}') || (chx == '[' && c == ']')) {
                        return true;
                    }
                    return false;
                } else {
                    System.out.println("Error " + c + "at " + j);
                    return false;
                }
            default:
                break;
        }
        return true;
    }

    public boolean doChecker(String s) {
        boolean rtnFlag = true;
        for (int i = 0; i < s.length(); i++) {
            if (!innerChecker(s.charAt(i), i)) {
                rtnFlag = false;
                break;
            }
        }
        if (!this.stack.isEmpty()) {
            System.out.println("Error: missing right delimiter.");
            rtnFlag = false;
        }
        return rtnFlag;
    }

    /**
     * �߼��������
     */
    public void doWork() {
        System.out.println("�����룺");
        System.out.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tmpS;
        try {
            while ((tmpS = reader.readLine()) != null) {
                if (doChecker(tmpS)) {
                    System.out.println(tmpS + " ��֤�ɹ���");
                } else if ("q!".equals(tmpS)) {
                    break;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        BracketChecker bcInst = new BracketChecker();
        bcInst.doWork();
    }

    class Stack {
        private int size;
        private char[] arr;
        private int top;

        public Stack(int size) {
            this.size = size;
            this.arr = new char[size];
            this.top = -1;
        }

        /**
         * ��ջ
         * @param s Ԫ��
         */
        public void push(char s) {
            this.arr[++top] = s;
        }

        /**
         * ��ջ
         * @return Ԫ��
         */
        public char pop() {
            return this.arr[top--];
        }

        public boolean isFull() {
            return (this.size - 1) == this.top;
        }

        public boolean isEmpty() {
            return -1 == this.top;
        }
    }
}

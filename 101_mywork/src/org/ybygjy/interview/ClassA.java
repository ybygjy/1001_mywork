package org.ybygjy.interview;


public class ClassA {
    //��̬��Ա����
    public static double serialNum = Math.random();
    public String userName;
    static {
        serialNum = Math.random();
    }
    public ClassA() {
        userName = "WangYanCheng";
    }
}
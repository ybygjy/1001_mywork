package org.ybygjy.pattern.state;
/**
 * Client
 * @author WangYanCheng
 * @version 2010-11-13
 */
public class Client {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        GumballMachine gumballMachine = new GumballMachine(0);
        System.out.println(gumballMachine);
        gumballMachine.insertQuarter();
        gumballMachine.ejectQuarter();
        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        System.out.println(gumballMachine);
    }
}

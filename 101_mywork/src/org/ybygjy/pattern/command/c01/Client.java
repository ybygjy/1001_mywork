package org.ybygjy.pattern.command.c01;

/**
 * �ͻ���
 * @author WangYanCheng
 * @version 2012-12-10
 */
public class Client {
    public static void main(String[] args) {
        //����Ľ�����
        Receiver receiver = new Receiver();
        //����
        Command command = new ConcreateCommand(receiver);
        //����ĵ�����
        Invoker invoker = new Invoker(command);
        invoker.invoke();
    }
}

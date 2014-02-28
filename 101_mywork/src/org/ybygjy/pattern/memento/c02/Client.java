package org.ybygjy.pattern.memento.c02;

/**
 * Memento Pattern�ͻ��ˣ���������֤���
 * @author WangYanCheng
 * @version 2013-1-7
 */
public class Client {
    public static void main(String[] args) {
        //������
        Caretaker caretaker = new Caretaker();
        //������
        Originator originator = new Originator();
        originator.changeState("ON");
System.out.println(originator.toString());
        //ת��״̬
        caretaker.saveMemento(originator.createMemenIF());
        originator.changeState("OFF");
System.out.println(originator.toString());
        //�ָ�״̬
        originator.restoreMemento(caretaker.retrieveMemento());
System.out.println(originator.toString());
    }
}

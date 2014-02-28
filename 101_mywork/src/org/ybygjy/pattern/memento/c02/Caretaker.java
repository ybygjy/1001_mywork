package org.ybygjy.pattern.memento.c02;

/**
 * Memento Pattern�����˽�ɫ
 * @author WangYanCheng
 * @version 2013-1-7
 */
public class Caretaker {
    private MementoIF memento;
    public void saveMemento(MementoIF memento) {
        this.memento = memento;
    }
    public MementoIF retrieveMemento() {
        return memento;
    }
}

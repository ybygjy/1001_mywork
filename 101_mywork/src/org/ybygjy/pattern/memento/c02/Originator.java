package org.ybygjy.pattern.memento.c02;

/**
 * �����˽�ɫ
 * <p>
 * 1��ӵ�ж�Memento��ɫ����ȫ����(��ӿ�)
 * <p>
 * 2��������ṩMemento��ɫ��СȨ�޷���(խ�ӿ�)
 * @author WangYanCheng
 * @version 2013-1-7
 */
public class Originator {
    private String state;

    public MementoIF createMemenIF() {
        return new Memento(state);
    }

    public void restoreMemento(MementoIF memento) {
        if (memento instanceof Memento) {
            changeState((Memento) memento);
        }
    }
    public void changeState(String state) {
        this.state = state;
    }
    protected void changeState(Memento memento) {
        this.state = memento.getState();
    }
    @Override
    public String toString() {
        return "Originator [state=" + state + "]";
    }

    private class Memento implements MementoIF {
        private String state;

        public Memento(String state) {
            this.state = state;
        }

        /**
         * @return the state
         */
        private String getState() {
            return state;
        }

        /**
         * @param state the state to set
         */
        private void setState(String state) {
            this.state = state;
        }
    }
}

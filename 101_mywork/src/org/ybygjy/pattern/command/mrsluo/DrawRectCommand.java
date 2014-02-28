package org.ybygjy.pattern.command.mrsluo;

/**
 * ���ƾ���
 * @author WangYanCheng
 * @version 2011-8-1
 */
public class DrawRectCommand implements ICommand {
    private String graphInst;
    /**
     * {@inheritDoc}
     */
    public String getObject() {
        return this.graphInst;
    }

    /**
     * {@inheritDoc}
     */
    public void redo() {
        System.out.println("���ƾ��Σ�" + this.getObject() + "��");
    }

    /**
     * {@inheritDoc}
     */
    public void setObject(String obj) {
        this.graphInst = obj;
    }

    /**
     * {@inheritDoc}
     */
    public void undo() {
        System.out.println("�������Σ�" + this.getObject() + "��");
    }

}

package org.ybygjy.pattern.command.mrsluo;

/**
 * ������Բ
 * @author WangYanCheng
 * @version 2011-8-1
 */
public class DrawEllipseCommand implements ICommand {
    private String grapObj;
    /**
     * {@inheritDoc}
     */
    public String getObject() {
        return grapObj;
    }

    /**
     * {@inheritDoc}
     */
    public void redo() {
        System.out.println("������Բ��" + getObject() + "��");
    }

    /**
     * {@inheritDoc}
     */
    public void setObject(String obj) {
        this.grapObj = obj;
    }

    /**
     * {@inheritDoc}
     */
    public void undo() {
        System.out.println("������Բ��" + getObject() + "��");
    }

}

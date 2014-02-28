package org.ybygjy.pattern.command.mrsluo;

/**
 * ��װ����
 * @author WangYanCheng
 * @version 2011-8-1
 */
public interface ICommand {
    /**
     * ȡ�����������
     * @return �����������
     */
    public String getObject();
    /**
     * ���������������
     * @param obj �����������
     */
    public void setObject(String obj);
    /**
     * ����
     */
    public void redo();
    /**
     * ����
     */
    public void undo();
}

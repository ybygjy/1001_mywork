package org.ybygjy.dbcompare;

import java.sql.Connection;

import org.ybygjy.dbcompare.model.ContextModel;


/**
 * �������񹫹���Ϊ
 * @author WangYanCheng
 * @version 2011-10-8
 */
public interface Task {
    /**
     * ִ������
     */
    public void execute();

    /**
     * ������������
     * @param taskListener �������ʵ��
     */
    public void addListener(TaskListener taskListener);

    /**
     * ɾ�����������
     * @param taskListener �������ʵ��
     */
    public void removeListener(TaskListener taskListener);

    /**
     * ������Ҫ�����ݿ⽻��
     * @param conn ���ݿ�����
     */
    public void setConn(Connection conn);

    /**
     * ����ͨ��ģ��ʵ��
     * @return contextModel ģ��ʵ��
     */
    public ContextModel getCommonModel();
}

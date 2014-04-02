package org.ybygjy.dbcompare;

import java.io.OutputStream;

import org.ybygjy.dbcompare.model.ContextModel;


/**
 * 
 * @author WangYanCheng
 * @version 2011-10-9
 */
public interface TaskReport {
    /**
     * ���ɱ���
     * @param contextModel ������ģ�͹���ʵ����
     */
    public void generateReport(ContextModel[] commonModel);
    /**
     * ��ʱ���������������Ϣ
     * @param srcUser ԭʼ�û�
     */
    public void setSrcUser(String srcUser);
    /**
     * ��ʱ���������������Ϣ
     * @param targetUser �����û�
     */
    public void setTargetUser(String targetUser);
    /**
     * ��ʱ�������������ת����������
     * @param ous ת��������ʵ��
     */
    public void setReOutputStream(OutputStream ous);
}

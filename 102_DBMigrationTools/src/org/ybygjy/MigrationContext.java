package org.ybygjy;


/**
 * ���������ĸ���������֮�������
 * @author WangYanCheng
 * @version 2012-10-17
 */
public interface MigrationContext {
    /**
     * ȡ����������
     * @param attrName ����
     * @return attrValue ֵ
     */
    public Object getAttribute(String attrName);
    /**
     * �洢����������
     * @param attrName ����
     * @param attrValue ֵ
     */
    public void setAttribute(String attrName, Object attrValue);
    /**
     * ׷�ӷ�ʽ�洢�����ı���
     * @param attrName 
     * @param attrValue
     */
    public void appendSortedAttr(String attrName, String attrValue);
}

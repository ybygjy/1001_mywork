package org.ybygjy.pattern.state;
/**
 * ��������״̬�ӿڹ淶
 * @author WangYanCheng
 * @version 2010-11-13
 */
public interface State {
    /**
     * Ͷ��Ǯ��
     */
    public void insertQuarter();
    /**
     * �˻�Ǯ��
     */
    public void enjectQuarter();
    /**
     * ת������
     */
    public void turnCrank();
    /**
     * ����
     */
    public void dispense();
}

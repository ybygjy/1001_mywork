package org.ybygjy.dbcompare;
/**
 * ����״̬����
 * @author WangYanCheng
 * @version 2011-10-8
 */
public interface TaskListener {
    /**
     * ����ִ��ǰ����
     * @param taskInst ����ʵ��
     */
    public void beforeExecute(Task taskInst);
    /**
     * ����ִ�к����
     * @param taskInst ����ʵ��
     */
    public void afterExecute(Task taskInst);
}

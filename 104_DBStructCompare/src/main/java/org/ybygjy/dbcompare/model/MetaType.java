package org.ybygjy.dbcompare.model;

/**
 * �����������
 * @author WangYanCheng
 * @version 2011-10-9
 */
public enum MetaType {
    /** �� */
    TABLE_OBJ(1),
    /** ���ֶ� */
    TABLE_FIELDOBJ(2),
    /** ��ͼ */
    VIEW_OBJ(3),
    /** ���� */
    FUNC_OBJ(4),
    /** ���� */
    PROC_OBJ(5),
    /** ������ */
    TRIG_OBJ(6),
    /** ���� */
    SEQ_OBJ(7),
    /** ����*/
    TYPE_OBJ(8),
    /** Լ��*/
    CONS_OBJ(9),
    /** ״̬�Ƿ�*/
    INVALID_OBJ(10),
    /** ������(Oracle)*/
    PACKAGE_OBJ(11),
    /**δʶ��*/
    NONE_OBJ(100);
    private int taskType;

    /**
     * Constructor
     * @param taskType taskType
     */
    MetaType(int taskType) {
        this.taskType = taskType;
    }
    @Override
    public String toString() {
        return String.valueOf(this.taskType);
    }
}

package org.ybygjy.meta.model;

/**
 * Լ���ֶ�
 * @author WangYanCheng
 * @version 2012-9-13
 */
public class ConstraintColumnMeta {
    /** Լ��*/
    private ConstraintMeta cmInst;
    /** �����*/
    private TableMeta tableMeta;
    /** �ֶ�*/
    private FieldMeta fieldMeta;
    /**
     * @return the cmInst
     */
    public ConstraintMeta getCmInst() {
        return cmInst;
    }
    /**
     * @param cmInst the cmInst to set
     */
    public void setCmInst(ConstraintMeta cmInst) {
        this.cmInst = cmInst;
    }
    /**
     * @return the tableMeta
     */
    public TableMeta getTableMeta() {
        return tableMeta;
    }
    /**
     * @param tableMeta the tableMeta to set
     */
    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }
    /**
     * @return the fieldMeta
     */
    public FieldMeta getFieldMeta() {
        return fieldMeta;
    }
    /**
     * @param fieldMeta the fieldMeta to set
     */
    public void setFieldMeta(FieldMeta fieldMeta) {
        this.fieldMeta = fieldMeta;
    }
}

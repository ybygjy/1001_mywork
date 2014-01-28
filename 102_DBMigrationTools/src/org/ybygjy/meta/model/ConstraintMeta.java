package org.ybygjy.meta.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ʵ��Լ��
 * @author WangYanCheng
 * @version 2012-9-13
 */
public class ConstraintMeta {
    /** Լ������ */
    private String constraintName;
    /** Լ������ */
    private ConstraintType constraintType;
    /** ���������� */
    private TableMeta tableMeta;
    /** ��/���ñ�ʶ */
    private boolean isEnable;
    /** Լ���� */
    private List<ConstraintColumnMeta> constaintColumn = new ArrayList<ConstraintColumnMeta>();

    /**
     * @return the constraintName
     */
    public String getConstraintName() {
        return constraintName;
    }

    /**
     * @param constraintName the constraintName to set
     */
    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    /**
     * @return the constraintType
     */
    public ConstraintType getConstraintType() {
        return constraintType;
    }

    /**
     * @param constraintType the constraintType to set
     */
    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
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
     * @return the constaintColumn
     */
    public List<ConstraintColumnMeta> getConstraintColumn() {
        return constaintColumn;
    }

    /**
     * ���Լ���ֶ�
     * @param ccmArr Լ���ֶμ�
     */
    public void setConstraintColumn(List<ConstraintColumnMeta> ccmArr) {
        this.constaintColumn.addAll(ccmArr);
    }

    /**
     * @return the isEnable
     */
    public boolean isEnable() {
        return isEnable;
    }

    /**
     * @param isEnable the isEnable to set
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * ����OracleԼ�����͵�ö��
     * @author WangYanCheng
     * @version 2012-9-13
     */
    public enum ConstraintType {
        /** Unknown Type*/
        UN("UNKNOWN"),
        /** Check Constraint*/
        C("C"),
        /** Primary Key*/
        P("P"),
        /** Unique Key*/
        U("U"),
        /** Referential Integrity*/
        R("R"),
        /** With check option on a view*/
        V("V"),
        /** With read only, on a view*/
        O("O"),
        /** Hash expression*/
        H("H"),
        /** Constraint that involves a REF column*/
        F("F"),
        /** Supplemental logging*/
        S("S");
        /**���ͱ��*/
        private String type;
        
        /**
         * Constructor
         * @param type ����
         */
        private ConstraintType(String type) {
            this.type = type;
        }
        
        /**
         * �����ַ���ȡ��Ӧ��Լ�����ͣ�Ĭ�Ϸ���δ֪����
         * @param typeValue ���������ַ���
         * @return rtnType 
         */
        public static ConstraintType getConstraintType(String typeValue) {
            for (ConstraintType ct : ConstraintType.values()) {
                if (ct.getConstraintValue().equals(typeValue)) {
                    return ct;
                }
            }
            return UN;
        }
        
        /**
         * Լ��ֵ
         * @return type Լ��ֵ
         */
        public String getConstraintValue() {
            return this.type;
        }
    }
}

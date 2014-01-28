package org.ybygjy.sql.model;

import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.meta.model.TableMeta;

/**
 * ����SQL���ģ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class SqlModel {
    /** SQL��� */
    private String sqlStmt;
    /** �����ֶ�����*/
    private FieldMeta[] whereFields;
    /** ��ѯ/�����ֶ�����*/
    private FieldMeta[] selectFields;
    /** {@link TableMeta} */
    private TableMeta tableMeta;

    /**
     * setter tableMeta
     * @param tableMeta {@link TableMeta}
     */
    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }

    /**
     * getter tableMeta
     * @return tableMeta {@link TableMeta}
     */
    public TableMeta getTableMeta() {
        return this.tableMeta;
    }

    /**
     * @return the sqlStmt
     */
    public String getSqlStmt() {
        return sqlStmt;
    }

    /**
     * @param sqlStmt the sqlStmt to set
     */
    public void setSqlStmt(String sqlStmt) {
        this.sqlStmt = sqlStmt;
    }

    /**
     * @return the whereFields
     */
    public FieldMeta[] getWhereFields() {
        return whereFields;
    }

    /**
     * @param whereFields the whereFields to set
     */
    public void setWhereFields(FieldMeta[] whereFields) {
        this.whereFields = whereFields;
    }

    /**
     * @return the selectFields
     */
    public FieldMeta[] getSelectFields() {
        return selectFields;
    }

    /**
     * @param selectFields the selectFields to set
     */
    public void setSelectFields(FieldMeta[] selectFields) {
        this.selectFields = selectFields;
    }
}

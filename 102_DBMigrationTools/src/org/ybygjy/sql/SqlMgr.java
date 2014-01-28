package org.ybygjy.sql;

import java.util.List;
import java.util.Map;

import org.ybygjy.meta.model.TableMeta;
import org.ybygjy.sql.model.SqlModel;


/**
 * ���������SQL���Ĺ���
 * @author WangYanCheng
 * @version 2012-4-9
 */
public interface SqlMgr {
    /**
     * ������ѯSQL
     * @param tableMeta {@link TableMeta}
     * @return sqlStr SQL���
     */
    public SqlModel buildQrySQL(TableMeta tableMeta);

    /**
     * ��������SQL
     * @param tableMeta {@link TableMeta}
     * @return sqlStr SQL���
     */
    public SqlModel buildInsSQL(TableMeta tableMeta);

    /**
     * ������ѯSQL
     * @param tableMeta {@link TableMeta}
     * @return sqlStr SQL���
     */
    public Map<String, SqlModel> buildQrySQL(List<TableMeta> tableMeta);

    /**
     * ��������SQL
     * @param tableMeta {@link TableMeta}
     * @return sqlStr SQL���
     */
    public Map<String, SqlModel> buildInsSQL(List<TableMeta> tableMeta);

    /**
     * ������ѯ{@link java.sql.Clob}�����ֶ�
     * @param tableMeta {@link TableMeta}
     * @return rtnSMInst SQL���ʵ��
     * @see SqlModel
     */
    public SqlModel buildQryClobSQL(TableMeta tableMeta);

    /**
     * ��������SQL
     * @param tableMeta {@link TableMeta}
     * @return rtnSMInst SQL���ʵ��
     * @see SqlModel
     */
    public SqlModel buildInsertClobSQL(TableMeta tableMeta);
}

package org.ybygjy.exec.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.ybygjy.ctx.MigrationContextFactory;
import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.sql.model.SqlModel;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.SysConstants;

/**
 * SqlExecutor Oracleʵ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class SqlExecutorImpl4Oracle extends AbstractSqlExecutor {
    /** ?������ */
    private String sqlRegQM = "\\?";

    /**
     * ���캯��
     * @param conn �����ݿ������
     */
    public SqlExecutorImpl4Oracle(Connection conn) {
        super(conn);
    }

    @Override
    protected int rawInsert(SqlModel sqlModel, List<Map<String, Object>> dataMapList) {
        int rtnCount = 0;
        Statement stmt = null;
        logger.info("������ԭʼ/�������ִ��SQL");
        try {
            stmt = conn.createStatement();
            for (Iterator<Map<String, Object>> iterator = dataMapList.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                String sql = generalStandInsertSQL(sqlModel, dataMap);
                try {
                    stmt.execute(sql);
                    rtnCount++;
                } catch (Exception e) {
//                    logger.info("ԭʼ/���䷽ʽִ��SQL����".concat(e.getMessage()));
//                    logger.info("����SQL��䣺".concat(sql));
                    //TODO �˴������⣬��Ҫ���Ǵ����¼���������������OOM���ɿ��ǲ����ⲿ�ļ�I/O���ķ�ʽ����
                    MigrationContextFactory.getInstance().getCtx().appendSortedAttr(SysConstants.CTX_SQL_RAWINSERT_FAIL, sql);
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "ԭʼSQL��ʽ�������", e);
        } finally {
            DBUtils.close(stmt);
        }
        logger.info("��ɣ�ԭʼ/�������ִ��SQL����Ӱ�죺".concat(String.valueOf(rtnCount)).concat("�У�"));
        return rtnCount;
    }

    /**
     * �����������SQL
     * @param sqlModel {@link SqlModel}
     * @param dataMap ���ݼ�
     * @return rtnSql SQL���
     */
    private String generalStandInsertSQL(SqlModel sqlModel, Map<String, Object> dataMap) {
        SimpleDateFormat sdfInst = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmplSql = sqlModel.getSqlStmt();
        FieldMeta[] tmpParames = sqlModel.getSelectFields();
        String dateTmpl = "TO_DATE('@T', 'YYYY-MM-DD HH24:MI:SS')";
        for (FieldMeta fmInst : tmpParames) {
            String fieldCode = fmInst.getFieldCode();
            Object objValue = dataMap.get(fieldCode);
            switch (fmInst.getFieldType()) {
                case NUM:
                    tmplSql = tmplSql.replaceFirst(sqlRegQM,
                        objValue == null ? "0" : String.valueOf((Double) objValue));
                    break;
                case DATE:
                    String tmpStr = objValue == null ? "NULL" : dateTmpl.replace("@T",
                        sdfInst.format(new Date(((java.sql.Date) objValue).getTime())));
                    tmplSql = tmplSql.replaceFirst(sqlRegQM, tmpStr);
                    break;
                default:
                    // Ĭ�϶����ַ�������
                    tmplSql = tmplSql.replaceFirst(sqlRegQM,
                        objValue == null ? "NULL" : "'".concat(String.valueOf(objValue)).concat("'"));
                    break;
            }
        }
        return tmplSql;
    }
}

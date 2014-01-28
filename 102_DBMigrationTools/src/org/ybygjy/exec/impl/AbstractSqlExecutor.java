package org.ybygjy.exec.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ybygjy.exec.SqlExecutor;
import org.ybygjy.exec.SqlExecutorListener;
import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.sql.model.SqlModel;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.SysConstants;

/**
 * ����SQLִ��
 * @author WangYanCheng
 * @version 2012-10-16
 */
public abstract class AbstractSqlExecutor implements SqlExecutor {
    /** {@link Logger} */
    protected Logger logger;
    /** �����ݿ������ */
    protected Connection conn;
    /** ������ */
    private SqlExecutorListener selInst;

    /**
     * ���캯��
     * @param conn {@link Connection}
     */
    public AbstractSqlExecutor(Connection conn) {
        this.conn = conn;
        this.logger = LoggerFactory.getInstance().getLogger(getClass().getName());
    }

    @Override
    public int executeQuery(SqlModel sqlModel) {
        int dataCount = 0;
        Statement stmt = null;
        ResultSet rs = null;
        String tmpSql = sqlModel.getSqlStmt();
        logger.info("ִ�в�ѯ#SQL��䣺".concat(tmpSql));
        try {
            stmt = this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(tmpSql);
            List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
            int flag = 1;
            while (rs.next()) {
                dataCount++;
                if (flag >= SysConstants.MAX_CACHEITEM) {
                    logger.info("ִ�в�ѯ#�����ˣ�".concat(String.valueOf(tmpList.size())).concat("����"));
                    this.fireAfterQuery(sqlModel, tmpList, rs);
                    tmpList.clear();
                    flag = 1;
                }
                tmpList.add(assemblyData(rs, sqlModel));
                flag++;
            }
            if (tmpList.size() > 0) {
                logger.info("ִ�в�ѯ#�����ˣ�".concat(String.valueOf(tmpList.size())).concat("����"));
                this.fireAfterQuery(sqlModel, tmpList, rs);
            }
        } catch (SQLException e) {
            logger.warning("ִ�в�ѯ#����".concat(tmpSql));
            e.printStackTrace();
        } finally {
            DBUtils.close(rs);
            DBUtils.close(stmt);
        }
        return dataCount;
    }

    @Override
    public int executeInsert(SqlModel sqlModel, List<Map<String, Object>> dataMapList) {
        int rtnCount = 0;
        String tmpSql = sqlModel.getSqlStmt();
        logger.info("ִ�в���#SQL��䣺".concat(tmpSql));
        PreparedStatement pstmt = null;
        boolean changeMode = false;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(tmpSql);
            for (Iterator<Map<String, Object>> iterator = dataMapList.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                settingParameter(sqlModel, pstmt, dataMap);
                pstmt.addBatch();
            }
            int[] effectArr = pstmt.executeBatch();
            rtnCount = effectArr.length;
            conn.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "ִ�в������", e);
            changeMode = true;
            try {
                conn.rollback();
            } catch (SQLException e1) {
                logger.log(Level.WARNING, "Connection�ع�����", e1);
            }
            
        } finally {
            DBUtils.close(pstmt);
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(Level.WARNING, "��ԭConnection�Զ��ύ����", e);
            }
        }
        logger.info("ִ�в���#�����ˣ�".concat(String.valueOf(rtnCount)).concat("����"));
        if (changeMode) {
            logger.info("����ִ��SQL������Ҫ�л������䷽ʽִ�У��Զ�λ����SQL��");
            rawInsert(sqlModel, dataMapList);
        }
        return rtnCount;
    }

    @Override
    public void executeSQL(String sql) {
        executeSQL(new String[] { sql });
    }

    @Override
    public void executeSQL(String[] sqls) {
        Statement stmt = null;
        String tmplSql = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (String sql : sqls) {
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            logger.log(Level.WARNING, "ִ��SQL���ִ���".concat(tmplSql == null ? "" : tmplSql), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBUtils.close(stmt);
        }
    }

    @Override
    public void addListener(SqlExecutorListener selInst) {
        this.selInst = selInst;
    }

    /**
     * ����FieldMeta�����ض����͵�ֵ��
     * @param rs {@link ResultSet}
     * @param sqlModel {@link SqlModel}
     * @return dataMap ֵ��
     * @throws SQLException {@link SQLException}
     */
    protected Map<String, Object> assemblyData(ResultSet rs, SqlModel sqlModel) throws SQLException {
        FieldMeta[] fmInstArr = sqlModel.getSelectFields();// sqlModel.getColumnMeta();
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        for (FieldMeta fmInst : fmInstArr) {
            String fieldCode = fmInst.getFieldCode();
            Object tmpObj = null;
            switch (fmInst.getFieldType()) {
                case STR:
                    tmpObj = rs.getString(fieldCode);
                    break;
                case NUM:
                    tmpObj = rs.getString(fieldCode);
                    tmpObj = null == tmpObj ? null : rs.getDouble(fieldCode);
                    break;
                case DATE:
                    tmpObj = rs.getDate(fieldCode);
                    break;
            }
            if (tmpObj != null) {
                rtnMap.put(fieldCode, tmpObj);
            }
        }
        return rtnMap;
    }

    /**
     * ��װ�¼�����
     * @param sqlModel {@link SqlModel}
     * @param dataMap ���ݼ�
     * @param rs {@link ResultSet}
     */
    protected void fireAfterQuery(SqlModel sqlModel, List<Map<String, Object>> dataMap, ResultSet rs) {
        this.selInst.afterQuery(sqlModel, dataMap);
    }

    /**
     * ������ͨ����ִ�в���
     * @param sqlModel {@link SqlModel}
     * @param pstmt {@link PreparedStatement}
     * @param dataMap ���ݼ�
     * @throws SQLException {@link SQLException}
     */
    protected void settingParameter(SqlModel sqlModel, PreparedStatement pstmt, Map<String, Object> dataMap)
        throws SQLException {
        FieldMeta[] tmpParames = sqlModel.getSelectFields();// sqlModel.getColumnMeta();
        int flag = 1;
        for (FieldMeta fmInst : tmpParames) {
            switch (fmInst.getFieldType()) {
                case STR:
                    pstmt.setString(flag++, (String) dataMap.get(fmInst.getFieldCode()));
                    break;
                case NUM:
                    Object tmpObj = dataMap.get(fmInst.getFieldCode());
                    if (tmpObj != null) {
                        pstmt.setDouble(flag++, (Double) tmpObj);
                    } else {
                        pstmt.setString(flag++, null);
                    }
                    break;
                case DATE:
                    pstmt.setDate(flag++, (java.sql.Date) dataMap.get(fmInst.getFieldCode()));
                    break;
            }
        }
    }

    /**
     * ԭʼ/���䷽ʽִ��SQL
     * @param sqlModel {@link SqlModel}
     * @param dataMapList ���ݼ�
     * @return rtnCount Ӱ������
     */
    protected int rawInsert(SqlModel sqlModel, List<Map<String, Object>> dataMapList) {
        return 0;
    }
}

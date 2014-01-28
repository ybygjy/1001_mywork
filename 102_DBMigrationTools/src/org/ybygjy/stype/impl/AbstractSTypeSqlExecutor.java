package org.ybygjy.stype.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ybygjy.exec.SqlExecutorListener;
import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.sql.model.SqlModel;
import org.ybygjy.stype.STypeSqlExecutor;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.SysConstants;

/**
 * ������_���������͵Ĵ���
 * @author WangYanCheng
 * @version 2012-10-16
 */
public abstract class AbstractSTypeSqlExecutor implements STypeSqlExecutor {
    /** Logger */
    protected Logger logger;
    /** �����ݿ������ */
    protected Connection conn;
    /** ������ */
    protected SqlExecutorListener selInst;

    /**
     * ���캯��
     * @param conn �����ݿ������
     */
    public AbstractSTypeSqlExecutor(Connection conn) {
        this.conn = conn;
        this.logger = LoggerFactory.getInstance().getLogger(getClass().getName().toString());
    }

    @Override
    public void executeQuerySpecialType(SqlModel sqlModel) {
        PreparedStatement pstmt = null;
        String sqlTMPL = null;
        if (sqlModel != null && (sqlTMPL = sqlModel.getSqlStmt()) != null) {
            return;
        }
        ResultSet rs = null;
        logger.info("ִ�в�ѯ#SQL��䣺".concat(sqlTMPL));
        try {
            pstmt = this.conn.prepareStatement(sqlTMPL);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                this.selInst.afterSpecialTypeQuery(sqlModel, rs);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "ִ�в�ѯ#����".concat(sqlTMPL), e);
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
    }

    @Override
    public int executeSpecialTypeInsert(SqlModel srcSqlModel, SqlModel targetSqlModel, ResultSet rs) {
        if (null == rs) {
            return -1;
        }
        int rtnCount = 0;
        // ������ȷ���������ֻ���SQL��UPDATE TABLE A SET A.COL=? AND
        // A.KEY=?����������Where����һ���ں���
        String sqlTmpl = targetSqlModel.getSqlStmt();
        // ȡ��ѯ
        FieldMeta[] selectFields = targetSqlModel.getSelectFields();
        // ȡWhere����
        FieldMeta[] whereFields = targetSqlModel.getWhereFields();
        PreparedStatement pstmt = null;
        int rowCount = 0;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sqlTmpl);
            // ��Ϊ�������һ��rs.next()���Դ˴�����do/while
            do {
                int paramIndex = 1;
                paramIndex = setterClobParameter(pstmt, rs, selectFields, paramIndex);
                paramIndex = setterParamater(pstmt, rs, whereFields, paramIndex);
                pstmt.addBatch();
                rowCount++;
                if (rowCount > (SysConstants.MAX_CACHEITEM / 10 - 1)) {
                    executeBatch(pstmt, true);
                    logger.info("������".concat(String.valueOf(rowCount)).concat("��"));
                    rowCount = 0;
                }
                rtnCount++;
            } while (rs.next());
            if (rowCount != 0) {
                executeBatch(pstmt, true);
                rowCount = 0;
            }
        } catch (SQLException e) {
            this.logger.log(Level.WARNING, "�����߼�����", e);
            try {
                this.conn.rollback();
            } catch (SQLException e1) {
                this.logger.log(Level.WARNING, "�ع�����", e1);
            }
        } finally {
            if (pstmt != null) {
                DBUtils.close(pstmt);
            }
            try {
                this.conn.setAutoCommit(true);
            } catch (SQLException e) {
                this.logger.log(Level.WARNING, "����Connection�Զ��ύ����", e);
            }
        }
        return rtnCount;
    }

    @Override
    public void addListener(SqlExecutorListener selInst) {
        this.selInst = selInst;
    }

    /**
     * ������ͨ����ִ�в���
     * @param pstmt {@link PreparedStatement}
     * @param rs {@link ResultSet}
     * @param fms {@link FieldMeta}
     * @param paramIndex ��������
     * @return paramIndex �������������ں�����������ִ�в���
     * @throws SQLException {@link SQLException}
     */
    protected int setterParamater(PreparedStatement pstmt, ResultSet rs, FieldMeta[] fms, int paramIndex)
        throws SQLException {
        for (FieldMeta fm : fms) {
            switch (fm.getFieldType()) {
                case STR:
                    pstmt.setString(paramIndex++, rs.getString(fm.getFieldCode()));
                    break;
                case NUM:
                    pstmt.setDouble(paramIndex++, rs.getDouble(fm.getFieldCode()));
                    break;
                case DATE:
                    pstmt.setDate(paramIndex++, rs.getDate(fm.getFieldCode()));
                    break;
            }
        }
        return paramIndex;
    }

    /**
     * ����Clob����ִ�в���
     * @param pstmt {@link PreparedStatement}
     * @param rs {@link ResultSet}
     * @param fms {@link FieldMeta}
     * @param paramIndex ��������
     * @return paramIndex �������������ں�����������ִ�в���
     * @throws SQLException {@link SQLException}
     */
    protected abstract int setterClobParameter(PreparedStatement pstmt, ResultSet rs, FieldMeta[] fms,
                                               int paramIndex) throws SQLException;

    /**
     * ִ��
     * @param pstmt {@link PreparedStatement}
     * @param canCommit �ɷ��ύ����{true:��;false:��}
     * @throws SQLException {@link SQLException}
     */
    private void executeBatch(PreparedStatement pstmt, boolean canCommit) throws SQLException {
        verifyBatchUpdateStatus(pstmt.executeBatch());
        if (canCommit) {
            this.conn.commit();
        }
    }

    /**
     * ��֤
     * @param flags ״̬��
     */
    private void verifyBatchUpdateStatus(int[] flags) {
        StringBuilder sbud = new StringBuilder("���� ".concat(String.valueOf(flags.length)).concat(
            " ���������쳣״̬ "));
        int ii = 0;
        for (int i : flags) {
            if (Statement.EXECUTE_FAILED == i) {
                ii++;
            }
        }
        if (ii > 0) {
            sbud.append(String.valueOf(ii)).append(" ��");
            this.logger.warning(sbud.toString());
        }
    }
}

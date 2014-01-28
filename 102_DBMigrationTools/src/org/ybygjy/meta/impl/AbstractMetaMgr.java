package org.ybygjy.meta.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.meta.MetaMgr;
import org.ybygjy.util.DBUtils;

/**
 * ����Meta����ʵ��
 * @author WangYanCheng
 * @version 2012-10-19
 */
public abstract class AbstractMetaMgr implements MetaMgr {
    /** �����ݿ�����Ӷ��� */
    protected Connection conn;
    /** Logger Instance */
    protected Logger logger;
    /**
     * ���캯��
     * @param conn �����ݿ������
     */
    public AbstractMetaMgr(Connection conn) {
        this.conn = conn;
        logger = LoggerFactory.getInstance().getLogger(getClass().getName());
    }

    @Override
    public boolean disableTableTriggers(String tableName) {
        String sqlTMPL = "ALTER TABLE ".concat(tableName).concat(" DISABLE ALL TRIGGERS");
        Statement stmt = null;
        boolean rtnFlag = false;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.execute(sqlTMPL);
            rtnFlag = true;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "���ñ�����ʧ��", e);
        } finally {
            DBUtils.close(stmt);
        }
        return rtnFlag;
    }

    @Override
    public boolean enableTableTriggers(String tableName) {
        String sqlTMPL = "ALTER TABLE ".concat(tableName).concat(" ENABLE ALL TRIGGERS");
        Statement stmt = null;
        boolean rtnFlag = false;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.execute(sqlTMPL);
            rtnFlag = true;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "���ñ�����ʧ��", e);
        } finally {
            DBUtils.close(stmt);
        }
        return rtnFlag;
    }

    @Override
    public abstract void resetTableSequence(String tableName);
}

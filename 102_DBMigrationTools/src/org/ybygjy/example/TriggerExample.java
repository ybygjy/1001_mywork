package org.ybygjy.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ybygjy.util.DBUtils;


/**
 * ����������
 * @author WangYanCheng
 * @version 2012-4-23
 */
public class TriggerExample {
    /** �����ݿ������ */
    private Connection conn;
    /**
     * ���ô�����
     * @param triggerName ����������
     * @return rtnFlag true/false
     * @throws SQLException {@link SQLException}
     */
    public boolean enableTrigger(String triggerName) throws SQLException {
        String sqlTmpl = "ALTER TRIGGER @T ENABLE".replaceAll("@T", triggerName);
        Statement stmt = null;
        boolean rtnFlag = true;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.execute(sqlTmpl);
        } catch (SQLException sqle) {
            rtnFlag = false;
            sqle.printStackTrace();
        } finally {
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnFlag;
    }
    /**
     * ���ô�����
     * @param triggerName ����������
     * @return rtnFlag true/false
     * @throws SQLException {@link SQLException}
     */
    public boolean disableTrigger(String triggerName) throws SQLException {
        String sqlTmpl = "ALTER TRIGGER @T DISABLE".replaceAll("@T", triggerName);
        Statement stmt = null;
        boolean rtnFlag = true;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.execute(sqlTmpl);
        } catch (SQLException sqle) {
            rtnFlag = false;
            sqle.printStackTrace();
        } finally {
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnFlag;
    }

    /**
     * ȡ����������������trigger��������
     * @param tableName �����
     * @return rtnArr rtnArr/null
     * @throws SQLException {@link SQLException}
     */
    public String[] getTriggers(String tableName) throws SQLException {
        String[] rtnArr = null;
        String tmplSql = "SELECT TRIGGER_NAME FROM USER_TRIGGERS WHERE TABLE_NAME=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = this.conn.prepareStatement(tmplSql);
            pstmt.setString(1, tableName);
            rs = pstmt.executeQuery();
            List<String> tmpList = new ArrayList<String>();
            while (rs.next()) {
                tmpList.add(rs.getString(1));
            }
            if (tmpList.size() > 0) {
                rtnArr = tmpList.toArray(new String[0]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != pstmt) {
                pstmt.close();
            }
        }
        return rtnArr;
    }

    /**
     * ���������������Ĵ�����
     * @param tableNames ����������
     * @throws SQLException {@link SQLException}
     */
    public void disableTableTriggers(String[] tableNames) throws SQLException {
        for (String tableName : tableNames) {
            String[] triggerArray = getTriggers(tableName);
            if (null == triggerArray) {
                continue;
            }
            for (String triggerName : triggerArray) {
                System.out.println("Լ����".concat(tableName).concat("#").concat(triggerName));
                disableTrigger(triggerName);
            }
        }
    }
    /**
     * ���������������Ĵ�����
     * @param tableNames ����������
     * @throws SQLException {@link SQLException}
     */
    public void enableTableTriggers(String[] tableNames) throws SQLException {
        for (String tableName : tableNames) {
            String[] triggerArray = getTriggers(tableName);
            if (null == triggerArray) {
                continue;
            }
            for (String triggerName : triggerArray) {
                System.out.println("Լ����".concat(tableName).concat("#").concat(triggerName));
                enableTrigger(triggerName);
            }
        }
    }
    public void setConnection(Connection conn) {
        this.conn = conn;
    }
    public static void main(String[] args) {
        String[] tableNames = {"CNTNOTICEJOB", "CNTNOTICESTEP", "FB_CHARGE_PLANS", "FB_CNTNOTICEJOB_LOG", "FB_ECDS_INFO", "FB_FUND_DIRECTION", "FB_FUND_DIRECTION_LOG", "FB_GROUP_BANK", "FB_INTEREST_ALLOT_LOG"};
        String connURL = "jdbc:oracle:thin:NSTCSA2922/726078@192.168.3.232:1521/NSDEV";
        Connection conn = DBUtils.createConn4Oracle(connURL);
        TriggerExample teInst = new TriggerExample();
        teInst.setConnection(conn);
        try {
            //teInst.disableTableTriggers(tableNames);
            teInst.enableTableTriggers(tableNames);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                DBUtils.close(conn);
            }
        }
    }
}

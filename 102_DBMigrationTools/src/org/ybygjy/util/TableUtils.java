package org.ybygjy.util;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ybygjy.logger.LoggerFactory;

/**
 * ��װ�Եĳ��ò���
 * @author WangYanCheng
 * @version 2012-5-2
 */
public class TableUtils {
    /** SQL��� */
    private static final String truncateSql = "DELETE FROM @T";
    /** ����Լ��SQL */
    private static final String enableConsSQL = "ALTER TABLE @T ENABLE CONSTRAINT @C";
    /** ��ѯ���Լ��SQL */
    private static final String qryConstSql = "SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS WHERE TABLE_NAME = ?";
    /** ����Լ��SQL */
    private static final String disableConstSql = "ALTER TABLE @T DISABLE CONSTRAINT @C CASCADE";
    /** Logger*/
    private static Logger logger;
    static {
        logger = LoggerFactory.getInstance().getLogger(TableUtils.class.getName());
    }
    /**
     * ������������������
     * @param conn �����ݿ������
     * @param tableName ������
     * @return rtnFlag 0/-1
     */
    public static int truncateTable(Connection conn, String tableName) {
        Statement stmt = null;
        String tmplSql = truncateSql.replaceAll("@T", tableName);
        try {
            disableTableConstraint(conn, tableName);
            stmt = conn.createStatement();
            stmt.execute(tmplSql);
            enableTableConstraint(conn, tableName);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "��������", e);
            return SysConstants.TABLE_TRUNCATE_FAILURE;
        } finally {
            DBUtils.close(stmt);
        }
        return SysConstants.TABLE_TRUNCATE_SUCCESS;
    }

    /**
     * ���ø������Լ��
     * @param conn ���Ӵ�
     * @param tableName ������
     */
    public static void disableTableConstraint(Connection conn, String tableName) throws SQLException {
        String[] consArr = getTableConstraints(conn, tableName);
        if (null == consArr) {
            return;
        }
        for (String consStr : consArr) {
            disableTableConstraint(conn, tableName, consStr);
        }
    }

    /**
     * ���ñ��Լ��
     * @param conn ���Ӵ�
     * @param tableName ������
     */
    public static void enableTableConstraint(Connection conn, String tableName) throws SQLException {
        String[] consArr = getTableConstraints(conn, tableName);
        if (null == consArr) {
            return;
        }
        for (String consStr : consArr) {
            enableTableConstraint(conn, tableName, consStr);
        }
    }

    /**
     * ���ñ��Լ��
     * @param conn ���Ӵ�
     * @param tableName ������
     * @param consName Լ������
     */
    public static void enableTableConstraint(Connection conn, String tableName, String consName)
        throws SQLException {
        Statement pstmt = null;
        String tmpSql = enableConsSQL.replaceAll("@T", tableName).replaceAll("@C", consName);
        try {
            pstmt = conn.createStatement();
            pstmt.execute(tmpSql);
        } catch (SQLException e) {
            throw e;
        } finally {
            DBUtils.close(pstmt);
        }
    }

    /**
     * ���ø������Լ��
     * @param conn ���Ӵ�
     * @param tableNames ����������
     */
    public void disableTableConstraint(Connection conn, String[] tableNames) throws SQLException {
        for (String tableName : tableNames) {
            disableTableConstraint(conn, tableName);
        }
    }

    /**
     * ȡ�������Լ��
     * @param conn ����
     * @param tableName ������
     * @return rtnStrArr Լ����������
     */
    public static String[] getTableConstraints(Connection conn, String tableName) throws SQLException {
        String[] rtnConsArr = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(qryConstSql);
            pstmt.setString(1, tableName);
            rs = pstmt.executeQuery();
            List<String> tmpConsArr = new ArrayList<String>();
            while (rs.next()) {
                tmpConsArr.add(rs.getString(1));
            }
            if (tmpConsArr.size() > 0) {
                rtnConsArr = new String[tmpConsArr.size()];
                rtnConsArr = tmpConsArr.toArray(rtnConsArr);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
        return rtnConsArr;
    }

    /**
     * ���ñ�Լ��
     * @param conn ���Ӵ�
     * @param tableName ������
     * @param consName Լ������
     */
    public static void disableTableConstraint(Connection conn, String tableName, String consName)
        throws SQLException {
        Statement pstmt = null;
        String tmpSql = disableConstSql.replaceAll("@T", tableName).replaceAll("@C", consName);
        try {
            pstmt = conn.createStatement();
            pstmt.execute(tmpSql);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "���ñ�Լ������", e);
        } finally {
            DBUtils.close(pstmt);
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        String[] fileNames = FileUtils.fetchFileName(new File("C:\\KettleLog\\BMS"));
        String url4Oracle = "jdbc:oracle:thin:NSTCSA4042/6316380@192.168.3.232:1521/NSDEV";
        Connection conn = DBUtils.createConn4Oracle(url4Oracle);
        try {
            for (String fileName : fileNames) {
                fileName = fileName.replaceAll("_PD", "");
System.out.println("����".concat(fileName));
                TableUtils.enableTableConstraint(conn, fileName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn);
        }
    }
}

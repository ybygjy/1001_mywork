package org.ybygjy.dbcompare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * ������
 * @author WangYanCheng
 * @version 2011-10-4
 */
public class DBUtils {
    /** ����Դ */
    private static DataSource ds;
    /** ��ʼ����� */
    private static boolean isInit = false;
    /**SQLServer���ݿ�����*/
    private static final String MS_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    /**Oracle���ݿ�����*/
    private static final String ORA_DRIVER = "oracle.jdbc.driver.OracleDriver";
    /**
     * ȡOracle���ݿ�����
     * @param connURL ���Ӵ�
     * @return rtnConn rtnConnection
     */
    public static Connection createConn4Oracle(String connURL) {
    	Connection conn = null;
    	try {
			Class.forName(ORA_DRIVER);
			Properties prop = new Properties();
			prop.setProperty("internal_logon", "sysdba");
			conn = DriverManager.getConnection(connURL, prop);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Oracle ���ݿ���������ʧ��", e.fillInStackTrace());
		} catch (SQLException e) {
			throw new RuntimeException("Oracle ���ݿ�����ʧ��", e.fillInStackTrace());
		}
    	return conn;
    }
    /**
     * ȡMSSql���ݿ�����
     * @param connURL ���Ӵ�
     * @return rtnConn rtnConnection
     */
    public static Connection createConn4MSSql(String connURL) {
    	Connection conn = null;
    	try {
    		Class.forName(MS_DRIVER);
    		conn = DriverManager.getConnection(connURL);
    	} catch(ClassNotFoundException cnfe) {
    		throw new RuntimeException("SQLServer ���ݿ���������ʧ��", cnfe.fillInStackTrace());
    	} catch (SQLException sqle) {
    		throw new RuntimeException("SQLServer ���ݿ�����ʧ��", sqle.fillInStackTrace());
    	}
    	return conn;
    }

    /**
     * ����SQL���ִ��ʵ��
     * @param conn ���Ӷ���
     * @return rtnStmt rtnStmt
     * @throws SQLException �׳��쳣��Log
     */
    public static Statement createStmt(Connection conn) throws SQLException {
        return conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    public static void close(ResultSet rs, Statement stmt) {
        closeResultSet(rs);
        close(stmt);
    }

    public static void closeResultSet(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement stmt) {
        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �ر����ݿ�����
     * @param conn conn
     */
    public static void close(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    /**
     * ȡRS���б��룬�����鷽ʽ����
     * @param rs {@link ResultSet}
     * @return rtnArr ������
     * @throws SQLException SQLException
     */
    public static String[] getRSColumn(ResultSet rs) throws SQLException {
        if (null == rs) {
            return null;
        }
        int count = rs.getMetaData().getColumnCount();
        String[] rtnArr = new String[count];
        for (int i = 1; i <= count; i++) {
            rtnArr[i - 1] = rs.getMetaData().getColumnName(i);
        }
        return rtnArr;
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
//            conn = DBUtils.createConn4Oracle("jdbc:oracle:thin:sys/Syj129@192.168.0.74:1521/nstest");
        	conn = DBUtils.createConn4MSSql("jdbc:sqlserver://192.168.0.132:1433;databaseName=MASTER;user=sa;password=syj");
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn);
        }
    }
}

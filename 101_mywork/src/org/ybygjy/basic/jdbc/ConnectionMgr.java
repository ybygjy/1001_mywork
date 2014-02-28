package org.ybygjy.basic.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

/**
 * ���ӹ���
 * @author WangYanCheng
 * @version 2011-2-21
 */
public class ConnectionMgr {
    /**����Դ*/
    private static OracleDataSource ods;
    static {
        try {
            ods = new OracleDataSource();
            ods.setURL("jdbc:oracle:thin:leopard/leopard@192.168.0.7:1521:version");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ȡ��Connection
     * @return connInst ����ʵ��
     * @throws SQLException SQLException
     */
    public static Connection getConn() throws SQLException {
        return ods.getConnection();
    }

    /**
     * �ر�Connection
     * @param conn ����ʵ��
     */
    public static void closeConn(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

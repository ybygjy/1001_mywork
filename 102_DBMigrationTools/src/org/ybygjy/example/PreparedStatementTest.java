package org.ybygjy.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ybygjy.util.DBUtils;


/**
 * ����{@link PreparedStatement}����
 * @author WangYanCheng
 * @version 2012-4-10
 */
public class PreparedStatementTest {
    /** �����ݿ������ */
    private Connection conn;

    /**
     * Constructor
     * @param conn {@link Connection}
     */
    public PreparedStatementTest(Connection conn) {
        this.conn = conn;
    }

    /**
     * ��֤����
     * @param dataArray ���ݼ�
     */
    public void testInsert(List<Map<String, String>> dataArray) {
        String preSql = "INSERT INTO WYC_TEST(ID, NAME, ADDR, CDATE) VALUES(?, ?, ?, ?)";
        String[] preParam = { "ID", "NAME", "ADDR", "CDATE" };
        PreparedStatement pstmt = null;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(preSql);
            for (Iterator<Map<String, String>> iterator = dataArray.iterator(); iterator.hasNext();) {
                Map<String, String> dataMap = iterator.next();
                settingParameter(preParam, pstmt, dataMap);
                pstmt.addBatch();
            }
            int[] affectCount = pstmt.executeBatch();
            System.out.println("��Ӱ���������".concat(String.valueOf(affectCount)));
            this.conn.commit();
            this.conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != pstmt) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ����pstmt����
     * @param preParam ��
     * @param pstmt {@link PreparedStatement}
     * @param dataMap ���ݼ�
     * @throws SQLException {@link SQLException}
     */
    private void settingParameter(String[] preParam, PreparedStatement pstmt, Map<String, String> dataMap) throws SQLException {
        pstmt.setInt(1, Integer.parseInt(dataMap.get("ID")));
        pstmt.setString(2, dataMap.get("NAME"));
        pstmt.setString(3, dataMap.get("ADDR"));
        pstmt.setDate(4, Date.valueOf(dataMap.get("CDATE")));
    }

    /**
     * �����������
     * @return rtnDataList rtnDataList
     */
    private static List<Map<String, String>> buildInsertData() {
        List<Map<String, String>> rtnDataList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 3; i++) {
            Map<String, String> tmpMap = new HashMap<String, String>();
            String tmpStr = "100".concat(String.valueOf(i));
            tmpMap.put("ID", tmpStr);
            tmpMap.put("NAME", tmpStr);
            tmpMap.put("ADDR", tmpStr);
            tmpMap.put("CDATE", "2012-04-10");
            rtnDataList.add(tmpMap);
        }
        return rtnDataList;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBUtils.createConn4Oracle("jdbc:oracle:thin:NSTCSA2442/725382@192.168.3.232:1521/NSDEV");
            PreparedStatementTest pstInst = new PreparedStatementTest(conn);
            pstInst.testInsert(buildInsertData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                DBUtils.close(conn);
            }
        }
    }
}

package org.ybygjy.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ybygjy.util.DBUtils;

/**
 * ���������������𽫸�������ֵ����������ֵ
 * <p>ʹ��ɾ������Ȼ���ؽ��ķ�ʽ��ɣ��ʺ����в���һ�µ������
 * @author WangYanCheng
 * @version 2012-5-7
 */
public class AutoIncrementSEQ {
    private String qrySQL = "SELECT SEQUENCE_NAME FROM USER_SEQUENCES WHERE SEQUENCE_NAME LIKE '%EDC%'";
    private String dropSEQ = "DROP SEQUENCE @SEQ";
    private String createSEQ = "CREATE SEQUENCE @SEQ START WITH @SW INCREMENT BY 1 NOCYCLE NOMAXVALUE";
    private int strides = 393340;
    private Connection conn;

    /**
     * ���캯����ʼ��
     * @param conn {@link Connection}
     */
    public AutoIncrementSEQ(Connection conn) {
        this.conn = conn;
    }

    /**
     * �߼��������
     */
    public void doWork() {
        String[] seqNameArray = getSequence4DBUser();
        //String[] seqNameArray = FileUtils.fetchFileName(new File("D:\\DEV\\works\\ProD\\@N9\\sql\\FBCM\\oracle\\sql\\NEW\\seq"));
        List<String> seqDDLArray = new ArrayList<String>();
        for (String seqName : seqNameArray) {
        	if (!filter(seqName)) {
        		rebuildSEQ(seqDDLArray, seqName, strides);
        	}
        }
        innerExecDDLSql(seqDDLArray);
    }
    public boolean filter(String seqName) {
    	String[] filterSEQ = { "SEQ_CLT_INSERT", "S_CPM_CUSTOMER",
    			"S_SAM_ACTOR", "S_CPM_CUSTOMER", "S_CPM_SUBJECT",
    			"S_CPM_ACCOUNT", "S_CPM_BOOK_UNIT", "S_CPM_STD_BOOK",
    			"S_CPM_ACC_INTR", "S_CPM_WORKDAY", "S_CPM_INTR_LOG",
    			"S_CPM_VOUCHER", "S_CPM_INTR_LOG", "S_CPM_INTR_RATE"};
    	boolean rtnFlag = false;
    	for (String filterSeq : filterSEQ) {
    		rtnFlag = (filterSeq.equals(seqName));
    		System.out.println(seqName);
    		if (rtnFlag) {
    			break;
    		}
    	}
    	return rtnFlag;
    }
    /**
     * ȡ�û��������µ�����
     * @return rtnArray �������Ƽ�/null
     */
    public String[] getSequence4DBUser() {
        Statement stmt = null;
        ResultSet rs = null;
        String[] rtnArray = null;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(qrySQL);
            List<String> tmpArray = new ArrayList<String>();
            while (rs.next()) {
                tmpArray.add(rs.getString(1));
            }
            if (tmpArray.size() > 0) {
                rtnArray = new String[tmpArray.size()];
                tmpArray.toArray(rtnArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnArray;
    }

    /**
     * �ؽ�����
     * @param seqName ��������
     * @param startWith ��ʼֵ
     */
    public void rebuildSEQ(List<String> rtnSqlArr, String seqName, int startWith) {
        rtnSqlArr.add(this.dropSEQ.replace("@SEQ", seqName));
        rtnSqlArr.add(this.createSEQ.replace("@SEQ", seqName)
            .replaceFirst("@SW", String.valueOf(startWith)));
    }

    /**
     * ִ��SQL���
     * @param sqlArray
     */
    private void innerExecDDLSql(List<String> sqlArray) {
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            for (String tmpSql : sqlArray) {
                stmt.addBatch(tmpSql);
            }
            int[] rtnArr = stmt.executeBatch();
            int index = 0;
            for (String seqSQL : sqlArray) {
                System.out.println("SQL��" + seqSQL + "\t�����".concat(String.valueOf(rtnArr[index])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ִ�����
     * @param args �����б�
     */
    public static void main(String[] args) {
        String connURL = "jdbc:oracle:thin:NSTCFSS67/agcwgs6316380@192.1.116.200:1521/NSDEV";
        Connection conn = null;
        try {
            conn = DBUtils.createConn4Oracle(connURL);
            AutoIncrementSEQ aiSEQInst = new AutoIncrementSEQ(conn);
            aiSEQInst.doWork();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

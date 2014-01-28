package org.ybygjy.example;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.ybygjy.util.DBUtils;
import org.ybygjy.util.FileUtils;
import org.ybygjy.util.SysConstants;

/**
 * ��CLOB�������͵Ĵ���
 * <p>1����CLOB�е���ӡ��޸�
 * <p>2������CLOB���롢���µĻ�������
 * <p>2.1������/���£����Ǽ���JDBC3.0
 * <p>2.2��ʹ��JDBC4.0����
 * @author WangYanCheng
 * @version 2012-8-30
 */
public class OracleCLOBTest {
    /** �����ݿ������ */
    private Connection conn;
    /** ���Ա��� */
    private String tableName = "CLOB_TABLE";

    /**
     * ���캯��
     * @param conn {@link Connection}
     */
    public OracleCLOBTest(Connection conn) {
        this.conn = conn;
    }
    /**
     * ���Բ���
     * <p>1��ע��Oracle��JDBC����Ϊojdbc6.jar
     * <p>2������clob.setString��ʽ�����ݷǳ����ʱ��ĳ�������ķ�ʽ
     * @throws SQLException
     */
    public void insertDateNew() throws SQLException {
        String sql = "INSERT INTO ".concat(tableName).concat(" (ID,CONTENT, BLOB_CONTENT) VALUES(?,?,?)");
        PreparedStatement pstmt = null;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sql);
            for (int i = 1; i < 50; i++) {
                pstmt.setString(1, String.valueOf(i));
                Clob clob = this.conn.createClob();
                clob.setString(1, "HelloWorld " + i);
                pstmt.setClob(2, clob);
                Blob blob = this.conn.createBlob();
                OutputStream ousInst = blob.setBinaryStream(1);
                this.writeBLOBContent(ousInst);
                pstmt.setBlob(3, blob);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            this.conn.commit();
        } catch (SQLException e) {
            this.conn.rollback();
            e.printStackTrace();
        } finally {
            this.conn.setAutoCommit(true);
            DBUtils.close(pstmt);
        }
    }
    /**
     * ����������д���������
     * @param ous {@link OutputStream}
     */
    public void writeBLOBContent(OutputStream ous) {
        BufferedInputStream bisInst = null;
        byte[] buff = new byte[1024 * 1024];
        try {
            bisInst = new BufferedInputStream(FileUtils.getClassStream(this.getClass().getName(), this.getClass().getClassLoader()));
            int flag = -1;
            while ((flag = bisInst.read(buff)) != -1) {
                ous.write(buff, 0, flag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bisInst) {
                try {
                    bisInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                ous.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ���Բ���CLOB
     * <p>1�����ȿ������񣬱�֤����͸�����ͬһ���Ự��
     * <p>2����������ʱCLOB�ֶ�����������Ϊ�գ�EMPTY_CLOB��
     * <p>3��������ɺ��ѯ��������ݲ�����CLOB�е�ֵ������FOR UPDATE���ԣ�
     */
    public void insertData() throws SQLException {
        String sql = "INSERT INTO ".concat(tableName).concat(" (ID,CONTENT,BLOB_CONTENT) VALUES(?,EMPTY_CLOB(),?)");
        PreparedStatement pstmt = null;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sql);
            for (int i = 1; i < 50; i++) {
                pstmt.setString(1, String.valueOf(i));
                InputStream ins = FileUtils.getClassStream(this.getClass().getName(), this.getClass().getClassLoader());
                pstmt.setBlob(2, ins);
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            this.updateInsertCLOB();
            this.conn.commit();
        } catch (SQLException e) {
            this.conn.rollback();
            e.printStackTrace();
        } finally {
            this.conn.setAutoCommit(true);
            DBUtils.close(pstmt);
        }
    }

    /**
     * ֧��JDBC4.0֮ǰ�������CLOB
     * @param pstmt {@link PreparedStatement}
     * @throws SQLException
     */
    private void updateInsertCLOB() throws SQLException {
        String sql = "SELECT ID,CONTENT FROM ".concat(tableName);
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = this.conn.prepareStatement(sql);
            resultSet = pstmt.executeQuery(sql);
            while (resultSet.next()) {
                Clob clobInst = resultSet.getClob(2);
                Writer writerInst = clobInst.setCharacterStream(1);
                writerInst.write("HelloWorld " + resultSet.getString(1));
                writerInst.close();
            }
        } catch (IOException e) {
            throw new SQLException(e.getCause());
        } finally {
            DBUtils.close(pstmt);
        }
    }

    /**
     * ���Բ�ѯCLOB
     */
    public void queryDate() throws SQLException {
        String sql = "SELECT * FROM ".concat(tableName);
        PreparedStatement pstmt = null;
        try {
            pstmt = this.conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<Map<String, Object>> rtnList = DBUtils.extractData(rs);
            for (Map<String, Object> valueEntity : rtnList) {
                System.out.println(valueEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(pstmt);
        }
    }

    /**
     * ���Ը���CLOB
     * <p>ע�⣬��Clob��Ϊ��������ʱ�������rs.getClob(2)����null
     * <p>������������ǲ����ȸ���ΪEMPTY_CLOB()�ٸ��³ɾ���clob����
     * <p>��jdbc6�п�ֱ�Ӳ��ø���ʱʹ��clob��ɣ�ʹ��conn����clob����ֱ�Ӵ���clob��
     */
    public void updateClobDataOld() throws SQLException {
        String sql = "SELECT * FROM ".concat(tableName).concat(" WHERE ID=? FOR UPDATE");
        PreparedStatement pstmt = null;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, "1");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Clob clobInst = rs.getClob(2);
                clobInst.truncate(0);
                Writer writerInst = clobInst.setCharacterStream(1);
                try {
                    writerInst.write("BBC");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        writerInst.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.conn.commit();
        } catch (SQLException e) {
            this.conn.rollback();
            e.printStackTrace();
        } finally {
            this.conn.setAutoCommit(true);
            DBUtils.close(pstmt);
        }
    }

    /**
     * ���Ը���CLOB
     */
    public void updateClobDataOld2() throws SQLException {
        String sql = "SELECT * FROM ".concat(tableName).concat(" WHERE ID=? FOR UPDATE");
        PreparedStatement pstmt = null;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, "1");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                oracle.sql.CLOB clobInst = (oracle.sql.CLOB) rs.getClob(2);
                clobInst.truncate(0);
                clobInst.setString(1, "Hi");
            }
            this.conn.commit();
        } catch (SQLException e) {
            this.conn.rollback();
            e.printStackTrace();
        } finally {
            this.conn.setAutoCommit(true);
            DBUtils.close(pstmt);
        }
    }

    /**
     * ���Ը���ĳ�����ĳ��clob�ֶ�ֵ
     * <p><strong>����ȷ�����м�¼ȷʵ���ڣ�����������������˷���Դ</strong>
     * @param tableName ������
     * @param clobCol clob������
     * @param keyCol ������
     * @param keyValue ����ֵ
     * @throws SQLException {@link SQLException}
     */
    public void updateCLOBDataNew(String tableName, String clobCol, String keyCol, String keyValue) throws SQLException {
        String sqlTmpl = "UPDATE @T A SET A.@CC=? WHERE A.@PK=@PV";
        sqlTmpl = sqlTmpl.replaceAll("@T", tableName).replaceAll("@CC", clobCol).replaceAll("@PK", keyCol).replaceAll("@PV", keyValue);
        PreparedStatement pstmt = null;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sqlTmpl);
            Clob clob = this.conn.createClob();
            pstmt.setClob(1, clob);
            Writer writerInst = clob.setCharacterStream(1);
            try {
                writerInst.write("HelloWorld");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writerInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pstmt.executeUpdate();
            this.conn.commit();
        } catch (SQLException sqle) {
            this.conn.rollback();
            sqle.printStackTrace();
        } finally {
            this.conn.setAutoCommit(true);
            DBUtils.close(pstmt);
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBUtils.createConn4Oracle(SysConstants.DB_URL_ORACLE);
            OracleCLOBTest clobTest = new OracleCLOBTest(conn);
            //clobTest.insertDateNew();
            //clobTest.insertData();
            //clobTest.queryDate();
            //clobTest.updateDataOld();
            //clobTest.updateDataNew();
            clobTest.updateCLOBDataNew("EDC_MESSAGE", "CONTENT", "ID", "2");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn);
        }
    }
}

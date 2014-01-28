package org.ybygjy.example;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ybygjy.util.DBUtils;
import org.ybygjy.util.FileUtils;
import org.ybygjy.util.SysConstants;

/**
 * SqlServer�ֶ�TEXT���ʹ���
 * @author WangYanCheng
 * @version 2012-8-30
 */
public class MSSqlCLOBTest {
    /** �����ݿ������ */
    private Connection conn;

    /**
     * Constructor
     */
    public MSSqlCLOBTest(Connection conn) {
        this.conn = conn;
    }

    /**
     * ���Բ�ѯ
     * <p>1��TEXT����ֱ��getString����
     * <p>2�����ǵ��������ͳһ����ѡ����getClob��ʽȡ����
     * <p>3����Ҫ��BINARY���͵Ĵ���
     * @throws SQLException
     */
    public void queryData() throws SQLException {
        String qrySql = "SELECT * FROM BLOB_TABLE A WHERE A.ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = this.conn.prepareStatement(qrySql);
            pstmt.setString(1, "1");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                InputStream ins = rs.getBinaryStream("PHOTO");
                restoreInputStream(ins);
                ins.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(pstmt);
        }
    }

    /**
     * ת��������
     * @param ins {@link InputStream}
     * @throws IOException IOException
     */
    private void restoreInputStream(InputStream ins) {
        BufferedOutputStream bosInst = null;
        byte[] buff = new byte[1024 * 1024];
        try {
            bosInst = new BufferedOutputStream(new FileOutputStream("C:\\ttt.zip"));
            int flag = -1;
            while ((flag = ins.read(buff)) != -1) {
                bosInst.write(buff, 0, flag);
            }
            bosInst.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bosInst != null) {
                try {
                    bosInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * �ַ�����ѯ
     * <p>1��ȡClob����
     * <p>2��ȡClob�����������
     * <p>3����ӡ����������
     * @throws SQLException
     */
    public void testQueryCLOB() throws SQLException {
        //String sql = "SELECT ID, CLOB_TEXT FROM BLOB_TABLE A WHERE A.CLOB_TEXT IS NOT NULL";
        String sql = "SELECT A.ID,A.CONTENT FROM EDC_MESSAGE A";
        PreparedStatement pstmt = null;
        try {
            pstmt = this.conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Clob clobInst = rs.getClob(2);
                Reader readerInst = clobInst.getCharacterStream();
                char[] charBuff = new char[4*1024];
                int flag = -1;
                try {
                    while ((flag = readerInst.read(charBuff)) != -1) {
                        System.out.print(new String(charBuff,0,flag));
                    }
                    System.out.println();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != readerInst) {
                        try {
                            readerInst.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != pstmt) {
                DBUtils.close(pstmt);
            }
        }
    }

    /**
     * �ַ�������
     * @throws SQLException
     */
    public void testInsertCLOB() throws SQLException {
        String sql = "INSERT INTO BLOB_TABLE(ID, CLOB_TEXT) VALUES(?,?)";
        PreparedStatement pstmt = null;
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sql);
            char[] charBuff = new char[4*1024];
            for (int i = 100; i < 105; i++) {
                pstmt.setString(1, String.valueOf(i));
                Clob clobInst = this.conn.createClob();
                pstmt.setClob(2, clobInst);
                InputStreamReader isrInst = null;
                Writer writerInst = clobInst.setCharacterStream(1);
                try {
                    isrInst = new InputStreamReader(FileUtils.getClassStream(this.getClass().getName(), this.getClass().getClassLoader()));
                    int flags = -1;
                    while ((flags = isrInst.read(charBuff)) != -1) {
                        writerInst.write(charBuff, 0, flags);
                    }
                    writerInst.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != writerInst) {
                        try {
                            writerInst.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != isrInst) {
                        try {
                            isrInst.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                pstmt.addBatch();
            }
            int[] rtnEFlags = pstmt.executeBatch();
            for (int rtnEFlag : rtnEFlags) {
                System.out.println(rtnEFlag);
            }
            this.conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.conn.setAutoCommit(true);
            DBUtils.close(pstmt);
        }
    }

    /**
     * �ֽ�������
     * @throws SQLException
     */
    public void testInsertBinary() throws SQLException {
        String sql = "INSERT INTO BLOB_TABLE(ID,PHOTO,CONTEXT) VALUES(?,?,0x00)";
        PreparedStatement pstmt = null;
        InputStream[] insArr = createInputStream();
        try {
            this.conn.setAutoCommit(false);
            pstmt = this.conn.prepareStatement(sql);
            for (int i = 0; i < 10; i++) {
                pstmt.setString(1, String.valueOf(i));
                pstmt.setBinaryStream(2, insArr[i]);
                pstmt.addBatch();
            }
            int[] rsFlags = pstmt.executeBatch();
            for (int rsFlag : rsFlags) {
                System.out.println(rsFlag);
            }
            this.conn.commit();
        } catch (SQLException e) {
            this.conn.rollback();
            e.printStackTrace();
        } finally {
            this.conn.setAutoCommit(true);
            DBUtils.close(pstmt);
            closeInputStream(insArr);
        }
    }

    /**
     * �ر�������
     * @param insArr ������
     */
    private void closeInputStream(InputStream[] insArr) {
        for (InputStream ins : insArr) {
            try {
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ϊ����׼��10������������
     * @return insArr ��������
     */
    private InputStream[] createInputStream() {
        InputStream[] insArr = new InputStream[10];
        for (int i = 0; i < 10; i++) {
            try {
                insArr[i] = new FileInputStream("f:\\WabacusDemo.zip");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return insArr;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBUtils.createConn4MSSql(SysConstants.DB_URL_SQLSERVER);
            MSSqlCLOBTest mssTInst = new MSSqlCLOBTest(conn);
            //mssTInst.testInsertCLOB();
            mssTInst.testQueryCLOB();
            // mssTInst.queryDate();
            // mssTInst.testInsert();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn);
        }
    }
}

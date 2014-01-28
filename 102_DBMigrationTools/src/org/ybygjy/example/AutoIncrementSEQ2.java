package org.ybygjy.example;

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

import org.ybygjy.MessageListener;
import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.FileUtils;
import org.ybygjy.util.SysConstants;

/**
 * ���ݱ�������������У������²��裺
 * <p>
 * 1�������ݿ��е�����
 * <p>
 * 2����������������ȡ������
 * <p>
 * 3����ѯ������
 * <p>
 * 4����������ֵ
 * <h4>������Լ����</h4>
 * <p>
 * 1��Ҫ�������������й����
 * @author WangYanCheng
 * @version 2012-5-31
 */
public class AutoIncrementSEQ2 {
    /** �����ݿ������ */
    private Connection conn;
    /** ��Ϣ����*/
    private MessageListener mlInst;
    /** ǰ׺ */
    private String[] seqREGPrefix = { "^S_", "^SEQ_" };
    /** ��׺ */
    private String[] seqREGSuffix = { "_SEQ$" };
    /** Database schema */
    private String ownerSchema;
    /** Logger */
    private static Logger logger;
    static {
        logger = LoggerFactory.getInstance().getLogger(AutoIncrementSEQ2.class.getName());
    }
    /**
     * Constructor
     * @param messageListener 
     */
    public AutoIncrementSEQ2(MessageListener messageListener) {
        this.mlInst = messageListener;
    }
    /**
     * Constructor
     * @param conn �����ݿ������
     */
    public AutoIncrementSEQ2(Connection conn, String ownerSchema) {
        this.conn = conn;
        this.ownerSchema = ownerSchema;
    }

    /**
     * �������
     * @param tableName ������
     * @return rtnNums���������/0�������ڻ��������
     */
    public int qryTableNums(String tableName) {
        String tmplSQL = "SELECT COUNT(1) CC FROM ".concat(tableName);
        int rtnNums = 0;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(tmplSQL);
            if (rs.next()) {
                rtnNums = rs.getInt(1);
            }
        } catch (Exception e) {
            rtnNums = SysConstants.DB_SEQ_DEFVAL;
            logger.log(Level.WARNING, "�������ʧ�ܣ�".concat(tmplSQL), e);
        } finally {
            DBUtils.close(rs);
            DBUtils.close(stmt);
        }
        return rtnNums;
    }

    /**
     * ������
     * @return rtnSEQArr/null
     */
    public String[] qrySEQ() {
        String qrySEQ = "SELECT SEQUENCE_NAME, INCREMENT_BY FROM USER_SEQUENCES A WHERE NOT EXISTS (SELECT 1 FROM USER_DEPENDENCIES B WHERE B.REFERENCED_OWNER = ? AND B.TYPE = 'TRIGGER' AND B.REFERENCED_TYPE = 'SEQUENCE' AND B.REFERENCED_NAME = A.SEQUENCE_NAME)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String[] rtnArray = null;
        try {
            pstmt = conn.prepareStatement(qrySEQ);
            pstmt.setString(1, this.ownerSchema);
            rs = pstmt.executeQuery();
            List<String> tmpList = new ArrayList<String>();
            while (rs.next()) {
                tmpList.add(rs.getString(1));
                tmpList.add(rs.getString(2));
            }
            rtnArray = tmpList.toArray(new String[1]);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "������ʧ��", e);
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
        return rtnArray;
    }

    /**
     * ������ȡ������
     * @param seqArray ���м�
     * @return tableNameArray �����Ƽ���
     */
    public String[] analyseTableName(String[] seqArray) {
        String[] tableNameArray = new String[seqArray.length];
        for (int index = tableNameArray.length; index >= 0; index--) {
            String seqName = seqArray[index];
            tableNameArray[index] = analyseTableName(seqName);
            System.out.println("��".concat(tableNameArray[index]).concat(":").concat(seqName));
        }
        return tableNameArray;
    }

    /**
     * ��������
     * @param seqName ��������
     * @param newSeqValue ������ֵ
     * @param oldSeqValue <strong>ԭʼ��������ֵ</strong>
     */
    public void resetSEQ(String seqName, int newSeqValue, int oldSeqValue) {
        String tmpSql1 = "ALTER SEQUENCE @SEQ INCREMENT BY @V";
        String tmpSql2 = "SELECT @SEQ.nextval from dual";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(tmpSql1.replaceFirst("@SEQ", seqName).replaceFirst("@V",
                String.valueOf(newSeqValue - 1)));
            stmt.execute(tmpSql2.replaceFirst("@SEQ", seqName));
            stmt.execute(tmpSql1.replaceFirst("@SEQ", seqName).replaceFirst("@V",
                String.valueOf(oldSeqValue)));
            logger.info("����������ɣ�".concat(seqName).concat("��").concat(String.valueOf(oldSeqValue)).concat(">>")
                .concat(String.valueOf(newSeqValue)));
        } catch (Exception e) {
            logger.log(Level.WARNING,
                "��������ʧ�ܣ�".concat(seqName).concat("��").concat(String.valueOf(newSeqValue)).concat("��")
                    .concat(String.valueOf(oldSeqValue)), e);
        } finally {
            DBUtils.close(stmt);
        }
    }

    /**
     * ��ȡ������
     * @param seqName ��������
     * @return tableName ������
     */
    private String analyseTableName(String seqName) {
        String tableName = null;
        for (String tmpStr : seqREGPrefix) {
            tableName = tableName == null ? seqName.replaceFirst(tmpStr, "") : tableName.replaceFirst(
                tmpStr, "");
        }
        for (String tmpStr : seqREGSuffix) {
            tableName = tableName == null ? seqName.replaceFirst(tmpStr, "") : tableName.replaceFirst(
                tmpStr, "");
        }
        return tableName;
    }

    /**
     * �������(Ӳ����)
     * @param seqArray ���д�
     */
    public void doWork(String[] seqArray) {
        int index = seqArray.length - 1;
        for (; index >= 0; index--) {
            if (index % 2 == 0) {
                String seqName = seqArray[index];
                String tableName = analyseTableName(seqName);
                int tableNums = qryTableNums(tableName);
                tableNums = tableNums <= 0 ? 1 : tableNums;
                resetSEQ(seqName, tableNums * 2, Integer.parseInt(seqArray[index + 1]));
            }
        }
    }
    /**
     * �������(��ͨ)
     * @param seqArray ���д�
     */
    public void doWorkCommon(String[] seqArray) {
        int index = seqArray.length - 1;
        for (; index >= 0; index--) {
                String seqName = seqArray[index];
                String tableName = analyseTableName(seqName);
                int tableNums = qryTableNums(tableName);
                tableNums = tableNums <= 0 ? 1 : tableNums;
                resetSEQ(seqName, tableNums * 2, 1);
        }
    }

    /**
     * @return the mlInst
     */
    public MessageListener getMessageListener() {
        return mlInst;
    }

    /**
     * @param mlInst the mlInst to set
     */
    public void setMessageListener(MessageListener mlInst) {
        this.mlInst = mlInst;
    }

    /**
     * ����ʵ��
     * @param connURL �����ݿ������
     * @param seqPath ����·��
     * @return ais2Inst {@link AutoIncrementSEQ2}
     */
    public void doWork(String connURL, String seqPath) {
        if (this.getMessageListener() != null) {
            this.getMessageListener().beforeListener();
        }
        File dirInst = new File(seqPath);
        if (dirInst == null || !dirInst.exists()) {
            logger.log(Level.WARNING, "����Ŀ¼��ַ���� ".concat(seqPath));
        }
        String[] fileNames = FileUtils.fetchFileName(dirInst);
        Connection conn = null;
        try {
            conn = DBUtils.createConn4Oracle(connURL);
            String ownerSchema = DBUtils.getSchema(conn);
            if (ownerSchema != null) {
                AutoIncrementSEQ2 ais2Inst = new AutoIncrementSEQ2(conn, ownerSchema);
                ais2Inst.doWorkCommon(fileNames);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn);
        }
        if (this.getMessageListener() != null) {
            this.getMessageListener().afterListener();
        }
    }
}

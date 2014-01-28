package org.ybygjy.meta.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.ybygjy.ctx.MigrationContextFactory;
import org.ybygjy.meta.model.ConstraintColumnMeta;
import org.ybygjy.meta.model.ConstraintMeta;
import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.meta.model.FieldType;
import org.ybygjy.meta.model.TableMeta;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.SysConstants;

/**
 * Meta����Oracleʵ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class MetaMgrImpl4Oracle extends AbstractMetaMgr {
    /**
     * ���캯��
     * @param conn ���Ӷ���
     */
    public MetaMgrImpl4Oracle(Connection conn) {
        super(conn);
    }

    @Override
    public TableMeta getTableMeta(String tableName) {
        String tmplSql = "SELECT A.TABLE_NAME FROM USER_TABLES A WHERE A.TABLE_NAME =?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        TableMeta tableMeta = null;
        try {
            pstmt = this.conn.prepareStatement(tmplSql);
            pstmt.setString(1, tableName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                tableMeta = new TableMeta();
                tableMeta.setTableName(tableName);
                getFieldMeta(tableMeta);
                tableMeta.addConstraints(this.getConstraints(tableMeta));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
        return tableMeta;
    }

    @Override
    public Map<String, TableMeta> getAllTableMeta() {
        String tmplSql = "SELECT A.TABLE_NAME FROM USER_TABLES A";
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, TableMeta> rtnMap = new HashMap<String, TableMeta>();
        try {
            stmt = this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(tmplSql);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                TableMeta tableMeta = new TableMeta();
                tableMeta.setTableName(tableName);
                getFieldMeta(tableMeta);
                rtnMap.put(tableName, tableMeta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs);
            DBUtils.close(stmt);
        }
        return rtnMap;
    }

    /**
     * ȡFieldMeta
     * @param tableMeta {@link TableMeta}
     * @throws SQLException {@link SQLException}
     */
    public void getFieldMeta(TableMeta tableMeta) throws SQLException {
        String sql = "SELECT A.COLUMN_NAME, A.DATA_TYPE, A.NULLABLE, A.DATA_LENGTH  FROM USER_TAB_COLUMNS A WHERE A.TABLE_NAME = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, tableMeta.getTableName());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FieldMeta fmInst = new FieldMeta();
                fmInst.setFieldCode(rs.getString("COLUMN_NAME"));
                fmInst.setFieldTypeStr(rs.getString("DATA_TYPE"));
                fmInst.setFieldType(mappingFieldType(rs.getString("DATA_TYPE")));
                fmInst.setDataLength(rs.getInt("DATA_LENGTH"));
                tableMeta.addField(fmInst);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
    }

    /**
     * ӳ���ֶ�����
     * @param fieldType �����ַ���
     * @return rtnFieldType {@link FieldType}
     */
    private FieldType mappingFieldType(String fieldType) {
        FieldType rtnFieldType = FieldType.mappingType4Oracle(fieldType);
        return rtnFieldType == null ? FieldType.STR : rtnFieldType;
    }

    @Override
    public void disableTableConstraint(String tableName, String consStr) {
        String tmplSql = "ALTER TABLE @T DISABLE CONSTRAINT @C CASCADE";
        Statement pstmt = null;
        try {
            tmplSql = tmplSql.replaceAll("@T", tableName).replaceAll("@C", consStr);
            pstmt = this.conn.createStatement();
            pstmt.execute(tmplSql);
        } catch (Exception e) {
            logger.warning("����Լ������".concat(e.getMessage()).concat("����DLL��䣺").concat(tmplSql));
        } finally {
            DBUtils.close(pstmt);
        }
    }

    @Override
    public void disableTableConstraint(String[] tableNames) {
        for (String tableName : tableNames) {
            String[] consArr = getTableConstraints(tableName);
            if (null == consArr) {
                logger.warning("��".concat(tableName).concat("û����֮������Լ����"));
                continue;
            }
            logger.info("��ʼ���ñ�".concat(tableName).concat("��Լ������").concat(String.valueOf(consArr.length))
                .concat("��"));
            for (String consStr : consArr) {
                disableTableConstraint(tableName, consStr);
            }
            logger.info("��ɽ��ñ�".concat(tableName).concat("��Լ����"));
        }
    }

    @Override
    public void enableTableConstraint(String tableName, String consStr) {
        String tmplSql = "ALTER TABLE @T ENABLE CONSTRAINT @C";
        PreparedStatement pstmt = null;
        try {
            tmplSql = tmplSql.replace("@T", tableName).replaceAll("@C", consStr);
            pstmt = this.conn.prepareStatement(tmplSql);
            pstmt.execute();
        } catch (Exception e) {
            logger.warning("����Լ������".concat(e.getMessage()).concat("����DLL��䣺").concat(tmplSql));
            MigrationContextFactory.getInstance().getCtx()
                .appendSortedAttr(SysConstants.CTX_SQL_DISCONST, tmplSql);
        } finally {
            DBUtils.close(pstmt);
        }
    }

    @Override
    public void enableTableConstraint(String[] tableNames) {
        for (String tableName : tableNames) {
            String[] consArr = getTableConstraints(tableName);
            if (null == consArr) {
                logger.warning("��".concat(tableName).concat("û����֮������Լ����"));
                continue;
            }
            logger.info("��ʼ���ñ�".concat(tableName).concat("��Լ������").concat(String.valueOf(consArr.length))
                .concat("��"));
            for (String consStr : consArr) {
                enableTableConstraint(tableName, consStr);
            }
            logger.info("������ñ�".concat(tableName).concat("��Լ����"));
        }
    }

    @Override
    public String[] getTableConstraints(String tableName) {
        String[] rtnConsArr = null;
        String tmplSql = "SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS WHERE TABLE_NAME = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = this.conn.prepareStatement(tmplSql);
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
            e.printStackTrace();
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
        return rtnConsArr;
    }

    @Override
    public void disableTableConstraint(String tableName) {
        String[] consArr = getTableConstraints(tableName);
        if (consArr != null) {
            for (String consStr : consArr) {
                disableTableConstraint(tableName, consStr);
            }
        }
    }

    @Override
    public void enableTableConstraint(String tableName) {
        this.logger.info("���ñ�".concat(tableName).concat("Լ�� ��ʼ��"));
        String[] consArr = getTableConstraints(tableName);
        if (consArr != null) {
            for (String consStr : consArr) {
                enableTableConstraint(tableName, consStr);
            }
        }
        this.logger.info("���ñ�".concat(tableName).concat("Լ�� ������"));
    }

    @Override
    public List<ConstraintMeta> getConstraints(TableMeta tableMeta) {
        String sqlTMPL = "SELECT A.CONSTRAINT_NAME, A.CONSTRAINT_TYPE, A.STATUS FROM USER_CONSTRAINTS A WHERE A.TABLE_NAME=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ConstraintMeta> rtnList = new ArrayList<ConstraintMeta>();
        try {
            pstmt = this.conn.prepareStatement(sqlTMPL);
            pstmt.setString(1, tableMeta.getTableName());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ConstraintMeta ocInst = new ConstraintMeta();
                ocInst.setConstraintName(rs.getString(1));
                ocInst.setTableMeta(tableMeta);
                ocInst.setConstraintType(ConstraintMeta.ConstraintType.getConstraintType(rs.getString(2)));
                ocInst.setEnable("ENABLED".equals(rs.getString(3)));
                rtnList.add(ocInst);
                ocInst.setConstraintColumn(this.getConstraintsColumns(ocInst));
            }
        } catch (SQLException e) {
            this.logger.warning(e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
        return rtnList;
    }

    @Override
    public List<ConstraintColumnMeta> getConstraintsColumns(ConstraintMeta cmInst) {
        String sqlTMPL = "SELECT A.COLUMN_NAME FROM USER_CONS_COLUMNS A WHERE A.CONSTRAINT_NAME =? AND A.TABLE_NAME =? ORDER BY A.POSITION";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ConstraintColumnMeta> rtnList = new ArrayList<ConstraintColumnMeta>();
        try {
            pstmt = this.conn.prepareStatement(sqlTMPL);
            pstmt.setString(1, cmInst.getConstraintName());
            pstmt.setString(2, cmInst.getTableMeta().getTableName());
            rs = pstmt.executeQuery();
            Map<String, FieldMeta> fieldMetas = cmInst.getTableMeta().getFieldMap();
            while (rs.next()) {
                ConstraintColumnMeta ccmInst = new ConstraintColumnMeta();
                ccmInst.setCmInst(cmInst);
                ccmInst.setTableMeta(cmInst.getTableMeta());
                ccmInst.setFieldMeta(fieldMetas.get(rs.getString(1)));
                rtnList.add(ccmInst);
            }
        } catch (SQLException e) {
            this.logger.warning(e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
        return rtnList;
    }

    @Override
    public void resetTableSequence(String tableName) {
        //TODO �˴��߼������ع�Ϊ��������
        String sqlTMPL = "SELECT B.REFERENCED_NAME,C.INCREMENT_BY,C.LAST_NUMBER FROM USER_DEPENDENCIES A INNER JOIN USER_DEPENDENCIES B ON A.NAME = B.NAME AND B.REFERENCED_TYPE = 'SEQUENCE' LEFT JOIN USER_SEQUENCES C ON C.SEQUENCE_NAME = B.REFERENCED_NAME WHERE A.REFERENCED_NAME= ? AND A.TYPE='TRIGGER' AND A.REFERENCED_TYPE = 'TABLE'";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sqlTMPL);
            pstmt.setString(1, tableName);
            rs = pstmt.executeQuery();
            long rowNums = queryTableRowNums(tableName);
            while (rs.next()) {
                //��ñ������,����������������
                String seqName = rs.getString(1);
                long incV = rs.getLong(2);
                long lasV = rs.getLong(3);
                //��������������������ֵС�����ʱ����������
                if (rowNums <= lasV) {
                    continue;
                }
                innerResetSequence(seqName, incV, rowNums);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "��������ʧ��", e);
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
    }

    /**
     * ������������
     * @param tableName ������
     * @return rtnNums �����������
     */
    private long queryTableRowNums(String tableName) {
        String sqlTmpl = "SELECT COUNT(1) FROM ".concat(tableName);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long rtnNums = 0;
        try {
            pstmt = conn.prepareStatement(sqlTmpl);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rtnNums = rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "��ѯ����������", e);
        } finally {
            DBUtils.close(rs);
            DBUtils.close(pstmt);
        }
        return rtnNums;
    }

    /**
     * ��������ֵ�ڲ��߼�
     * <p>��������˼·����������
     * <ol>
     * <li>�������е���ֵ(newV-1)
     * <li>��������ˢ�����е�ǰֵ
     * <li>��ԭ���е���ֵ
     * </ol>
     * @param seqName ��������
     * @param oldV ԭ����ֵ
     * @param newV �µ�ֵֵ
     * @return rtnFlag true/false
     */
    private boolean innerResetSequence(String seqName, long oldV, long newV) {
        boolean rtnFlag = false;
        Statement stmt = null;
        String sqlTMP1 = "ALTER SEQUENCE ".concat(seqName).concat(" INCREMENT BY @V");
        String sqlTMP2 = "SELECT ".concat(seqName).concat(".nextval from dual");
        try {
            stmt = conn.createStatement();
            stmt.execute(sqlTMP1.replaceAll("@V", String.valueOf(newV - 1)));
            stmt.execute(sqlTMP2);
            stmt.execute(sqlTMP1.replaceAll("@V", String.valueOf(oldV)));
        } catch (SQLException e) {
            logger.log(Level.WARNING, "��������ֵ�ڲ��߼�", e);
        } finally {
            DBUtils.close(stmt);
        }
        return rtnFlag;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:NSTCSA2442/725382@192.168.3.232:1521/NSDEV";
        Connection conn = DBUtils.createConn4Oracle(url);
        MetaMgrImpl4Oracle mmioInst = new MetaMgrImpl4Oracle(conn);
        mmioInst.disableTableConstraint("FBCM_APP_DETAIL", "FK_APP_DETAIL_REF_CED_APP");
        // Map<String, TableMeta> rtnMap = mmioInst.fetchTableMetaMap();
        // System.out.println(rtnMap.size());
        DBUtils.close(conn);
    }
}

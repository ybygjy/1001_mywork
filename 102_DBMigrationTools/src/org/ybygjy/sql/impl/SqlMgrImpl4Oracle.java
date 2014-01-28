package org.ybygjy.sql.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.meta.model.ConstraintColumnMeta;
import org.ybygjy.meta.model.ConstraintMeta;
import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.meta.model.FieldType;
import org.ybygjy.meta.model.TableMeta;
import org.ybygjy.sql.SqlMgr;
import org.ybygjy.sql.model.SqlModel;


/**
 * SQLMgr Oracleʵ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class SqlMgrImpl4Oracle implements SqlMgr {
    /**{@link Logger}*/
    private Logger logger;
    /**
     * Constructor
     */
    public SqlMgrImpl4Oracle() {
        logger = LoggerFactory.getInstance().getLogger(this.getClass().getName());
    }
    @Override
    public SqlModel buildQrySQL(TableMeta tableMeta) {
        SqlModel sqlModel = new SqlModel();
        sqlModel.setTableMeta(tableMeta);
        AnalyseSql asInst = new AnalyseSql(tableMeta);
        sqlModel.setSqlStmt(asInst.analyseQuerySql());
        sqlModel.setSelectFields(asInst.getFieldMetaArr());
        return sqlModel;
    }

    @Override
    public SqlModel buildInsSQL(TableMeta tableMeta) {
        AnalyseSql asInst = new AnalyseSql(tableMeta);
        SqlModel rtnSM = new SqlModel();
        rtnSM.setTableMeta(tableMeta);
        rtnSM.setSqlStmt(asInst.analyseInsertSql());
        rtnSM.setSelectFields(asInst.getFieldMetaArr());
        return rtnSM;
    }

    @Override
    public Map<String, SqlModel> buildQrySQL(List<TableMeta> tableMetaes) {
        Map<String, SqlModel> rtnMap = new HashMap<String, SqlModel>();
        for (TableMeta tmInst : tableMetaes) {
            rtnMap.put(tmInst.getTableName(), this.buildQrySQL(tmInst));
        }
        return rtnMap;
    }

    @Override
    public Map<String, SqlModel> buildInsSQL(List<TableMeta> tableMetaList) {
        Map<String, SqlModel> rtnMap = new HashMap<String, SqlModel>();
        for (Iterator<TableMeta> iterator = tableMetaList.iterator(); iterator.hasNext();) {
            TableMeta tableMeta = iterator.next();
            rtnMap.put(tableMeta.getTableName(), buildInsSQL(tableMeta));
        }
        return rtnMap;
    }

    @Override
    public SqlModel buildQryClobSQL(TableMeta tableMeta) {
        if (!tableMeta.hasSpecialType()) {
            this.logger.warning("�� ".concat(tableMeta.getTableName()).concat(" û�����������ֶΡ�"));
            return null;
        }
        //ȡ����Լ��
        ConstraintMeta pkCons = tableMeta.getPrimaryConstraint();
        if (null == pkCons) {
            this.logger.warning("��  ".concat(tableMeta.getTableName()).concat(" û������Լ����"));
            return null;
        }
        //����SELECT @������,@Clob���Ͷ� FROM ������
        String sqlTMPL = "SELECT @S FROM @T";
        List<FieldMeta> selectFields = new ArrayList<FieldMeta>();
        StringBuffer sbud = new StringBuffer();
        for (ConstraintColumnMeta ccmInst : pkCons.getConstraintColumn()) {
            sbud.append(ccmInst.getFieldMeta().getFieldCode()).append(",");
        }
        //ȡ�ֶ���
        for (FieldMeta fm : tableMeta.getSpecialTypeColumns()) {
            sbud.append(fm.getFieldCode()).append(",");
        }
        sbud.setLength(sbud.length() - 1);
        sqlTMPL = sqlTMPL.replaceAll("@S", sbud.toString()).replaceAll("@T", tableMeta.getSrcTableName());
        SqlModel rtnSqlModel = new SqlModel();
        rtnSqlModel.setSqlStmt(sqlTMPL);
        rtnSqlModel.setTableMeta(tableMeta);
        rtnSqlModel.setSelectFields(selectFields.toArray(new FieldMeta[selectFields.size()]));
        return rtnSqlModel;
    }

    @Override
    public SqlModel buildInsertClobSQL(TableMeta tableMeta) {
        //�����飺һ���ǲ�ѯ���·�ʽ;һ���Ǹ��´�Clob������ʽ
        //TODO ���԰�������һ��˳���Ƚ�����select���ֶκ�where���ֶ�,Ȼ��ͳһ�����滻������������������ѭ����
        //��������
        if (tableMeta.getSpecialTypeCounts() <= 0) {
            return null;
        }
        String tmplSql = "UPDATE @T A SET @CSTR WHERE @PSTR";
        //ȡ����Լ��
        ConstraintMeta cmInst = tableMeta.getPrimaryConstraint();
        if (cmInst == null) {
            logger.info("��".concat(tableMeta.getTableName()).concat(" ����������Լ�����޷����´��ֶ�!"));
            return null;
        }
        //�洢SQL�������
        List<FieldMeta> whereFields = new ArrayList<FieldMeta>();
        List<ConstraintColumnMeta> ccmInstArray = cmInst.getConstraintColumn();
        StringBuilder sbud = new StringBuilder();
        //ƴ��SQL��������PK_1=? AND PK_2=?
        for (ConstraintColumnMeta tmpCCM : ccmInstArray) {
            whereFields.add(tmpCCM.getFieldMeta());
            sbud.append("A.").append(tmpCCM.getFieldMeta().getFieldCode()).append("=").append("? AND ");
        }
        sbud.setLength(sbud.length() - 4);
        tmplSql = tmplSql.replaceFirst("@PSTR", sbud.toString());
        sbud.setLength(0);
        List<FieldMeta> fmArray = tableMeta.getSpecialTypeColumns();
        List<FieldMeta> selectFields = new ArrayList<FieldMeta>();
        for (FieldMeta fmInst : fmArray) {
            if (fmInst.getFieldType() == FieldType.CLOB || fmInst.getFieldType() == FieldType.NCLOB) {
                selectFields.add(fmInst);
                sbud.append("A.").append(fmInst.getFieldCode()).append("=?,");
            }
        }
        //˵�����е����������ֶβ���CLOB
        if (sbud.length() == 0) {
            return null;
        }
        sbud.setLength(sbud.length() - 1);
        tmplSql = tmplSql.replaceFirst("@CSTR", sbud.toString()).replaceFirst("@T", tableMeta.getTargetTableName());
        SqlModel sqlModel = new SqlModel();
        sqlModel.setSqlStmt(tmplSql);
        sqlModel.setTableMeta(tableMeta);
        sqlModel.setSelectFields(selectFields.toArray(new FieldMeta[selectFields.size()]));
        sqlModel.setWhereFields(whereFields.toArray(new FieldMeta[whereFields.size()]));
        return sqlModel;
    }
}

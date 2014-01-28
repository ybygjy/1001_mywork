package org.ybygjy.sql.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.meta.model.ConstraintColumnMeta;
import org.ybygjy.meta.model.ConstraintMeta;
import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.meta.model.TableMeta;
import org.ybygjy.sql.SqlMgr;
import org.ybygjy.sql.model.SqlModel;


/**
 * SQLMgr MSSqlʵ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class SqlMgrImpl4MSSql implements SqlMgr {
    /**��־*/
    private Logger logger;
    /**
     * Constructor
     */
    public SqlMgrImpl4MSSql() {
        logger = LoggerFactory.getInstance().getLogger(this.getClass().getName());
    }

    @Override
    public SqlModel buildQrySQL(TableMeta tableMeta) {
        SqlModel smInst = new SqlModel();
        smInst.setTableMeta(tableMeta);
        AnalyseSql asInst = new AnalyseSql(tableMeta);
        smInst.setSqlStmt(asInst.analyseQuerySql());
        smInst.setSelectFields(asInst.getFieldMetaArr());
        return smInst;
    }

    @Override
    public SqlModel buildInsSQL(TableMeta tableMeta) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, SqlModel> buildQrySQL(List<TableMeta> tableMeta) {
        Map<String, SqlModel> rtnMap = new HashMap<String, SqlModel>();
        for (Iterator<TableMeta> iterator = tableMeta.iterator(); iterator.hasNext();) {
            TableMeta tmInst = iterator.next();
            rtnMap.put(tmInst.getTableName(), buildQrySQL(tmInst));
        }
        return rtnMap;
    }

    @Override
    public Map<String, SqlModel> buildInsSQL(List<TableMeta> tableMeta) {
        return null;
    }

    @Override
    public SqlModel buildQryClobSQL(TableMeta tableMeta) {
        String sqlTMPL = new String("SELECT @S FROM @T A");
        //ȡ����Լ��
        ConstraintMeta pkConst = tableMeta.getPrimaryConstraint();
        if (null == pkConst) {
            logger.log(Level.ALL, "�����������ڣ����ܲ���Clob��������!");
            throw new RuntimeException("�����������ڣ����ܲ���Clob��������!");
        }
        if (!tableMeta.hasSpecialType()) {
            logger.log(Level.ALL, "���в��������������ֶΣ����ܲ���Clob��������!");
            throw new RuntimeException(tableMeta.getTableName().concat("���в��������������ֶΣ����ܲ���Clob��������!"));
        }
        //Լ���ֶ�
        List<ConstraintColumnMeta> constraintColumns  = pkConst.getConstraintColumn();
        StringBuilder qryColumns = new StringBuilder();
        //SQL����е���(����,��1,��2)
        List<FieldMeta> fieldList = new ArrayList<FieldMeta>();
        //����Լ����
        for (ConstraintColumnMeta ccmInst : constraintColumns) {
            fieldList.add(ccmInst.getFieldMeta());
            qryColumns.append(ccmInst.getFieldMeta().getFieldCode()).append(",");
        }
        //�������������
        for (FieldMeta fmInst : tableMeta.getSpecialTypeColumns()) {
            fieldList.add(fmInst);
            qryColumns.append(fmInst.getFieldCode()).append(",");
        }
        qryColumns.setLength(qryColumns.length() - 1);
        sqlTMPL = sqlTMPL.replaceAll("@S", qryColumns.toString()).replaceAll("@T", tableMeta.getTableName());
        SqlModel rtnSM = new SqlModel();
        rtnSM.setTableMeta(tableMeta);
        rtnSM.setSelectFields(fieldList.toArray(new FieldMeta[fieldList.size()]));
        rtnSM.setSqlStmt(sqlTMPL);
        return rtnSM;
    }

    @Override
    public SqlModel buildInsertClobSQL(TableMeta tableMeta) {
        //TODO ���մ�����ֶβ�ѯSQL�Ĵ���
        //TODO ע�����ִ�в�����ֶεĴ�����Բ���ͬ
        //TODO 1��JDBC3���ò�ѯ���·�ʽ��Ҫ�ǲ�֧��Connection#createClob
        //TODO 2��JDBC4������ø��´���Clob�������
        return null;
    }
}

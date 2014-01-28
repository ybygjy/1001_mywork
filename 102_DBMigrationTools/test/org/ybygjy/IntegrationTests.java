package org.ybygjy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ybygjy.ctx.MigrationContextFactory;
import org.ybygjy.exec.SqlExecutorListener;
import org.ybygjy.logger.LoggerFactory;
import org.ybygjy.meta.model.TableMeta;
import org.ybygjy.sql.model.SqlModel;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.SysConstants;
import org.ybygjy.util.TableUtils;

/**
 * ���ɲ���
 * @author WangYanCheng
 * @version 2012-10-16
 */
public class IntegrationTests {
    /** ��Oracle���ݿ������ */
    private Connection targetConnection;
    /** ��SQLServer���ݿ������ */
    private Connection srcConnection;
    /** ���Ի��������Դʵ�� */
    protected TestEnv testEnvInst;
    /** ��־ */
    protected Logger logger;
    /** ģ������*/
    private String moduleName;
    @Before
    public void startUp() {
        initLogger();
        srcConnection = getSrcConnection();
        targetConnection = getTargetConnection();
        logger = LoggerFactory.getInstance().getLogger(getClass().getName());
        doBuildEnv();
    }

    /**
     * �������Ի���
     */
    protected void doBuildEnv() {
        testEnvInst = new TestEnv();
        switch (getTestModel()) {
            case MSSQL_ORA:
                testEnvInst.buildMSSql2Oracle(srcConnection, targetConnection);
                break;
            case ORA_ORA:
                testEnvInst.buildOracle2Oracle(srcConnection, targetConnection);
                break;
        }
    }

    @Test
    public void doWork() {
        logger.info("��ʼ��ͨ���ݵ�Ǩ��");
        List<TableMeta> tableMetaes = new ArrayList<TableMeta>();
        beforeMigration(getModuleName());
        for (String tableName : getTableName()) {
            logger.info("��ʼǨ�Ʊ�".concat(tableName));
            TableMeta tableMeta = getCommonTableMeta(tableName);
            if (null == tableMeta) {
                continue;
            }
            if (beforeMigrationTable(getModuleName(), tableMeta)) {
                doMigrationCommonData(tableMeta);
            }
            afterMigrationTable(getModuleName(), tableMeta);
            if (tableMeta.hasSpecialType()) {
                tableMetaes.add(tableMeta);
            }
            logger.info("���Ǩ�Ʊ�".concat(tableName));
        }
        showRawInsertFailInfo();
        showConstErrorInfo();
        logger.info("�����ͨ���ݵ�Ǩ��");
        logger.info("��ʼ�������ݵ�Ǩ��");
        showSPTypeInfo(tableMetaes);
        for (TableMeta tableMeta : tableMetaes) {
            logger.info("��ʼ��������Ǩ�Ʊ�".concat(tableMeta.getTableName()));
            doMigrationSpecialData(tableMeta);
            logger.info("�����������Ǩ�Ʊ�".concat(tableMeta.getTableName()));
        }
        afterMigration(getModuleName());
        logger.info("����������ݵ�Ǩ��");
    }

    /**
     * �������õı�
     * @return tableName ������
     */
    protected String[] getTableName() {
        return new String[] { "EDC_MESSAGE" };
    }

    protected Connection getSrcConnection() {
        return DBUtils.createConn4MSSql(SysConstants.DB_URL_SQLSERVER);
    }

    protected Connection getTargetConnection() {
        return DBUtils.createConn4Oracle(SysConstants.DB_URL_ORACLE);
    }

    protected TestModel getTestModel() {
        return TestModel.MSSQL_ORA;
    }
    
    /**
     * @return the moduleName
     */
    protected String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName the moduleName to set
     */
    protected void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    /**
     * ������Ǩ��ǰ
     * @param moduleName ģ�����
     * @param tableMeta �����
     * @return rtnFlag true/false
     */
    protected boolean beforeMigrationTable(String moduleName, TableMeta tableMeta) {
        String tableName = tableMeta.getTargetTableName();
        boolean rtnFlag = true;
        logger.info("���ñ�Ĵ�������".concat(tableName));
        testEnvInst.getTarMetaMgr().disableTableTriggers(tableName);
        logger.info("���ñ�Ĵ�������ɣ�".concat(tableName));
        logger.info("�����".concat(tableName));
        rtnFlag = SysConstants.TABLE_TRUNCATE_SUCCESS == TableUtils.truncateTable(targetConnection, tableName);
        logger.info("������ɣ�".concat(tableName));
        logger.info("���ñ������Լ����".concat(tableName));
        testEnvInst.getTarMetaMgr().disableTableConstraint(tableName);
        logger.info("���ñ������Լ����ɣ�".concat(tableName));
        return rtnFlag;
    }
    /**
     * ��Ǩ����ɺ�
     * @param moduleName ģ������
     * @param tableMeta �����
     */
    protected void afterMigrationTable(String moduleName, TableMeta tableMeta) {
        String tableName = tableMeta.getTargetTableName();
        logger.info("���ñ�Ĵ�������".concat(tableName));
        testEnvInst.getTarMetaMgr().enableTableTriggers(tableName);
        logger.info("���ñ�Ĵ�������ɣ�".concat(tableName));
        logger.info("���ñ������Լ����".concat(tableName));
        testEnvInst.getTarMetaMgr().enableTableConstraint(tableName);
        logger.info("���ñ������Լ����ɣ�".concat(tableName));
        logger.info("���������������У�".concat(tableName));
        testEnvInst.getTarMetaMgr().resetTableSequence(tableName);
        logger.info("�����������ı�������ɣ�".concat(tableName));
    }
    /**
     * ��ͨ����Ǩ��֮ǰ
     * @param moduleName ģ������
     * @return rtnFlag true/false
     */
    protected boolean beforeMigration(String moduleName) {
        return true;
    }

    /**
     * ��ͨ����Ǩ��֮��
     * @param moduleName ģ������
     */
    protected void afterMigration(String moduleName) {
    }

    /**
     * Ǩ����ͨ��������
     * @param tableMeta {@link TableMeta}
     */
    private void doMigrationCommonData(TableMeta tableMeta) {
        final SqlModel tarSMInst = testEnvInst.getTarSqlMgr().buildInsSQL(tableMeta);
        testEnvInst.getSrcSqlExe().addListener(new SqlExecutorListener() {
            @Override
            public void afterSpecialTypeQuery(SqlModel sqlModel, ResultSet rs) {
            }

            @Override
            public void afterQuery(SqlModel smInst, List<Map<String, Object>> dataMap) {
                testEnvInst.getTarSqlExe().executeInsert(tarSMInst, dataMap);
            }
        });
        testEnvInst.getSrcSqlExe().executeQuery(testEnvInst.getSrcSqlMgr().buildQrySQL(tableMeta));
    }

    /**
     * Ǩ��������������
     * @param tableMeta {@link TableMeta}
     */
    private void doMigrationSpecialData(TableMeta tableMeta) {
        final SqlModel stypesm4Ora = testEnvInst.getTarSqlMgr().buildInsertClobSQL(tableMeta);
        if (stypesm4Ora == null) {
            return;
        }
        testEnvInst.getSrcSTypeExe().addListener(new SqlExecutorListener() {
            @Override
            public void afterSpecialTypeQuery(SqlModel sqlModel, ResultSet rs) {
                testEnvInst.getTarSTypeExe().executeSpecialTypeInsert(sqlModel, stypesm4Ora, rs);
            }

            @Override
            public void afterQuery(SqlModel smInst, List<Map<String, Object>> dataMap) {
            }
        });
        testEnvInst.getSrcSTypeExe().executeQuerySpecialType(
            testEnvInst.getSrcSqlMgr().buildQryClobSQL(tableMeta));
    }

    /**
     * ��ӡ�������ͱ���Ϣ
     * @param tableMetas {@link TableMeta}��
     */
    private void showSPTypeInfo(List<TableMeta> tableMetas) {
        StringBuffer sbuf = new StringBuffer();
        int i = 0;
        for (TableMeta tableMeta : tableMetas) {
            sbuf.append(tableMeta.getTableName()).append("\t");
            if (i % 10 == 0) {
                sbuf.append("\t\n");
            }
        }
        logger.warning("��ӡ�������ͱ���Ϣ\t\n".concat(sbuf.toString()));
    }

    /**
     * ��ӡԼ����/���ô�����Ϣ
     */
    private void showConstErrorInfo() {
        Object tmpObj = MigrationContextFactory.getInstance().getCtx()
            .getAttribute(SysConstants.CTX_SQL_DISCONST);
        if (tmpObj != null) {
            SortedSet<String> sqlSets = (SortedSet<String>) tmpObj;
            StringBuffer sbuf = new StringBuffer();
            for (Iterator<String> iterator = sqlSets.iterator(); iterator.hasNext();) {
                sbuf.append(iterator.next()).append(";\t\n");
            }
            logger.warning("��ӡԼ����/���ô�����Ϣ\t\n".concat(sbuf.toString()));
        }
    }

    /**
     * ��ӡ�������SQL���
     */
    private void showRawInsertFailInfo() {
        Object tmpObj = MigrationContextFactory.getInstance().getCtx()
            .getAttribute(SysConstants.CTX_SQL_RAWINSERT_FAIL);
        if (tmpObj != null) {
            SortedSet<String> sqlSets = (SortedSet<String>) tmpObj;
            StringBuffer sbuf = new StringBuffer();
            for (Iterator<String> iterator = sqlSets.iterator(); iterator.hasNext();) {
                sbuf.append(iterator.next()).append(";\t\n");
            }
            logger.warning("��ӡ�������SQL���\t\n".concat(sbuf.toString()));
        }
    }

    /**
     * ȡ����{@link TableMeta}
     * @param tableName ������
     * @return rtnMeta {@link TableMeta}
     */
    protected TableMeta getCommonTableMeta(String tableName) {
        TableMeta tarTMInst = testEnvInst.getTarMetaMgr().getTableMeta(tableName);
        if (null == tarTMInst) {
            logger.info("Ŀ�Ķ�ȱ�ٱ�".concat(tableName));
            return null;
        }
        String bmsTableName = tableName.replaceAll("_PD", "");
        TableMeta srcTMInst = testEnvInst.getSrcMetaMgr().getTableMeta(bmsTableName);
        if (null == srcTMInst) {
            logger.info("Դ��ȱ�ٱ�".concat(tableName));
            return null;
        }
        TableMeta commonTM = srcTMInst.consultAndRebuilt(tarTMInst);
        //TODO ׾�ӵ����
        commonTM.setSrcTableName(bmsTableName);
        commonTM.setTargetTableName(tableName);
        return commonTM;
    }

    /**
     * ��ʼLogger
     */
    protected void initLogger() {
        try {
            Handler fileHandler = new FileHandler(getLogFile(), false);
            fileHandler.setFormatter(new SimpleFormatter());
            LoggerFactory.getInstance().addLoggerHandler(fileHandler);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ȡLog��־�洢�ļ�
     * @return rtnFilePath �ļ�����
     */
    protected String getLogFile() {
        return "./DataMigrationLog%g.log";
    }

    @After
    public void tearDown() {
        DBUtils.close(targetConnection);
        DBUtils.close(srcConnection);
    }


}

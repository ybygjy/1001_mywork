package org.ybygjy.ui;

import java.io.File;
import java.sql.Connection;
import java.util.logging.Level;

import org.ybygjy.IntegrationTests;
import org.ybygjy.MessageListener;
import org.ybygjy.TestModel;
import org.ybygjy.exp.ExpInterface4DBMS;
import org.ybygjy.exp.ExpInterface4EDC;
import org.ybygjy.exp.ExpInterface4FBCM;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.FileUtils;

/**
 * ����UI���߼�����
 * @author WangYanCheng
 * @version 2012-10-24
 */
public class UILogicAdapter extends IntegrationTests {
    /**ԭ���ݿ�*/
    private Connection srcConn;
    /**Ŀ�����ݿ�*/
    private Connection tarConn;
    /**��Ϣ���ݶ���*/
    private MessageListener messageListener;
    /**�ļ�Ŀ¼ʵ��*/
    private File dirFileInst;
    /**
     * Constructor
     * @param srcConnStr
     * @param tarConnStr
     * @param mlInst
     */
    public UILogicAdapter(String srcConnStr, String tarConnStr, MessageListener mlInst, String dirFile) {
        this.srcConn = DBUtils.createConn4Oracle(srcConnStr);
        if (null == this.srcConn) {
            logger.log(Level.WARNING, "���ݿ�����ʧ�ܣ�����ָ�������Ӵ�\r\n".concat(srcConnStr));
        }
        this.tarConn = DBUtils.createConn4Oracle(tarConnStr);
        if (null == this.tarConn) {
            logger.log(Level.WARNING, "���ݿ�����ʧ�ܣ�����ָ�������Ӵ�\r\n".concat(tarConnStr));
        }
        this.messageListener = mlInst;
        this.dirFileInst = new File(dirFile);
        super.startUp();
    }

    @Override
    protected Connection getSrcConnection() {
        return this.srcConn;
    }

    @Override
    protected Connection getTargetConnection() {
        return this.tarConn;
    }

    @Override
    public void doWork() {
        this.messageListener.beforeListener();
        super.doWork();
        super.tearDown();
        this.messageListener.afterListener();
    }

    @Override
    protected TestModel getTestModel() {
        return TestModel.ORA_ORA;
    }

    @Override
    protected String[] getTableName() {
        return FileUtils.fetchFileName(dirFileInst);
    }

    @Override
    protected String getModuleName() {
        return FileUtils.getFileName(dirFileInst);
    }

    @Override
    protected boolean beforeMigration(String moduleName) {
        super.beforeMigration(moduleName);
        if ("DBMS".equals(moduleName)) {
            new ExpInterface4DBMS(testEnvInst).beforeMigration();
        } else if ("FBCM".equals(moduleName)) {
            new ExpInterface4FBCM(testEnvInst).beforeMigration();
        } else if ("EDC".equals(moduleName)) {
            new ExpInterface4EDC(testEnvInst).beforeMigration();
        }
        return true;
    }

    @Override
    protected void afterMigration(String moduleName) {
        super.afterMigration(moduleName);
        // TODO �µ�������ĳ������ģ��Ǩ����ɺ�Ҫ������ҵ�񼶱���
        if ("DBMS".equals(moduleName)) {
            new ExpInterface4DBMS(testEnvInst).afterMigration();
        } else if ("FBCM".equals(moduleName)) {
            new ExpInterface4FBCM(testEnvInst).afterMigration();
        } else if ("EDC".equals(moduleName)) {
            new ExpInterface4EDC(testEnvInst).afterMigration();
        }
    }

    /** ���ݶ���{ģ��,������Ŀ¼} */
    private String[][] dataCollect = {/*
                                       * {"BMS", "C:\\KettleLog\\BMS"}, {"RMS",
                                       * "C:\\KettleLog\\RMS"}
                                       */{ "FBCM", "C:\\KettleLog\\ERROR" } };
}

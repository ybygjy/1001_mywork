package org.ybygjy.ora;

import java.io.File;
import java.sql.Connection;

import org.junit.Before;
import org.ybygjy.IntegrationTests;
import org.ybygjy.TestModel;
import org.ybygjy.exp.ExpInterface4DBMS;
import org.ybygjy.exp.ExpInterface4EDC;
import org.ybygjy.exp.ExpInterface4FBCM;
import org.ybygjy.util.DBUtils;
import org.ybygjy.util.FileUtils;
import org.ybygjy.util.SysConstants;

/**
 * ���ɲ���_��������_Oracle����
 * @author WangYanCheng
 * @version 2012-10-16
 */
public class IntegrationTests4Oracle extends IntegrationTests {
    /**����*/
    //private String DB_URL_ORACLE4SMS = "jdbc:oracle:thin:NSTCSA3382/045869@192.168.0.143:1521/NSDEV";
    private String DB_URL_ORACLE4SMS = "jdbc:oracle:thin:NSTCSA543/573695@192.168.0.133:1521/NSDEV";
    /**����*/
    private String DB_URL_ORACLE4FSS = "jdbc:oracle:thin:NSTCSA3381/000800@192.168.0.143:1521/NSDEV";
    @Before
    public void startUp() {
        super.startUp();
    }
    @Override
    protected Connection getSrcConnection() {
        return DBUtils.createConn4Oracle(SysConstants.DB_URL_ORACLE);
    }

    @Override
    protected Connection getTargetConnection() {
        return DBUtils.createConn4Oracle(DB_URL_ORACLE4SMS);
    }
    
    @Override
    public void doWork() {
        super.doWork();
    }

    @Override
    protected TestModel getTestModel() {
        return TestModel.ORA_ORA;
    }

    @Override
    protected String[] getTableName() {
        return FileUtils.fetchFileName(new File(dataCollect[0][1]));
    }
    
    @Override
    protected String getModuleName() {
        return dataCollect[0][0];
    }

    @Override
    protected void afterMigration(String moduleName) {
        super.afterMigration(moduleName);
        //TODO �µ�������ĳ������ģ��Ǩ����ɺ�Ҫ������ҵ�񼶱���
        if ("DBMS".equals(moduleName)) {
            new ExpInterface4DBMS(testEnvInst).afterMigration();
        } else if ("FBCM".equals(moduleName)) {
            new ExpInterface4FBCM(testEnvInst).afterMigration();
        } else if ("EDC".equals(moduleName)) {
            new ExpInterface4EDC(testEnvInst).afterMigration();
        }
    }

    /**���ݶ���{ģ��,������Ŀ¼}*/
    private String[][] dataCollect = {/*{"BMS", "C:\\KettleLog\\BMS"}, {"RMS", "C:\\KettleLog\\RMS"}*/{"FBCM", "C:\\KettleLog\\FBCM"}};
}

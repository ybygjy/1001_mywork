package org.ybygjy.dbcompare.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;

import org.apache.tools.ant.BuildException;
import org.ybygjy.dbcompare.DBCompare;
import org.ybygjy.dbcompare.DBEnum;
import org.ybygjy.dbcompare.DBUtils;


/**
 * Ant�������
 * @author WangYanCheng
 * @version 2011-10-11
 */
public class DBCompare4AntTask extends org.apache.tools.ant.Task {
    /** ���ݿ����Ӵ� */
    private String dbURL;
    /** ԭʼ�û� */
    private String srcUser;
    /** �����û� */
    private String targetUser;
    /** ת���ļ� */
    private String restoreFilePath;
    /** ���ݿ�����{ORACLE\MSSQL}*/
    private String dbType;
    /** dbTypeEnum*/
    private DBEnum dbTypeEnum;
    @Override
    public void execute() throws BuildException {
		Connection conn = innerGetConn();
        if (conn == null) {
        	return;
        }
        DBCompare dbCompare = null;
        try {
            dbCompare = new DBCompare(conn, getSrcUser(), getTargetUser());
            dbCompare.setReportCtxOutput(createRestoreOutput());
            dbCompare.setDbType(dbTypeEnum);
            dbCompare.doWork();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != dbCompare) {
                if (null != dbCompare.getReportCtxOutput()) {
                    try {
                        dbCompare.getReportCtxOutput().flush();
                        dbCompare.getReportCtxOutput().close();
                    } catch (Exception e) {
System.out.println("�ر�ת����ʧ�ܣ�����ϵ������Ա��");
                        e.printStackTrace();
                    }
                }
            }
System.out.println("�ر�����");
            DBUtils.close(conn);
        }
System.out.println("ת���ļ���".concat(getRestoreFilePath()));
    }

    /**
     * create Connection
     * @param dbURL2 ���Ӵ�
     * @return rtnConn rtnConn/null
     */
    private Connection innerGetConn() {
System.out.println("��ʼ������");
    	Connection rtnConn= null;
    	if ("MSSQL".equals(getDbType())) {
    		dbTypeEnum = DBEnum.MSSQL;
    		rtnConn = DBUtils.createConn4MSSql(getDbURL());
    	} else {
    		dbTypeEnum = DBEnum.ORA;
    		rtnConn = DBUtils.createConn4Oracle(getDbURL());
    	}
    	if (rtnConn == null) {
    		throw new RuntimeException("��ʼ������ʧ�ܣ�\n���Ӵ���".concat(getDbURL()));
    	}
System.out.println(dbURL + "\t" + this.srcUser + "\t" + this.targetUser);
System.out.println("��ʼ���������");
		return rtnConn;
	}

	/**
     * ����ת��������
     * @return rtnOUS rtnOUS
     */
    private OutputStream createRestoreOutput() {
        if (getRestoreFilePath() == null) {
            return null;
        }
        System.out.println("��ʼ����ת��������");
        File rFile = null;
        OutputStream ous = null;
        try {
            rFile = getRestoreFilePath() == null ? null : new File(getRestoreFilePath());
            if (rFile == null) {
System.out.println("����ת���ļ�ʧ��(����ʹ��ת����)���ļ���ַ:[" + getRestoreFilePath() + "]");
            } else {
                ous = new FileOutputStream(rFile, false);
System.out.println("��ɴ���ת��������ת���ļ���" + rFile.getAbsolutePath());
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ous;
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getSrcUser() {
        return srcUser;
    }

    public void setSrcUser(String srcUser) {
        this.srcUser = srcUser.toUpperCase();
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser.toUpperCase();
    }

    public String getRestoreFilePath() {
        return restoreFilePath;
    }

    public void setRestoreFilePath(String restoreFile) {
        this.restoreFilePath = restoreFile;
    }

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}

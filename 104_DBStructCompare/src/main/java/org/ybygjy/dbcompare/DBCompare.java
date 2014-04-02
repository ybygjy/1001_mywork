package org.ybygjy.dbcompare;

import java.io.OutputStream;
import java.sql.Connection;

import org.ybygjy.dbcompare.model.ContextModel;


/**
 * ��������
 * @author WangYanCheng
 * @version 2011-10-8
 */
public class DBCompare {
    private String srcUser, targetUser;
    private Connection conn;
    /** ��������ת�� */
    private OutputStream reportCtxOutput;
    /**�������ݿ�����*/
    private DBEnum dbType;
    /**
     * Constructor
     * @param conn conn
     * @param srcUser srcUser
     * @param targetUser targetUser
     */
    public DBCompare(Connection conn, String srcUser, String targetUser) {
    	this.conn = conn;
        this.srcUser = srcUser;
        this.targetUser = targetUser;
    }


    /**
     * doWork
     */
    public void doWork() {
        final TaskReport[] taskReports = TaskReportFactory.getInstance().getTaskReport(srcUser, targetUser,
            getReportCtxOutput());
        TaskListener taskListener = new TaskListener() {

            public void beforeExecute(Task taskInst) {
                for (TaskReport tr : taskReports) {
                    ContextModel[] tcm = new ContextModel[1];
                    tcm[0] = taskInst.getCommonModel();
                    tr.generateReport(tcm);
                }
            }

            public void afterExecute(Task taskInst) {
                for (TaskReport tr : taskReports) {
                    ContextModel[] tcm = new ContextModel[1];
                    tcm[0] = taskInst.getCommonModel();
                    tr.generateReport(tcm);
                }
            }
        };
        Task[] taskArr = DBEnum.MSSQL == dbType ? TaskFactory.getTaskFactoryInst().getTaskArray4MSSql(srcUser, targetUser, conn, taskListener) : TaskFactory.getTaskFactoryInst().getTaskArray4Ora(srcUser, targetUser, conn, taskListener);
        for (Task task : taskArr) {
            task.execute();
        }
    }

    public OutputStream getReportCtxOutput() {
        return reportCtxOutput;
    }

    public void setReportCtxOutput(OutputStream reportCtxOutput) {
        this.reportCtxOutput = reportCtxOutput;
    }

    /**
     * ȡ��ǰ���ݿ�����
     * @return {@link DBEnum}
     */
	public DBEnum getDbType() {
		return dbType;
	}
	/**
	 * �洢��ǰ���ݿ�����
	 * @param dbType {@link DBEnum}
	 */
	public void setDbType(DBEnum dbType) {
		this.dbType = dbType;
	}
}

package org.ybygjy.dbcompare;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.ybygjy.dbcompare.report.CommandReport;


/**
 * ����ʵ������
 * @author WangYanCheng
 * @version 2011-10-9
 */
public class TaskReportFactory {
    /** used singleton pattern */
    private static TaskReportFactory reportFactInst = new TaskReportFactory();
    /**
     * Constructor
     */
    private TaskReportFactory() {
    }

    /**
     * ȡ������ʵ��
     * @return reportFactInst ������ʵ��
     */
    public static TaskReportFactory getInstance() {
        return reportFactInst;
    }
    /**
     * 
     * @param srcUser
     * @param targetUser
     * @param ous
     * @return
     */
    public TaskReport[] getTaskReport(String srcUser, String targetUser, OutputStream ous) {
        List<TaskReport> taskReports = new ArrayList<TaskReport>();
        TaskReport tmpTr = new CommandReport();
        tmpTr.setReOutputStream(ous);
        tmpTr.setSrcUser(srcUser);
        tmpTr.setTargetUser(targetUser);
        taskReports.add(tmpTr);
        return taskReports.toArray(new TaskReport[taskReports.size()]);
    }
}

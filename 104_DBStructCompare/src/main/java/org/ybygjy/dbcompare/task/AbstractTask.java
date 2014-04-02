package org.ybygjy.dbcompare.task;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.ybygjy.dbcompare.Task;
import org.ybygjy.dbcompare.TaskListener;
import org.ybygjy.dbcompare.model.ContextModel;
import org.ybygjy.dbcompare.model.TaskInfo;


/**
 * ���������񹫹�ʵ��
 * @author WangYanCheng
 * @version 2011-10-8
 */
public abstract class AbstractTask implements Task {
    /** �¼��������󼯺� */
    private List<TaskListener> taskListeners;
    /** ���ݿ�����ʵ�� */
    protected Connection conn;
    /** ģ�͹���ʵ�� */
    protected ContextModel contextModel;
    /** ԭʼ�û�*/
    protected String srcUser;
    /** �����û�*/
    protected String targetUser;
    /**
     * ���췽��
     */
    public AbstractTask(String srcUser, String targetUser) {
        taskListeners = new ArrayList<TaskListener>();
        contextModel = new ContextModel();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setSrcUser(srcUser);
        taskInfo.setTargetUser(targetUser);
        taskInfo.setTaskClassName(this.getClass().getName());
        contextModel.setTaskInfo(taskInfo);
        this.srcUser = taskInfo.getSrcUser();
        this.targetUser = taskInfo.getTargetUser();
    }

    /**
     * {@inheritDoc} �������ʵ�ִ˷���
     */
    public abstract void execute();

    /**
     * {@inheritDoc}
     */
    public void addListener(TaskListener taskListener) {
        taskListeners.add(taskListener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(TaskListener taskListener) {
        if (null != taskListener) {
            taskListeners.remove(taskListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * {@inheritDoc}
     */
    public ContextModel getCommonModel() {
        return contextModel;
    }

    /**
     * ����ִ��ǰ
     */
    protected void beforeListener() {
        for (TaskListener t : taskListeners) {
            if (null != t) {
                t.beforeExecute(this);
            }
        }
    }

    /**
     * ����ִ�к�
     */
    protected void afterListener() {
        for (TaskListener t : taskListeners) {
            if (null != t) {
                t.afterExecute(this);
            }
        }
    }
}

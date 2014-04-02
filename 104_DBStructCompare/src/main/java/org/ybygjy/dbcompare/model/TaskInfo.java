package org.ybygjy.dbcompare.model;

/**
 * ����ʵ����Ϣ
 * @author WangYanCheng
 * @version 2011-10-8
 */
public class TaskInfo {
    /** �������� */
    private String taskName;
    /** ����ʵ���� */
    private String taskClassName;
    /** �������� */
    private String taskDesc;
    /** Դ�û� */
    private String srcUser;
    /** �����û� */
    private String targetUser;
    /** �������� */
    private MetaType metaType;
    /** isFinished*/
    private boolean isFinished;
    /**
     * ȡ��������
     * @return taskName taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * �洢��������
     * @param taskName ��������
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * ȡ�������ʵ����
     * @return taskClassName taskClassName
     */
    public String getTaskClassName() {
        return taskClassName;
    }

    /**
     * �洢����ʵ����
     * @param taskClassName taskClassName
     */
    public void setTaskClassName(String taskClassName) {
        this.taskClassName = taskClassName;
    }

    /**
     * ȡ����������Ϣ
     * @return taskDesc
     */
    public String getTaskDesc() {
        return taskDesc;
    }

    /**
     * �洢����������Ϣ
     * @param taskDesc taskDesc
     */
    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    /**
     * ȡԴ�û�
     * @return srcUser srcUser
     */
    public String getSrcUser() {
        return srcUser;
    }

    /**
     * �洢Դ�û�
     * @param srcUser srcUser
     */
    public void setSrcUser(String srcUser) {
        this.srcUser = srcUser;
    }

    /**
     * ȡĿ��(����)�û�
     * @return targetUser targetUser
     */
    public String getTargetUser() {
        return targetUser;
    }

    /**
     * �洢Ŀ��(����)�û�
     * @param targetUser targetUser
     */
    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    /**
     * ȡ�������ͱ��
     * @return metaType metaType
     */
    public MetaType getTaskType() {
        return metaType;
    }

    /**
     * �洢�������ͱ��
     * @param metaType {@link MetaType}
     */
    public void setTaskType(MetaType metaType) {
        this.metaType = metaType;
    }

    /**
     * ȡ��ǰ����ִ��״̬
     * @return isFinished {true:���;false:δ���}
     */
    public boolean isFinished() {
        return isFinished;
    }
    /**
     * �洢����ִ��״̬
     * @param isFinished {true:���;false:δ���}
     */
    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public String toString() {
        return "TaskInfo [��������=" + taskName + ", ����ʵ����=" + taskClassName + ", ��������="
            + taskDesc + ", ԭʼ�û�=" + srcUser + ", �����û�=" + targetUser + ", ���������=" + metaType
            + ", ����״̬=" + isFinished + "]";
    }
}

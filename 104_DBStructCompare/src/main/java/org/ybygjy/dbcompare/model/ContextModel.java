package org.ybygjy.dbcompare.model;

import java.util.HashMap;
import java.util.Map;

/**
 * ������ģ�͵ĳ������,������ģ�͵ĳ������
 * @author WangYanCheng
 * @version 2011-10-8
 */
public class ContextModel {
    private TaskInfo taskInfo;
    /** ����ԭʼ���ݼ� */
    private Map<String, Object> taskRawDataColl = new HashMap<String, Object>();

    /**
     * ȡ������Ϣʵ��
     * @return ������Ϣʵ��
     */
    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    /**
     * �洢������Ϣʵ��
     * @param taskInfo ������Ϣʵ��
     */
    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    /**
     * �洢ԭʼ���ݼ�
     * @param key ��
     * @param obj ֵ
     */
    public void putRawData(String key, Object obj) {
        this.taskRawDataColl.put(key, obj);
    }

    /**
     * ȡԭʼ���ݼ�
     * @param key ��
     * @return rtnV ֵ����/ֵ��
     */
    public Object getRawData(String key) {
        return taskRawDataColl.containsKey(key) ? taskRawDataColl.get(key) : null;
    }
}

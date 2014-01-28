package org.ybygjy.meta;

import java.util.List;
import java.util.Map;

import org.ybygjy.meta.model.ConstraintColumnMeta;
import org.ybygjy.meta.model.ConstraintMeta;
import org.ybygjy.meta.model.TableMeta;

//TODO �ýӿ�̫�󣬽���ýӿ�����ְ����в�֣���������ء���Լ��Ԫ���ݹ�����ء���Ԫ���ݹ������
/**
 * ����Meta����
 * <p>
 * 1������Meta���ݿ������ʵ���ת��
 * <p>
 * 2������Metaʵ�����װ
 * @author WangYanCheng
 * @version 2012-4-9
 */
public interface MetaMgr {
    /**
     * ȡ�õ�ǰ�������ݿ���û�SCHEMA��ӵ�еı����
     * @return rtnList Map�ṹ�洢�ı����
     */
    public Map<String, TableMeta> getAllTableMeta();

    /**
     * ȡ�ø������Ƶı����
     * @param tableName �����
     * @return rtnTableMeta {@link TableMeta}
     */
    public TableMeta getTableMeta(String tableName);

    /**
     * ���ñ��Լ��
     * @param tableName ������
     * @param consStr Լ������
     */
    public void disableTableConstraint(String tableName, String consStr);

    /**
     * �������ñ���뼯���еı������Լ��
     * @param tableNames ����뼯��
     */
    public void disableTableConstraint(String[] tableNames);

    /**
     * ���ñ��Լ��
     * @param tableName ������
     * @param consStr Լ������
     */
    public void enableTableConstraint(String tableName, String consStr);

    /**
     * �������ñ���뼯���еı������Լ��
     * @param tableNames ����뼯��
     */
    public void enableTableConstraint(String[] tableNames);

    /**
     * ȡ�������Լ��
     * @param tableName ������
     * @return rtnConsArr Լ��������/null
     */
    public String[] getTableConstraints(String tableName);

    /**
     * ���ñ������Լ��
     * @param tableName ������
     */
    public void disableTableConstraint(String tableName);

    /**
     * ���ñ������Լ��
     * @param tableName ������
     */
    public void enableTableConstraint(String tableName);

    /**
     * ȡ����Լ��
     * @param tableMeta {@link TableMeta}
     * @return rtnList {@link ConstraintMeta}
     */
    public List<ConstraintMeta> getConstraints(TableMeta tableMeta);

    /**
     * ȡԼ���ֶ�
     * @param cmInst {@link ConstraintColumnMeta}
     * @return rtnList {@link ConstraintColumnMeta}
     */
    public List<ConstraintColumnMeta> getConstraintsColumns(ConstraintMeta cmInst);

    /**
     * ���ñ�Ĵ�����
     * @param tableName ������
     */
    public boolean disableTableTriggers(String tableName);

    /**
     * ���ñ�Ĵ�����
     * @param tableName ������
     */
    public boolean enableTableTriggers(String tableName);

    /**
     * ���ñ�����
     * @param tableName ������
     */
    public void resetTableSequence(String tableName);
}

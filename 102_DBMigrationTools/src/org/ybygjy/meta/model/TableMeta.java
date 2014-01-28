package org.ybygjy.meta.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ����TableMetaģ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class TableMeta {
    /** ������ */
    private String tableName;
    /** ��������Դ��Ŀ���ӳ�䣬׾�ӵ���Ƶ��±��������� */
    private String srcTableName;
    /** ��������Դ��Ŀ���ӳ�䣬׾�ӵ���Ƶ��±��������� */
    private String targetTableName;
    /** fieldMeta���� */
    private List<FieldMeta> fieldList = new ArrayList<FieldMeta>();
    /** fieldMap�洢fieldMap���ϻ���hashCode���� */
    private Map<String, FieldMeta> fieldMap = new HashMap<String, FieldMeta>();
    /** ��Լ�� */
    private List<ConstraintMeta> constraints = new ArrayList<ConstraintMeta>();
    /** ͳ�����������ֶ���������CLOB\BLOB */
    private int specialTypeCounts;

    /**
     * ������
     * @param tableName ������
     */
    public void setTableName(String tableName) {
        this.tableName = tableName.toUpperCase();
    }

    public String getTableName() {
        return this.tableName;
    }

    /**
     * ���FieldMeta
     * <p>
     * 1�����FieldMeta��List
     * <p>
     * 2�����FieldMeta��Map
     * @param fieldMeta {@link FieldMeta}
     */
    public void addField(FieldMeta fieldMeta) {
        String fieldCode = fieldMeta.getFieldCode();
        fieldCode = fieldCode.toUpperCase();
        if (!fieldMap.containsKey(fieldCode)) {
            this.fieldMap.put(fieldCode, fieldMeta);
        }
        if (fieldMeta.isSpecialType()) {
            this.specialTypeCounts++;
        }
        this.fieldList.add(fieldMeta);
    }

    /**
     * ȡFieldMeta List���϶���
     * @return fieldsList fieldMeta����
     */
    public List<FieldMeta> getFieldList() {
        return this.fieldList;
    }

    /**
     * ȡFieldMeta Map���϶���
     * @return fieldMap fieldMeta����
     */
    public Map<String, FieldMeta> getFieldMap() {
        return this.fieldMap;
    }

    /**
     * ȡFieldMeta
     * @param fieldCode �ֶα���
     * @return rtnFieldMeta {@link FieldMeta}
     */
    public FieldMeta getField(String fieldCode) {
        return this.fieldMap.get(fieldCode);
    }

    /**
     * �ο����ݵ�tableMeta�ؽ�����tableMeta
     * @param tableMeta ����tableMeta
     * @return rtnTableMeta {@link TableMeta}
     */
    public TableMeta consultAndRebuilt(TableMeta tableMeta) {
        // TODO ������һ����ڽṹ�ϵĹ�ϵ�����ȵ�ǰ��ͨ���Ƚ��ֶ����ķ�ʽʵ������TableMeta���ݵ�ӳ��
        // TODO ����ȱ�ٶ�������TableMeta�ĳ���ӳ��ģ�ͣ���Ҫ�������ӳ��ģ��
        // TODO �������ӳ��ģ�����Ǿͷ����˵�ǰ��������TableMeta�Ĺ�����ϵ������SQL�����ִ�еĴ���ģʽ
        // TODO �������ӳ��ģ��SQL������߾�ֱ�������Լ��ض���TableMeta�������ڹ���TableMeta
        // TODO �������ӳ��ģ��SQLִ����߾��������ӳ��ģ�ͽ��в�������
        // TODO ����ģ�͵Ľṹ
        // TODO 1��������������TableMeta���ֶε�ӳ���ϵ
        // TODO 2������A���B�ֶ���A(������)���C�ֶ�����ϵ
        // TODO 3���ù��ߵĶ�λ���Ǿ��Ǵ����������ݵ����͵��룬����ӳ��ģ��ҲҪ�������ȥ����
        TableMeta rtnTableMeta = new TableMeta();
        rtnTableMeta.setTableName(getTableName());
        rtnTableMeta.addConstraints(tableMeta.getConstraints());
        rtnTableMeta.setSpecialTypeCounts(tableMeta.getSpecialTypeCounts());
        Map<String, FieldMeta> consultFieldMap = tableMeta.getFieldMap();
        for (Iterator<String> iterator = this.fieldMap.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            // ����δ�����ֶ����͵������޶����أ�ֻҪ���ֶα�����ͬ����
            if (consultFieldMap.containsKey(key)) {
                FieldMeta tmpFieldMeta = consultFieldMap.get(key);
                FieldMeta fieldMeta = new FieldMeta();
                fieldMeta.setFieldCode(tmpFieldMeta.getFieldCode());
                fieldMeta.setFieldType(tmpFieldMeta.getFieldType());
                fieldMeta.setFieldTypeStr(tmpFieldMeta.getFieldTypeStr());
                rtnTableMeta.addField(fieldMeta);
            } else {
                System.err.println("��".concat(tableMeta.getTableName()).concat("ȱ���ֶΣ�".concat(key)));
            }
        }
        return rtnTableMeta;
    }

    /**
     * ��ӱ�����Լ��
     * @param constraints {@link ConstraintMeta}
     */
    public void addConstraints(List<ConstraintMeta> constraints) {
        this.constraints.addAll(constraints);
    }

    /**
     * ȡ��Լ���б�
     * @return constraints {@link ConstraintMeta}
     */
    public List<ConstraintMeta> getConstraints() {
        return this.constraints;
    }

    /**
     * ��ȡ����Լ��
     * @return primaryConstraint ����Լ��/null
     */
    public ConstraintMeta getPrimaryConstraint() {
        if (this.constraints.size() > 0) {
            for (ConstraintMeta cmInst : this.constraints) {
                if (cmInst.getConstraintType() == ConstraintMeta.ConstraintType.P) {
                    return cmInst;
                }
            }
        }
        return null;
    }

    /**
     * ȡ���������ֶμ�
     * @return columns
     */
    public List<FieldMeta> getSpecialTypeColumns() {
        List<FieldMeta> rtnArray = new ArrayList<FieldMeta>(this.specialTypeCounts);
        for (FieldMeta fmInst : this.fieldList) {
            if (fmInst.getFieldType().getValue() >= FieldType.CLOB.getValue()) {
                rtnArray.add(fmInst);
            }
        }
        return rtnArray;
    }

    /**
     * ���������ֶ�����
     * @return the specialTypeCounts
     */
    public int getSpecialTypeCounts() {
        return specialTypeCounts;
    }

    /**
     * �Ƿ��зǱ�׼������
     * @return rtnFlag {true:��;false:��}
     */
    public boolean hasSpecialType() {
        return (this.specialTypeCounts > 0);
    }

    /**
     * ���������ֶ�����
     * @param scount �ֶ�����
     */
    public void setSpecialTypeCounts(int scount) {
        this.specialTypeCounts = scount;
    }

    /**
     * @return the srcTableName
     */
    public String getSrcTableName() {
        return srcTableName;
    }

    /**
     * @param srcTableName the srcTableName to set
     */
    public void setSrcTableName(String srcTableName) {
        this.srcTableName = srcTableName;
    }

    /**
     * @return the targetTableName
     */
    public String getTargetTableName() {
        return targetTableName;
    }

    /**
     * @param targetTableName the targetTableName to set
     */
    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    @Override
    public String toString() {
        return "TableMeta [tableName=" + tableName + ", fieldList=" + fieldList + "]";
    }
}

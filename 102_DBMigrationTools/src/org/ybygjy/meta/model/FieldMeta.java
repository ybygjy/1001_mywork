package org.ybygjy.meta.model;

/**
 * ����FieldMetaģ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class FieldMeta {
    /** �ֶα���*/
    private String fieldCode;
    /** ԭʼ�ֶ����� */
    private String fieldTypeStr;
    /** �ֶ����� */
    private FieldType fieldType;
    /** �ɷ�Ϊ��*/
    private boolean isNullable;
    /** Ĭ��ֵ*/
    private String defaultValue;
    /** ֧���ֶ����ݳ���*/
    private int dataLength;
    /**
     * @return the fieldCode
     */
    public String getFieldCode() {
        return fieldCode;
    }

    /**
     * @param fieldCode the fieldCode to set
     */
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    /**
     * @return the fieldType
     */
    public String getFieldTypeStr() {
        return fieldTypeStr;
    }

    /**
     * @param fieldType the fieldType to set
     */
    public void setFieldTypeStr(String fieldType) {
        this.fieldTypeStr = fieldType;
    }

    /**
     * @return the isNullable
     */
    public boolean isNullable() {
        return isNullable;
    }

    /**
     * @param isNullable the isNullable to set
     */
    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the dataLength
     */
    public int getDataLength() {
        return dataLength;
    }

    /**
     * @param dataLength the dataLength to set
     */
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * @return the fieldType
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * @param fieldType the fieldType to set
     */
    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * �ֶ��Ƿ����ڷ���ͨ����
     * @return rtnType {true:��;false:��}
     */
    public boolean isSpecialType() {
        return (this.getFieldType().getValue() >= FieldType.CLOB.getValue());
    }

    @Override
    public String toString() {
        return "FieldMeta [fieldCode=" + fieldCode + ", fieldType=" + fieldTypeStr + ", isNullable="
            + isNullable + ", defaultValue=" + defaultValue + ", dataLength=" + dataLength + "]";
    }
}

package org.ybygjy;

/**
 * �������ģʽ
 * @author WangYanCheng
 * @version 2012-10-16
 */
public enum TestModel {
    ORA_ORA(10),ORA_MSSQL(20),MSSQL_MSSQL(30),MSSQL_ORA(40);
    private int value;
    TestModel(int value) {
        this.value = value;
    }
    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}

package org.ybygjy.util;
/**
 * �������ݿ�����
 * @author WangYanCheng
 * @version 2012-11-14
 */
public enum DB_TYPE {
    /** Oracle*/
    ORACLE(11, "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@127.0.0.1:1521/SID"),
    /** SQLServer*/
    MSSQL(22, "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://127.0.0.1;databaseName=TEST;instanceName=SID"),
    /**δ֪����*/
    UNKNOW(1000, "", "");
    private int value;
    private String driverClass;
    private String connTmpl;
    private DB_TYPE(int value, String driverClass, String connTmpl) {
        this.value = value;
        this.driverClass = driverClass;
        this.connTmpl = connTmpl;
    }
    public static DB_TYPE getType(int value) {
        for (DB_TYPE dt : DB_TYPE.values()) {
            if (dt.value == value) {
                return dt;
            }
        }
        return UNKNOW;
    }
    /**
     * ���ݸ����ַ���ȷ�����ݿ�����
     * @param value �ַ���
     * @return dbType��{@link DB_TYPE#UNKNOW}
     */
    public static DB_TYPE getTypeByDriver(String value) {
        for (DB_TYPE dt : DB_TYPE.values()) {
            if (dt.getDriver().equals(value)) {
                return dt;
            }
        }
        return UNKNOW;
    }

    public static DB_TYPE getTypeByName(String value) {
        for (DB_TYPE dt : DB_TYPE.values()) {
            if (dt.name().equals(value)) {
                return dt;
            }
        }
        return UNKNOW;
    }
    /**
     * ���ݿ���������
     * @return driverClass
     */
    public String getDriver() {
        return this.driverClass;
    }
    /**
     * �����ݿ�����Ӵ�ģ��
     * @return connTmpl
     */
    public String getConnTmpl() {
        return this.connTmpl;
    }
}

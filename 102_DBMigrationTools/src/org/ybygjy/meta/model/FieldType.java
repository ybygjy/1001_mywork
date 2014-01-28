package org.ybygjy.meta.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.ybygjy.logger.LoggerFactory;

/**
 * Լ��FieldMeta����
 * <table border=1>
 * <tr>
 * <th>���ͱ��</th>
 * <th>���ʹ���</th>
 * </tr>
 * <tr>
 * <td>00</td>
 * <td>STR</td>
 * </tr>
 * <tr>
 * <td>01</td>
 * <td>NUM</td>
 * </tr>
 * <tr>
 * <td>02</td>
 * <td>DATE</td>
 * </tr>
 * <tr>
 * <td>100</td>
 * <td>CLOB</td>
 * </tr>
 * <tr>
 * <td>200</td>
 * <td>BLOB</td>
 * </tr>
 * </table>
 * @author WangYanCheng
 * @version 2012-4-10
 */
public enum FieldType {
    STR(00), NUM(01), DATE(02), CLOB(100), BLOB(200), NCLOB(101);
    private int flag;
    private static Map<String, FieldType> msSqlTypeMap = new HashMap<String, FieldType>();
    private static Map<String, FieldType> oracleTypeMap = new HashMap<String, FieldType>();
    /**{@link Logger}*/
    private static Logger logger;
    private FieldType(int flag) {
        this.flag = flag;
    }

    public int getValue() {
        return this.flag;
    }
    static {
        logger = LoggerFactory.getInstance().getLogger(FieldType.class.getName());
    }
    static {
        /** ����MSSql����ӳ�� */
        msSqlTypeMap.put("INT", NUM);
        msSqlTypeMap.put("DECIMAL", NUM);
        msSqlTypeMap.put("SMALLINT", NUM);
        msSqlTypeMap.put("NUMERIC", NUM);
        msSqlTypeMap.put("TINYINT", NUM);
        msSqlTypeMap.put("BIGINT", NUM);
        msSqlTypeMap.put("BIT", NUM);
        msSqlTypeMap.put("FLOAT", NUM);
        msSqlTypeMap.put("CHAR", STR);
        msSqlTypeMap.put("NCHAR", STR);
        msSqlTypeMap.put("VARCHAR", STR);
        msSqlTypeMap.put("NVARCHAR", STR);
        msSqlTypeMap.put("TEXT", CLOB);
        msSqlTypeMap.put("NTEXT", NCLOB);
        msSqlTypeMap.put("SMALLDATETIME", DATE);
        msSqlTypeMap.put("DATETIME", DATE);
        msSqlTypeMap.put("VARBINARY", BLOB);
        msSqlTypeMap.put("BINARY", BLOB);
        msSqlTypeMap.put("IMAGE", BLOB);
        /** ����OracleSql����ӳ�� */
        oracleTypeMap.put("^NUMBER.*", NUM);
        oracleTypeMap.put("^CHAR.*", STR);
        oracleTypeMap.put("^VARCHAR2.*", STR);
        oracleTypeMap.put("^CLOB.*", CLOB);
        oracleTypeMap.put("^BLOB.*", BLOB);
        oracleTypeMap.put("^TIMESTAMP.*", DATE);
        oracleTypeMap.put("^DATE.*", DATE);
    }

    /**
     * ӳ��MSSql����
     * @param fieldType ��������
     * @return rtnType {@link FieldType}
     */
    public static FieldType mappingType4MSSql(String fieldType) {
        fieldType = fieldType.toUpperCase();
        return msSqlTypeMap.containsKey(fieldType) ? msSqlTypeMap.get(fieldType) : null;
    }

    /**
     * ӳ��Oracle����
     * @param fieldType ��������
     * @return rtnType {@link FieldType}Ĭ�Ϸ���{@linkplain FieldType#STR}
     */
    public static FieldType mappingType4Oracle(String fieldType) {
        FieldType rtnType = null;
        fieldType = fieldType.toUpperCase();
        for (String regExpStr : oracleTypeMap.keySet()) {
            if (fieldType.matches(regExpStr)) {
                rtnType = oracleTypeMap.get(regExpStr);
                break;
            }
        }
        if (rtnType == null) {
            logger.warning("�Ƿ�Oracle��������==>".concat(fieldType));
        }
        return rtnType;
    }
}

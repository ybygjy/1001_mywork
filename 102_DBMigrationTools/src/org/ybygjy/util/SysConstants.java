package org.ybygjy.util;


/**
 * ϵͳ����
 * @author WangYanCheng
 * @version 2012-8-29
 */
public class SysConstants {
    /**Ĭ������ֵ_��������ģ���ʹ��*/
    public static final int DB_SEQ_DEFVAL= 10000000;
    /**SQLServer���ݿ����Ӵ�*/
    public static final String DB_URL_SQLSERVER = "jdbc:sqlserver://192.168.0.16;databaseName=angangdata;user=nsag;password=11111111;instanceName=sql2005";
    /**Oracle���ݿ����Ӵ�*/
    public static final String DB_URL_ORACLE = "jdbc:oracle:thin:NSTCSA3542/747209@192.168.0.143:1521/NSDEV";
    //public static final String DB_URL_ORACLE = "jdbc:oracle:thin:NSTCSA1421/475656@192.168.0.143:1521/NSDEV";
    /**�����ı���_����Լ��ʧ��SQL*/
    public static final String CTX_SQL_DISCONST="CTX_5001";
    /**�����ı���_����ʧ��SQL*/
    public static final String CTX_SQL_RAWINSERT_FAIL="CTX_2001";
    /**�����ʧ��*/
    public static final int TABLE_TRUNCATE_FAILURE=-1;
    /**�����ɹ�*/
    public static final int TABLE_TRUNCATE_SUCCESS=0;
    /**��󻺴�������Ŀ��ֵ*/
    public static final int MAX_CACHEITEM = 5000;
}

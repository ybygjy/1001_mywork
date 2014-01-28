package org.ybygjy.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * ��֤JDBC������JDK���ڵ�ת��
 * @author WangYanCheng
 * @version 2012-06-08
 */
public class DateTest {
    /**����*/
    private static SimpleDateFormat sdfInst = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * �������
     * @param args �����б�
     */
	public static void main(String[] args) {
		long currTime = 0;
		try {
			currTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2012-05-18 01:01:01").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(currTime);
		System.out.println(sdfInst.format(sqlDate));
	}
}

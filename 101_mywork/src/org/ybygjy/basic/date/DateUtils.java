package org.ybygjy.basic.date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * ��װ���ڴ���
 * @author WangYanCheng
 * @version 2012-9-6
 */
public class DateUtils {
    /**
     * ȡ�������������
     * @param date1 ����1
     * @param date2 ����2
     * @return diffDay �������
     */
    public static int getDiff(Date date1, Date date2) {
        long t1 = date1.getTime();
        long t2 = date2.getTime();
        long t3 = Math.abs(t1 - t2);
        return (int) (t3 / 1000 / 60 / 60 / 24);
    }

    /**
     * ȡ�����������������
     * @param date1 ����1
     * @param date2 ����2
     * @return rtnDateArr ������
     */
    public static String[] diffDateGroup(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return diffDateGroup(date2, date1);
        }
        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTimeInMillis(date1.getTime());
        List<Date> tmpList = new ArrayList<Date>();
        while (tmpCalendar.getTimeInMillis() <= date2.getTime()) {
            tmpList.add(tmpCalendar.getTime());
            tmpCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return convertDateArr2StrArr(tmpList);
    }

    /**
     * ת��������
     * @param dateArr ���ڼ�
     * @return ���ڴ���
     */
    public static String[] convertDateArr2StrArr(List<Date> dateArr) {
        String[] rtnArr = new String[dateArr.size()];
        int i = 0;
        for (Iterator<Date> iterator = dateArr.iterator(); iterator.hasNext();) {
            rtnArr[i++] = dateFormat(iterator.next(), "yyyy-MM-dd");
        }
        return rtnArr;
    }

    /**
     * �������������ݸ�ʽ�ַ������и�ʽ��
     * @param date ����
     * @param pattern ��ʽ�ַ���
     * @return rtnStr �����ַ���
     */
    public static String dateFormat(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

}

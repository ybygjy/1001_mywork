package org.ybygjy.basic.date;


/**
 * Calendar ʹ��
 * @author WangYanCheng
 * @version 2009-12-9
 */
public class CalendarApp {
    /**
     * ִ�����
     * @param args �����б�
     */
    public static void main(String[] args) {
//        Calendar calenInst = Calendar.getInstance();
//        String tmpDate = "2009-10-20 11:22:11";
//        System.out.println(tmpDate.substring(11, 16));
//        System.out.println(calenInst.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        CalendarApp caInst = new CalendarApp();
        System.out.println(caInst.doStrCompare("14:59", "15:00"));
    }
    /**
     * doStrCompare
     * @param str1 str1
     * @param str2 str2
     * @return rtnResult {���:����:С��}
     */
    public String doStrCompare(String str1, String str2) {
        int rtnResult = str1.compareTo(str2);
        return rtnResult == 0 ? "���" : rtnResult < 0 ? str1 + "С��" + str2 : str1 + "����" + str2;
    }
}

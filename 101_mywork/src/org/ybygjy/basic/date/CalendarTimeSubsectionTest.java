package org.ybygjy.basic.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ʱ��β���
 * @author WangYanCheng
 * @version 2009-11-20
 */
public class CalendarTimeSubsectionTest {
    /**
     * doPrint
     * @param obj the printed info
     */
    public static void doPrint(Object obj) {
        System.out.println(obj.toString());
    }

    /**
     * ����ʱ���
     */
    public void doCalcSubSect() {
        String[] arg1 = timeSubsection[0], arg2 = timeSubsection[1], arg3 = timeSubsection[2], tmpArg;
        Calendar currObj = Calendar.getInstance();
        //��ǰʱ��
        long currTime = currObj.getTimeInMillis(), startTime = 0L, stopTime = 0L;
        tmpArg = arg1[0].split(":");
        System.out.println("��ǰʱ��:" + currObj.getTimeInMillis());
        currObj.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArg[0]));
        currObj.set(Calendar.MINUTE, Integer.parseInt(tmpArg[1]));
        startTime = currObj.getTimeInMillis();
        tmpArg = arg1[1].split(":");
        currObj.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArg[0]));
        currObj.set(Calendar.MINUTE, Integer.parseInt(tmpArg[1]));
        stopTime = currObj.getTimeInMillis();
        if (startTime < currTime && currTime <= stopTime) {
            currObj.setTimeInMillis(currTime);
            System.out.println("��װ�" + doParseDate(currObj, "yyyy-MM-dd HH:mm:ss"));
        }
        tmpArg = arg2[0].split(":");
        currObj.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArg[0]));
        currObj.set(Calendar.MINUTE, Integer.parseInt(tmpArg[1]));
        startTime = currObj.getTimeInMillis();
        tmpArg = arg2[1].split(":");
        currObj.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArg[0]));
        currObj.set(Calendar.MINUTE, Integer.parseInt(tmpArg[1]));
        stopTime = currObj.getTimeInMillis();
        if (startTime < currTime && currTime <= stopTime) {
            currObj.setTimeInMillis(currTime);
            System.out.println("Сҹ��:" + doParseDate(currObj, "yyyy-MM-dd HH:mm:ss"));
        } else {
            System.out.println("[\n@START@\n@STOP@\n[@CURR@]��Сҹ��]"
                    .replaceAll("@START@", String.valueOf(startTime))
                    .replaceAll("@STOP@", String.valueOf(stopTime))
                    .replaceAll("@CURR@", String.valueOf(currTime))
            );
        }
        tmpArg = arg3[0].split(":");
        currObj.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArg[0]));
        currObj.set(Calendar.MINUTE, Integer.parseInt(tmpArg[1]));
        startTime = currObj.getTimeInMillis();
        tmpArg = arg3[1].split(":");
        currObj.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArg[0]));
        currObj.set(Calendar.MINUTE, Integer.parseInt(tmpArg[1]));
        stopTime = currObj.getTimeInMillis();
        if (startTime < currTime && currTime <= stopTime) {
            currObj.setTimeInMillis(currTime);
            System.out.println("��ҹ��:" + doParseDate(currObj, "yyyy-MM-dd HH:mi:ss"));
        } else {
            System.out.println("[\n@START@\n@STOP@\n[@CURR@]�Ǵ�ҹ��]"
                    .replaceAll("@START@", String.valueOf(startTime))
                    .replaceAll("@STOP@", String.valueOf(stopTime))
                    .replaceAll("@CURR@", String.valueOf(currTime))
            );
        }
    }
    /**
     * �������
     * @param args arguments lists
     */
    public static void main(String[] args) {
        Calendar currObj = Calendar.getInstance();
        String dateTemplate = "@HOUR@ʱ@MINUTE@��@SECOND@��";
        System.out.println(dateTemplate.replaceAll("@HOUR@", String.valueOf(currObj.get(Calendar.HOUR_OF_DAY)))
                .replaceAll("@MINUTE@", String.valueOf(currObj.get(Calendar.MINUTE)))
                .replaceAll("@SECOND@", String.valueOf(currObj.get(Calendar.SECOND))));
        CalendarTimeSubsectionTest ctstObj = new CalendarTimeSubsectionTest();
        long currTime = currObj.getTimeInMillis();
        System.out.println(ctstObj.isShift(currTime, new String[]{"8:00", "15:16"}));
        System.out.println(ctstObj.isShift(currTime, new String[]{"8:00", "16:00"}));
        DateFormat dateFObj = new SimpleDateFormat("yyyy-MM-dd");
        try {
            currObj = Calendar.getInstance();
            doPrint("��ǰʱ����{8:00,00:00}" + ctstObj.isShift(currTime, new String[]{"8:00", "00:00"}));
            currObj.setTime(dateFObj.parse("2009-12-04 "));
            System.out.println(ctstObj.isShift(currTime, new String[]{"16:00", "00:00"}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int resultFlag = ctstObj.doGetShift("");
        System.out.println(resultFlag);
    }
    /**
     * ��֤ĳһʱ���Ƿ���ĳһʱ���
     * @param currTime ĳһʱ��
     * @param timeSlot ĳһʱ���
     * @return true/false
     */
    public boolean isShift(final long currTime, String[] timeSlot) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(currTime);
        String[] tmpArray = timeSlot[0].split(":");
        long startTime, stopTime;
        tempCalendar.clear(Calendar.HOUR_OF_DAY);
        tempCalendar.clear(Calendar.MINUTE);
        tempCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmpArray[0]));
        tempCalendar.set(Calendar.MINUTE, Integer.parseInt(tmpArray[1]));
        startTime = tempCalendar.getTimeInMillis();
        tmpArray = timeSlot[1].split(":");
        int stopHour  = Integer.parseInt(tmpArray[0]), stopMinute = Integer.parseInt(tmpArray[1]);
        if (stopHour == 0) {
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        tempCalendar.clear(Calendar.HOUR_OF_DAY);
        tempCalendar.clear(Calendar.MINUTE);
        tempCalendar.set(Calendar.HOUR_OF_DAY, stopHour);
        tempCalendar.set(Calendar.MINUTE, stopMinute);
        stopTime = tempCalendar.getTimeInMillis();
        return ((startTime < currTime && currTime <= stopTime) ? true : false);
    }
    /**
     * ��μ���
     * @param orgCode ������λ
     * @return result {1:��ҹ;2:�װ�;3:Сҹ;4:ҹ��;0:���⴦��}
     */
    public int doGetShift(String orgCode) {
        int result = 0;
        Calendar currCalen = Calendar.getInstance();
        long currTime = currCalen.getTimeInMillis();
        if (isShift(currTime, timeSubsection[2])) {
            result = 1;
        } else if (isShift(currTime, timeSubsection[0])) {
            result = 2;
        } else if (isShift(currTime, timeSubsection[1])) {
            result = 3;
        }
        return result;
    }
    //ʱ��� 0:�װ�;1:Сҹ��;2:��ҹ��*/
    private static String[][] timeSubsection = {{"8:00", "16:00"}, {"16:00", "00:00"}, {"00:00", "08:00"}};
    /**
     * ���ڸ�ʽ��
     * @param calenObj ����ʵ��
     * @param formatStr ��ʽ����
     * @return result ��ʽ��ɵĴ�
     */
    public String doParseDate(Calendar calenObj, String formatStr) {
        DateFormat df = new SimpleDateFormat(formatStr);
        String result = null;
        result = df.format(calenObj.getTime());
        return result;
    }
}

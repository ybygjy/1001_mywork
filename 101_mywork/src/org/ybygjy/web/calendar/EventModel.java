package org.ybygjy.web.calendar;
/**
 * �������ģ��
 * @author WangYanCheng
 * @version 2011-3-24
 */
public class EventModel {
    /**����*/
    private String id;
    /**����*/
    private int type;
    /**����*/
    private String title;
    /**����*/
    private String content;
    /**��ʼ����*/
    private String startDay;
    /**��ʼʱ��*/
    private String startTime;
    /**��������*/
    private String endDay;
    /**����ʱ��*/
    private String endTime;
    /**ȫ����*/
    private boolean allDayFlag;
    /**
     * ���캯����ʼ������ֵ
     * @param id ����
     * @param type ����
     * @param title ����
     * @param content ����
     * @param startDay ��ʼ����
     * @param startTime ��ʼʱ��
     * @param endDay ��������
     * @param endTime ����ʱ��
     * @param allDayFlag ȫ����
     */
    public EventModel(String id, int type, String title, String content, String startDay, String startTime,
        String endDay, String endTime, boolean allDayFlag) {
        super();
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.startDay = startDay;
        this.startTime = startTime;
        this.endDay = endDay;
        this.endTime = endTime;
        this.allDayFlag = allDayFlag;
    }
    /**
     * ����ת��ΪJSON��ʽ
     * @return jsonStr JSON��ʽ
     */
    public String toJSON() {
        StringBuilder sbud = new StringBuilder();
        sbud.append("\"id\"").append(":\"").append(this.id).append("\",")
        .append("\"type\"").append(":\"").append(type).append("\",")
        .append("\"title\"").append(":\"").append(title).append("\",")
        .append("\"content\"").append(":\"").append(content).append("\",")
        .append("\"start\":\"").append(startDay).append("\",")
        .append("\"startTime\":\"").append(startTime).append("\",")
        .append("\"end\":\"").append(endDay).append("\",")
        .append("\"endTime\":\"").append(endTime).append("\",")
        .append("\"allDay\":").append(allDayFlag).append("");
        return sbud.toString();
    }
}

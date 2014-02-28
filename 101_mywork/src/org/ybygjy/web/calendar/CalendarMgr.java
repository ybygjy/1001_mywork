package org.ybygjy.web.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ������͹���Calendarģ��ҵ����Ϊ
 * @author WangYanCheng
 * @version 2011-3-24
 */
public class CalendarMgr {
    /**
     * ��ѯ��������
     * @param qryBean ��ѯBean
     * @return rtn
     */
    public List<EventModel> queryEvents(Object qryBean) {
        List<EventModel> rtnList = new ArrayList<EventModel>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        for (int i = 0; i < 5; i++) {
            String startDate = sdf.format(calendar.getTime());
            calendar.add(Calendar.HOUR, i);
            String stopDate = sdf.format(calendar.getTime());
            rtnList.add(new EventModel("" + i, i,
                "�ҵ�ʱ���ǣ�".concat(startDate), stopDate, startDate, "", stopDate, "", false));
        }
        return rtnList;
    }
    /**
     * ��ѯ������JSON��ʽ���
     * @param qryBean ��ѯBean
     * @return jsonStr jsonStr
     */
    public String queryEvents2JSON(Object qryBean) {
        List<EventModel> qryModel = queryEvents(qryBean);
        StringBuilder rtnStr = new StringBuilder("[");
        for (EventModel em : qryModel) {
            rtnStr.append("{").append(em.toJSON()).append("}").append(",");
        }
        rtnStr.setLength(rtnStr.length() - 1);
        rtnStr.append("]");
//        rtnStr = new StringBuilder("[]");
        return rtnStr.toString();
    }
}

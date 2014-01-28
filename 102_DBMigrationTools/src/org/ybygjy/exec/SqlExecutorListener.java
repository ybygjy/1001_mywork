package org.ybygjy.exec;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.ybygjy.sql.model.SqlModel;

/**
 * ʹ���¼��������
 * @author WangYanCheng
 * @version 2012-4-9
 */
public interface SqlExecutorListener {
    /**
     * �¼�������ƣ������ѯ��ɺ����
     * @param smInst {@link SqlModel}
     * @param dataMap ���ݼ�
     */
    public void afterQuery(SqlModel smInst, List<Map<String, Object>> dataMap);
    /**
     * ִ������������͵Ĳ������
     * @param sqlModel {@link SqlModel}
     * @param rs �����������͵��ֶεĽ����
     */
    public void afterSpecialTypeQuery(SqlModel sqlModel, ResultSet rs);
}

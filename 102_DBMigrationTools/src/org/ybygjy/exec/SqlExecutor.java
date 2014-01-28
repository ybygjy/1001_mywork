package org.ybygjy.exec;

import java.util.List;
import java.util.Map;

import org.ybygjy.sql.model.SqlModel;


/**
 * ����Sqlִ�й���
 * @author WangYanCheng
 * @version 2012-4-9
 */
public interface SqlExecutor {

    /**
     * ִ�в�ѯ���
     * @param sqlModel {@link SqlModel}
     * @return rtnCount ��������
     */
    public int executeQuery(SqlModel sqlModel);

    /**
     * ִ�в������
     * @param sqlModel {@link SqlModel}
     * @param dataMap ���ݼ���
     */
    public int executeInsert(SqlModel sqlModel, List<Map<String, Object>> dataMap) ;

    /**
     * ����¼�������
     * @param selInst {@link SqlExecutorListener}
     */
    public void addListener(SqlExecutorListener selInst);

    /**
     * ִ��SQL���
     * @param sql SQL���
     */
    public void executeSQL(String sql);

    
    /**
     * ����ִ��SQL���
     * <strong>ע�⣺��Ϊ�����������������������ĳ��SQL���ִ������Ӱ������SQL</strong>
     * @param sql SQL��伯
     */
    public void executeSQL(String[] tmplSql);
}

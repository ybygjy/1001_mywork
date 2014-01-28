package org.ybygjy.stype;

import java.sql.ResultSet;

import org.ybygjy.exec.SqlExecutor;
import org.ybygjy.exec.SqlExecutorListener;
import org.ybygjy.sql.model.SqlModel;

/**
 * �����������ͣ���CLob/Blob
 * @author WangYanCheng
 * @version 2012-10-15
 */
public interface STypeSqlExecutor {
    /**
     * ִ���������Ͳ�ѯ
     * <p>
     * ��Ҫ���ڲ��ص����ݽ������Ʋ�һ�£�{@linkplain SqlExecutor#executeQuery(SqlModel)}
     * �ڲ��ص��Ƿ�װ�õ����ݣ� ���÷������ܷ�װԭʼ����
     * @param sqlModel {@link SqlModel}
     * @return rtnCount ��������
     */
    public void executeQuerySpecialType(SqlModel sqlModel);

    /**
     * ִ���������͵Ĳ������
     * @param srcSqlModel Դ��ѯ�����Sql����{@link SqlModel}
     * @param targetSqlModel ����ִ��Sql����{@link SqlModel}
     * @param rs {@link ResultSet}
     */
    public int executeSpecialTypeInsert(SqlModel srcSqlModel, SqlModel targetSqlModel, ResultSet rs);
    /**
     * ����¼�������
     * @param selInst {@link SqlExecutorListener}
     */
    public void addListener(SqlExecutorListener selInst);
}

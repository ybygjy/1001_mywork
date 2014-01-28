package org.ybygjy.exec.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.ybygjy.sql.model.SqlModel;


/**
 * SqlExecutor MSSqlʵ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class SqlExecutorImpl4MSSql extends AbstractSqlExecutor {
    /**
     * ���캯����ʼ��
     * @param conn �����ݿ�����Ӷ���
     */
    public SqlExecutorImpl4MSSql(Connection conn) {
        super(conn);
    }

    @Override
    public int executeInsert(SqlModel sqlModel, List<Map<String, Object>> dataMap) {
        logger.warning("�ù���δʵ��#".concat(this.getClass().toString()));
        return 0;
    }
}

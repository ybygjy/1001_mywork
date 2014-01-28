package org.ybygjy.sql;

import org.ybygjy.sql.impl.SqlMgrImpl4MSSql;
import org.ybygjy.sql.impl.SqlMgrImpl4Oracle;

/**
 * SQLMgr �����࣬�����װ����ʵ���Ĵ���
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class SqlMgrFactory {
    /** singleton Pattern */
    private static SqlMgrFactory sqlMFInst = new SqlMgrFactory();

    /**
     * ȡSqlMgrʵ��MSSqlʵ��
     * @return rtnSqlMgr {@link SqlMgrImpl4MSSql}
     */
    public SqlMgr getSqlMgrImpl4MSSql() {
        return new SqlMgrImpl4MSSql();
    }

    /**
     * ȡSqlMgrʵ��Oracleʵ��
     * @return rtnSqlMgr {@link SqlMgrImpl4Oracle}
     */
    public SqlMgr getSqlMgrImpl4Oracle() {
        return new SqlMgrImpl4Oracle();
    }

    /**
     * ȡ����ʵ��
     * @return sqlMFInst ����ʵ��
     */
    public static final SqlMgrFactory getInstance() {
        return sqlMFInst;
    }
}

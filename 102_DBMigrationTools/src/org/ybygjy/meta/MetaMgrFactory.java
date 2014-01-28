package org.ybygjy.meta;

import java.sql.Connection;
import java.util.List;

import org.ybygjy.meta.impl.MetaMgrImpl4MSSql;
import org.ybygjy.meta.impl.MetaMgrImpl4Oracle;


/**
 * MetaMgr��������װ�˾���ʵ����ʵ��
 * @author WangYanCheng
 * @version 2012-4-9
 */
public class MetaMgrFactory {
    /**
     * Singleton Pattern
     */
    private static MetaMgrFactory mmfInst = new MetaMgrFactory();

    /**
     * ȡMetaMgr Oracleʵ��
     * @return rtnMgr {@link MetaMgr}
     */
    public MetaMgr getMetaMgr4Oracle(Connection conn) {
        return new MetaMgrImpl4Oracle(conn);
    }

    /**
     * ȡMetaMgr MSSqlʵ��
     * @return rtnMgr {@link MetaMgr}
     */
    public MetaMgr getMetaMgr4MSSql(Connection conn) {
        return new MetaMgrImpl4MSSql(conn);
    }

    /**
     * ȡ����ʵ��
     * @return mmfInst {@link MetaMgrFactory}
     */
    public static final MetaMgrFactory getInstance() {
        return mmfInst;
    }
}

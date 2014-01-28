package org.ybygjy;

import java.sql.Connection;

import org.ybygjy.exec.SqlExecutor;
import org.ybygjy.exec.impl.SqlExecutorImpl4MSSql;
import org.ybygjy.exec.impl.SqlExecutorImpl4Oracle;
import org.ybygjy.meta.MetaMgr;
import org.ybygjy.meta.MetaMgrFactory;
import org.ybygjy.sql.SqlMgr;
import org.ybygjy.sql.SqlMgrFactory;
import org.ybygjy.stype.STypeSqlExecutor;
import org.ybygjy.stype.impl.STypeSqlExecutor4MSSql;
import org.ybygjy.stype.impl.STypeSqlExecutor4Oracle;

/**
 * ͳһ�������������Դ
 * @author WangYanCheng
 * @version 2012-10-22
 */
public class TestEnv {
    /** Oracleʵ�� */
    private MetaMgr srcMetaMgr;
    /** SQL Serverʵ�� */
    private MetaMgr tarMetaMgr;
    /** Oracleʵ�� */
    private SqlExecutor srcSqlExe;
    /** SQLServerʵ�� */
    private SqlExecutor tarSqlExe;
    /** Oracleʵ�� */
    private STypeSqlExecutor srcSTypeExe;
    /** SQL Serverʵ�� */
    private STypeSqlExecutor tarSTypeExe;
    /** Oracleʵ�� */
    private SqlMgr srcSqlMgr;
    /** SQL Serverʵ�� */
    private SqlMgr tarSqlMgr;

    public void buildOracle2Oracle(Connection srcConn, Connection tarConn) {
        srcMetaMgr = MetaMgrFactory.getInstance().getMetaMgr4Oracle(srcConn);
        srcSqlExe = new SqlExecutorImpl4Oracle(srcConn);
        srcSTypeExe = new STypeSqlExecutor4Oracle(srcConn);
        srcSqlMgr = SqlMgrFactory.getInstance().getSqlMgrImpl4Oracle();
        tarMetaMgr = MetaMgrFactory.getInstance().getMetaMgr4Oracle(tarConn);
        tarSqlExe = new SqlExecutorImpl4Oracle(tarConn);
        tarSTypeExe = new STypeSqlExecutor4Oracle(tarConn);
        tarSqlMgr = SqlMgrFactory.getInstance().getSqlMgrImpl4Oracle();
    }

    public void buildMSSql2Oracle(Connection srcConn, Connection tarConn) {
        srcMetaMgr = MetaMgrFactory.getInstance().getMetaMgr4MSSql(srcConn);
        srcSqlExe = new SqlExecutorImpl4MSSql(srcConn);
        srcSTypeExe = new STypeSqlExecutor4MSSql(srcConn);
        srcSqlMgr = SqlMgrFactory.getInstance().getSqlMgrImpl4MSSql();
        tarMetaMgr = MetaMgrFactory.getInstance().getMetaMgr4Oracle(tarConn);
        tarSqlExe = new SqlExecutorImpl4Oracle(tarConn);
        tarSTypeExe = new STypeSqlExecutor4Oracle(tarConn);
        tarSqlMgr = SqlMgrFactory.getInstance().getSqlMgrImpl4Oracle();
    }

    /**
     * @return the srcMetaMgr
     */
    public MetaMgr getSrcMetaMgr() {
        return srcMetaMgr;
    }

    /**
     * @return the tarMetaMgr
     */
    public MetaMgr getTarMetaMgr() {
        return tarMetaMgr;
    }

    /**
     * @return the srcSqlExe
     */
    public SqlExecutor getSrcSqlExe() {
        return srcSqlExe;
    }

    /**
     * @return the tarSqlExe
     */
    public SqlExecutor getTarSqlExe() {
        return tarSqlExe;
    }

    /**
     * @return the srcSTypeExe
     */
    public STypeSqlExecutor getSrcSTypeExe() {
        return srcSTypeExe;
    }

    /**
     * @return the tarSTypeExe
     */
    public STypeSqlExecutor getTarSTypeExe() {
        return tarSTypeExe;
    }

    /**
     * @return the srcSqlMgr
     */
    public SqlMgr getSrcSqlMgr() {
        return srcSqlMgr;
    }

    /**
     * @return the tarSqlMgr
     */
    public SqlMgr getTarSqlMgr() {
        return tarSqlMgr;
    }
}

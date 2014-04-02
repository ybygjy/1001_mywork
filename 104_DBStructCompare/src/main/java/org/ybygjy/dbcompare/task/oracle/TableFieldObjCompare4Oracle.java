package org.ybygjy.dbcompare.task.oracle;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.ybygjy.dbcompare.DBUtils;
import org.ybygjy.dbcompare.model.AbstractObjectFieldMeta;
import org.ybygjy.dbcompare.model.AbstractObjectMeta;
import org.ybygjy.dbcompare.model.MetaConstant;
import org.ybygjy.dbcompare.model.MetaType;
import org.ybygjy.dbcompare.task.AbstractTask;


/**
 * ���ֶζ���ṹ�Ƚ�
 * @author WangYanCheng
 * @version 2011-10-8
 */
public class TableFieldObjCompare4Oracle extends AbstractTask {
	/**�ֶ�ȱʧSQL*/
	private static String tmplLAFSql;
	static {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("SELECT * FROM (")
			.append("SELECT (CASE WHEN A_TAB IS NULL THEN '1' WHEN B_TAB IS NULL THEN '2' ELSE '-1' END) TYPE,")
			.append("(CASE WHEN A_TAB IS NULL THEN B_TAB ELSE A_TAB END) TABLE_NAME,")
			.append("(CASE WHEN A_COL IS NULL THEN B_COL ELSE A_COL END) TABLE_COLUMN")
			.append(" FROM (SELECT TABLE_NAME A_TAB, COLUMN_NAME A_COL FROM ALL_TAB_COLS A WHERE A.OWNER = '@SRC' AND EXISTS(SELECT 1 FROM ALL_TABLES D WHERE D.OWNER='@TAR' AND D.TABLE_NAME = A.TABLE_NAME)) A")
			.append(" FULL JOIN (SELECT TABLE_NAME B_TAB, COLUMN_NAME B_COL FROM ALL_TAB_COLS B WHERE B.OWNER = '@TAR' AND EXISTS(SELECT 1 FROM ALL_TABLES D WHERE D.OWNER='@SRC' AND D.TABLE_NAME = B.TABLE_NAME)) B")
			.append(" ON A.A_TAB = B.B_TAB AND A.A_COL = B.B_COL")
			.append(" ) C WHERE C.TYPE <> '-1'");
		tmplLAFSql = sbuf.toString();
	}
    /**
     * Constructor
     * @param srcUser Դ�û�
     * @param targetUser �����û�
     */
    public TableFieldObjCompare4Oracle(String srcUser, String targetUser) {
        super(srcUser, targetUser);
        getCommonModel().getTaskInfo().setTaskName("���ֶζ���ṹ�Ƚ�");
        getCommonModel().getTaskInfo().setTaskType(MetaType.TABLE_FIELDOBJ);
    }
    @Override
    public void execute() {
        beforeListener();
        Statement stmt = null;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            //ִ�д洢���̲ɼ��ֶαȽ�����
            execStoredProcedure();
            //ȡ�������߼����ദ��
            //�ֶ�ȱʧ/����ͳ��
            Map<String,Map<String,AbstractObjectMeta>> lostAndExcessMap = getLostAndExcessArr(stmt);
            //�ֶαȽ���ϸ
            Map<String,AbstractObjectMeta> fieldCompareDetail = getFieldCompareDetail(stmt);
            getCommonModel().putRawData(MetaConstant.OBJ_LOSTANDEXCESS, lostAndExcessMap);
            getCommonModel().putRawData(MetaConstant.OBJ_COMPAREDETAIL, fieldCompareDetail);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(stmt);
        }
        getCommonModel().getTaskInfo().setFinished(true);
        afterListener();
    }

    /**
     * ִ�д洢����
     * @throws SQLException �׳��쳣��Log
     */
    private void execStoredProcedure() throws SQLException {
        String sql = "{call SYSTEM.NSTC_DBCOMPARE4TABLECOL(?, ?)}";
        CallableStatement callStmt = conn.prepareCall(sql);
        callStmt.setString(1, getCommonModel().getTaskInfo().getSrcUser());
        callStmt.setString(2, getCommonModel().getTaskInfo().getTargetUser());
        callStmt.execute();
    }

    /**
     * �ֶ�ȱʧ/����ͳ��
     * @param stmt SQLִ�ж���
     * @return rtnMap rtnMap
     * @throws SQLException �׳��쳣��Log
     */
    private Map<String, Map<String, AbstractObjectMeta>> getLostAndExcessArr(Statement stmt) throws SQLException {
    	String sql = tmplLAFSql.replaceAll("@SRC", srcUser).replaceAll("@TAR", targetUser);
        ResultSet rs = stmt.executeQuery(sql);
        Map<String, AbstractObjectMeta> lostArr = new HashMap<String, AbstractObjectMeta>();
        Map<String, AbstractObjectMeta> excessArr = new HashMap<String, AbstractObjectMeta>();
        while (rs.next()) {
            int type = rs.getInt("TYPE");
            String key = rs.getString("TABLE_NAME");
            String fieldCode = rs.getString("TABLE_COLUMN");
            if (MetaConstant.FLAG_LOST == type) {
                addTableField(lostArr, key, fieldCode);
            } else {
                addTableField(excessArr, key, fieldCode);
            }
        }
        rs.close();
        Map<String, Map<String, AbstractObjectMeta>> rtnMap = new HashMap<String, Map<String,AbstractObjectMeta>>();
        rtnMap.put(MetaConstant.OBJ_LOST, lostArr);
        rtnMap.put(MetaConstant.OBJ_EXCESS, excessArr);
        return rtnMap;
    }
    /**
     * ȡ�ֶζ���Ƚ���ϸ
     * @param stmt SQLִ��ʵ��
     * @return rtnMap rtnMap
     * @throws SQLException �׳��쳣��Log
     */
    private Map<String, AbstractObjectMeta> getFieldCompareDetail (Statement stmt) throws SQLException {
        //ȡ�ֶαȽϽ����
        String sql = "SELECT TABLE_NAME, FIELD_NAME, FIELD_TYPE, FIELD_LEN, FIELD_PRE, FIELD_SCA, FIELD_NULL, FIELD_DEFVLEN, FIELD_DEFVALUE FROM SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL ORDER BY TABLE_NAME,FIELD_NAME";
        ResultSet rs = stmt.executeQuery(sql);
        Map<String, AbstractObjectMeta> rtnMap = new HashMap<String, AbstractObjectMeta>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            AbstractObjectMeta tableObj = rtnMap.containsKey(tableName) ? rtnMap.get(tableName) : new AbstractObjectMeta(tableName);
            AbstractObjectFieldMeta tfoInst = new AbstractObjectFieldMeta();
            tfoInst.setTableObj(tableObj);
            tfoInst.loadData(rs);
            tableObj.addField(tfoInst);
            if (!rtnMap.containsKey(tableName)) {
                rtnMap.put(tableName, tableObj);
            }
        }
        return rtnMap;
    }
    /**
     * ��ӱ��ֶ�
     * @param map
     * @param tableName
     * @param fieldCode
     */
    private void addTableField(Map<String, AbstractObjectMeta> map, String tableName, String fieldCode) {
        if (map.containsKey(tableName)) {
            map.get(tableName).addField(fieldCode);
        } else {
            AbstractObjectMeta to = new AbstractObjectMeta(tableName);
            to.addField(fieldCode);
            map.put(tableName, to);
        }
    }
}

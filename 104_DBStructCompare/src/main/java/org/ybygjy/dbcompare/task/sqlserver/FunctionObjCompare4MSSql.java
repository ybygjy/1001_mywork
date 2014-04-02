package org.ybygjy.dbcompare.task.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ybygjy.dbcompare.DBUtils;
import org.ybygjy.dbcompare.model.AbstractObjectMeta;
import org.ybygjy.dbcompare.model.MetaConstant;
import org.ybygjy.dbcompare.model.MetaType;
import org.ybygjy.dbcompare.task.AbstractTask;


/**
 * ��������ṹ�Ƚ�
 * @author WangYanCheng
 * @version 2011-10-14
 */
public class FunctionObjCompare4MSSql extends AbstractTask {
	/**��������ȱʧ/������ϸ��ѯSQL*/
	private static String tmplLAESql = "SELECT * FROM (SELECT (CASE WHEN A.FUN_NAME IS NULL THEN '1' WHEN B.FUN_NAME IS NULL THEN '2' ELSE -1 END) TYPE, (CASE WHEN A.FUN_NAME IS NULL THEN B.FUN_NAME WHEN B.FUN_NAME IS NULL THEN A.FUN_NAME ELSE A.FUN_NAME END) FUN_NAME FROM (SELECT UPPER(NAME) AS FUN_NAME FROM @SRC.SYS.OBJECTS WHERE TYPE = 'FN') A FULL JOIN (SELECT UPPER(NAME) AS FUN_NAME FROM @TAR.SYS.OBJECTS WHERE TYPE = 'FN')B ON A.FUN_NAME = B.FUN_NAME) C WHERE C.TYPE <> '-1'";
	/**
	 * ��������ṹ�ȽϹ��캯��
	 * @param srcUser ԭʼ�û�
	 * @param targetUser �����û�
	 */
	public FunctionObjCompare4MSSql(String srcUser, String targetUser) {
		super(srcUser, targetUser);
		getCommonModel().getTaskInfo().setTaskName("��������ṹ�Ƚ�");
		getCommonModel().getTaskInfo().setTaskType(MetaType.FUNC_OBJ);
	}

	@Override
	public void execute() {
		beforeListener();
		Statement stmt = null;
		try {
			stmt = DBUtils.createStmt(conn);
			getCommonModel().putRawData(MetaConstant.OBJ_LOSTANDEXCESS, queryObjLostAndExcess(stmt));
		} catch (SQLException e) {
			getCommonModel().putRawData(MetaConstant.OBJ_LOSTANDEXCESS, null);
			e.printStackTrace();
		} finally {
			DBUtils.close(stmt);
		}
		getCommonModel().getTaskInfo().setFinished(true);
		afterListener();
	}

	/**
	 * ��ѯ����ȱʧ/������ϸ
	 * @param stmt SQL���ִ��ʵ��
	 * @return rtnMap rtnMap
	 * @throws SQLException �׳��쳣��Log
	 */
	private Map<String, List<AbstractObjectMeta>> queryObjLostAndExcess(Statement stmt) throws SQLException {
		ResultSet rs = stmt.executeQuery(tmplLAESql.replaceAll("@SRC", srcUser).replaceFirst("@TAR", targetUser));
		List<AbstractObjectMeta> lostList = new ArrayList<AbstractObjectMeta>();
		List<AbstractObjectMeta> excessList = new ArrayList<AbstractObjectMeta>();
		while (rs.next()) {
			int type = rs.getInt("TYPE");
			String objName = rs.getString("FUN_NAME");
			AbstractObjectMeta tmpObj = new AbstractObjectMeta(objName);
			tmpObj.setObjectType(MetaType.PROC_OBJ);
			boolean flag = (MetaConstant.FLAG_LOST == type) ? lostList.add(tmpObj) : excessList.add(tmpObj);
			if (!flag) {
				System.out.println("�ڲ��߼���������ϵ����Ա��");
			}
		}
		Map<String, List<AbstractObjectMeta>> rtnMap = new HashMap<String, List<AbstractObjectMeta>>();
		rtnMap.put(MetaConstant.OBJ_LOST, lostList);
		rtnMap.put(MetaConstant.OBJ_EXCESS, excessList);
		return rtnMap;
	}
}

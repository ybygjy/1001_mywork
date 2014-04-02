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
 * ��ͼ����ṹ�Ƚ�����
 * @author WangYanCheng
 * @version 2011-10-14
 */
public class ViewObjCompare4MSSql extends AbstractTask {
	/**ͳ�ƶ�������*/
	private static String tmplCountSql = "SELECT * FROM (SELECT COUNT(1) @SRC FROM @SRC.SYS.VIEWS)A, (SELECT COUNT(1) @TAR FROM @TAR.SYS.VIEWS) B;";
	/**����ȱʧ��ϸ*/
	private static String tmplLAESql = "SELECT '@T' TYPE,A.OBJECT_ID, UPPER(A.NAME) AS TABLE_NAME FROM @SRC.SYS.VIEWS A WHERE NOT EXISTS(SELECT 1 FROM @TAR.SYS.VIEWS B WHERE B.NAME = A.NAME)";
	/**
	 * ��ͼ����ṹ�Ƚ�����
	 * @param srcUser ԭʼ�û�
	 * @param targetUser �����û�
	 */
	public ViewObjCompare4MSSql(String srcUser, String targetUser) {
		super(srcUser, targetUser);
		getCommonModel().getTaskInfo().setTaskName("��ͼ����ṹ�Ƚ�����");
		getCommonModel().getTaskInfo().setTaskType(MetaType.VIEW_OBJ);
	}

	@Override
	public void execute() {
		beforeListener();
		Statement stmt = null;
		try {
			stmt = DBUtils.createStmt(conn);
			getCommonModel().putRawData(MetaConstant.OBJ_COUNT, queryObjCount(stmt));
			getCommonModel().putRawData(MetaConstant.OBJ_LOSTANDEXCESS, queryObjLostAndExcess(stmt));
		} catch (SQLException e) {
			getCommonModel().putRawData(MetaConstant.OBJ_COUNT, null);
			getCommonModel().putRawData(MetaConstant.OBJ_LOSTANDEXCESS, null);
			e.printStackTrace();
		} finally {
			DBUtils.close(stmt);
		}
		getCommonModel().getTaskInfo().setFinished(true);
		afterListener();
	}

	/**
	 * �����ȱʧ/������ϸ
	 * @param stmt SQL���ִ����
	 * @return rtnMap rtnMap
	 * @throws SQLException �׳��쳣��Log
	 */
	private Map<String, List<AbstractObjectMeta>> queryObjLostAndExcess(Statement stmt) throws SQLException {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(tmplLAESql.replaceFirst("@T", "1").replaceFirst("@SRC", targetUser).replaceFirst("@TAR", srcUser))
			.append(" UNION ALL ")
			.append(tmplLAESql.replaceFirst("@T", "2").replaceFirst("@SRC", srcUser).replaceFirst("@TAR", targetUser));
		ResultSet rs = stmt.executeQuery(sbuf.toString());
		List<AbstractObjectMeta> lostList = new ArrayList<AbstractObjectMeta>();
		List<AbstractObjectMeta> excessList = new ArrayList<AbstractObjectMeta>();
		while (rs.next()) {
			int type = rs.getInt("TYPE");
			String tableName = rs.getString("TABLE_NAME");
			String objId = rs.getString("OBJECT_ID");
			AbstractObjectMeta omInst = new AbstractObjectMeta(tableName);
			omInst.setObjectType(MetaType.VIEW_OBJ);
			omInst.setObjectId(objId);
			if (MetaConstant.FLAG_LOST == type) {
				lostList.add(omInst);
			} else {
				excessList.add(omInst);
			}
		}
		Map<String, List<AbstractObjectMeta>> rtnMap = new HashMap<String, List<AbstractObjectMeta>>();
		rtnMap.put(MetaConstant.OBJ_LOST, lostList);
		rtnMap.put(MetaConstant.OBJ_EXCESS, excessList);
		return rtnMap;
	}

	/**
	 * ��ѯ��������ͳ��
	 * @param stmt SQL���ִ����
	 * @return rtnMap rtnMap
	 * @throws SQLException �׳��쳣��Log
	 */
	private Map<String, String> queryObjCount(Statement stmt) throws SQLException {
		ResultSet rs = stmt.executeQuery(tmplCountSql.replaceAll("@SRC", srcUser).replaceAll("@TAR", targetUser));
		Map<String, String> rtnMap = new HashMap<String, String>();
		if (rs.next()) {
			rtnMap.put(srcUser, rs.getString(srcUser));
			rtnMap.put(targetUser, rs.getString(targetUser));
		}
		return rtnMap;
	}

}

package org.ybygjy.dbcompare.model;

/**
 * �������Լ��
 * @author WangYanCheng
 * @version 2011-10-17
 */
public class ConstraintMeta {
	/**Լ������*/
	private String consName;
	/**Լ������{����\���\CHECK\UNIQUE\etc}*/
	private String consType;
	/**��������*/
	private AbstractObjectMeta dbomInst;
	/**
	 * Constraint
	 * @param consName Լ������
	 */
	public ConstraintMeta(String consName){
		this.consName = consName;
	}
	/**
	 * ȡԼ������
	 * @return
	 */
	public String getConsName() {
		return consName;
	}
	/**
	 * �洢Լ������
	 * @param consName Լ������
	 */
	public void setConsName(String consName) {
		this.consName = consName;
	}

	/**
	 * ȡԼ������
	 * @return consType rtnConsType
	 */
	public String getConsType() {
		return consType;
	}

	/**
	 * �洢Լ������
	 * @param consType Լ������
	 */
	public void setConsType(String consType) {
		this.consType = consType;
	}

	/**
	 * ȡ��������ʵ��
	 * @return rtnDBOMInst rtnDBOMInst
	 */
	public AbstractObjectMeta getDbomInst() {
		return dbomInst;
	}

	/**
	 * �洢��������ʵ��
	 * @param dbomInst ����ʵ��
	 */
	public void setDbomInst(AbstractObjectMeta dbomInst) {
		this.dbomInst = dbomInst;
	}
}

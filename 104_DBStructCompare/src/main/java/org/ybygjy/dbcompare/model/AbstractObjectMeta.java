package org.ybygjy.dbcompare.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ��������ṹ
 * @author WangYanCheng
 * @version 2011-10-5
 */
public class AbstractObjectMeta {
	private String objectId;
    private String objectName;
    private String owner;
    /** ��������{TABLE:VIEW:PRO:FUN:ETC} */
    private MetaType objectType;
    /**����*/
    private List<AbstractObjectFieldMeta> objectFieldArr;
    /**��������*/
    private List<TriggerMeta> triggerObjArr;
    /**Լ����*/
    private List<ConstraintMeta> constraintArr;
    /**
     * Constructor
     */
    public AbstractObjectMeta() {
        objectFieldArr = new ArrayList<AbstractObjectFieldMeta>();
        triggerObjArr = new ArrayList<TriggerMeta>();
        constraintArr = new ArrayList<ConstraintMeta>();
    }

    public AbstractObjectMeta(String objName) {
        this();
        this.objectName = objName;
    }
    public AbstractObjectMeta(String objName, MetaType tableType) {
        this(objName);
        this.objectType = tableType;
    }
    /**
     * ����ֶζ���
     * @param fieldCode �ֶα���
     * @return �ֶζ���
     */
    public AbstractObjectFieldMeta addField(String fieldCode) {
        AbstractObjectFieldMeta tfo = new AbstractObjectFieldMeta(this, fieldCode);
        this.objectFieldArr.add(tfo);
        return tfo;
    }

    /**
     * ����ֶζ���
     * @param tfoInst �ֶζ���
     */
    public void addField(AbstractObjectFieldMeta tfoInst) {
        getObjectFieldArr().add(tfoInst);
    }

    /**
     * ��Ӵ���������
     * @param triggObj ����������
     */
    public void addTrigger(TriggerMeta triggObj) {
        getTriggerObjArr().add(triggObj);
    }

    /**
     * @return the objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @param objectName the objectName to set
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * @return the tableFieldArr
     */
    public List<AbstractObjectFieldMeta> getObjectFieldArr() {
        return objectFieldArr;
    }

    /**
     * @param tableFieldArr the tableFieldArr to set
     */
    public void setObjectFieldArr(List<AbstractObjectFieldMeta> tableFieldArr) {
        this.objectFieldArr = tableFieldArr;
    }

    public List<TriggerMeta> getTriggerObjArr() {
        return triggerObjArr;
    }

    public void setTriggerObjArr(List<TriggerMeta> triggerObjArr) {
        this.triggerObjArr = triggerObjArr;
    }

    /**
     * ȡ���������
     * @return objectType {table/view/etc}
     */
    public MetaType getObjectType() {
        return objectType;
    }

    /**
     * �洢���������
     * @param objectType objectType
     */
    public void setObjectType(MetaType objectType) {
        this.objectType = objectType;
    }
    /**
     * �������
     * @param owner owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    /**
     * ȡ�������
     * @return owner owner
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * ��������
     * @return objectId objectId
     */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * �洢��������
	 * @param objectId objectId
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * ȡ����Լ����
	 * @return rtnConArr ����Լ����
	 */
	public List<ConstraintMeta> getConstraintArr() {
		return constraintArr;
	}

	/**
	 * �洢����Լ����
	 * @param constraintArr ����Լ����
	 */
	public void setConstraintArr(List<ConstraintMeta> constraintArr) {
		this.constraintArr = constraintArr;
	}

	/**
	 * ׷�Ӷ���Լ��
	 * @param dbConsInst ����Լ��
	 */
	public void addConstraint(ConstraintMeta dbConsInst) {
		getConstraintArr().add(dbConsInst);
	}
}

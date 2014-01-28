package org.ybygjy.util;

import org.ybygjy.BusinessException;

/**
 * ���ݿ�������Ϣ
 * @author WangYanCheng
 * @version 2012-11-15
 */
public class DBConfEntry {
    private DB_TYPE dbType;
    private String connUrl;
    private String userName;
    private String passWord;
    /** ��Ǹ�ʵ���Ƿ��Ѿ���֤ͨ�����������������ݿ����� */
    private boolean vaild;

    /**
     * @return the dbType
     */
    public DB_TYPE getDbType() {
        return dbType;
    }

    /**
     * @param dbType the dbType to set
     */
    public void setDbType(DB_TYPE dbType) {
        this.dbType = dbType;
    }

    /**
     * @return the connUrl
     */
    public String getConnUrl() {
        return connUrl;
    }

    /**
     * @param connUrl the connUrl to set
     */
    public void setConnUrl(String connUrl) {
        this.connUrl = connUrl;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the passWord
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * @param passWord the passWord to set
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * @return the vaild
     */
    public boolean isVaild() {
        return vaild;
    }

    /**
     * @param vaild the vaild to set
     */
    public void setVaild(boolean vaild) {
        this.vaild = vaild;
    }

    /**
     * ���������ݿ������
     * @return rtnFlag {true:��������;false:����ʧ��}
     * @throws BusinessException ҵ���쳣
     */
    public boolean test() throws BusinessException {
        return preTest() ? DBUtils.testConnection(this) : false;
    }

    private boolean preTest() throws BusinessException {
        if (getDbType() == DB_TYPE.UNKNOW) {
            throw new BusinessException("���ݿ��������ʹ���");
        } else if (getConnUrl() == null || "".equals(getConnUrl())) {
            throw new BusinessException("���ݿ����Ӵ�Ϊ�գ�");
        } else if (getUserName() == null || "".equals(getUserName())) {
            throw new BusinessException("�û���Ϊ�գ�");
        } else if (getPassWord() == null || "".equals(getPassWord())) {
            throw new BusinessException("����Ϊ�գ�");
        }
        return true;
    }

    @Override
    public String toString() {
        return "DBConfEntry [dbType=" + dbType + ", connUrl=" + connUrl + ", userName=" + userName
            + ", passWord=" + passWord + ", vaild=" + vaild + "]";
    }
}

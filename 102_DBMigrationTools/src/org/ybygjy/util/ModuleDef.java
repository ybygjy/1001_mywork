package org.ybygjy.util;

/**
 * Ǩ��ģ�鶨��
 * @author WangYanCheng
 * @version 2012-10-22
 */
public enum ModuleDef {
    FBCM(10),RMS(11),GREPORT(12),DBMS(13),EDC(14),BMS(20),GDMS(21);
    /**����Ψһֵ*/
    private int val;
    /**Ŀ¼·��*/
    private String dir;
    private ModuleDef(int val) {
        this.val = val;
    }
    public void setDir(String dir) {
        this.dir = dir;
    }
    public String getDir() {
        return this.dir;
    }
    public int getValue() {
        return val;
    }
}

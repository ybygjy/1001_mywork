package org.ybygjy.basic.properties;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * �ⲿ���ü��ز���
 * <p>1�������ļ����Ƹ�ʽbaseName_���Դ���_���Ҵ���.properties</p>
 * <p>2��ע��native2assii</p>
 * @author WangYanCheng
 * @version 2009-12-7
 */
public class PropertiesTest {
    /**resource boundle inst*/
    private static ResourceBundle resBoundInst = null;
    /**
     * constructor
     */
    public PropertiesTest() {
        resBoundInst =
            ResourceBundle.getBundle("org.ybygjy.basic.properties.defaults");
    }
    /**
     * show parameters
     */
    public void doShowParameters() {
        Enumeration enumObj = resBoundInst.getKeys();
        while (enumObj.hasMoreElements()) {
            String key = (String) enumObj.nextElement();
            System.out.println(key + ":" + resBoundInst.getString(key));
        }
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        PropertiesTest proTestInst = new PropertiesTest();
        proTestInst.doShowParameters();
    }
}

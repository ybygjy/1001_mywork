package org.ybygjy.basic.ch_110_generic;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * ���÷�����Ʒ���������
 * @author WangYanCheng
 * @version 2014-6-16
 */
public class GenericReflection {
    /** ������*/
    private String className;

    public GenericReflection(String className) {
        super();
        this.className = className;
    }
    public void doWork() {
        //������
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //������ṹ
        parseClass(clazz);
        //ȡ�෽������
        //������
    }
    private void parseClass(Class clazz) {
        StringBuilder sbud = new StringBuilder();
        sbud.append(Modifier.toString(clazz.getModifiers())).append(" ")
            .append((clazz.isInterface() ? "interface " : (clazz.isPrimitive() ? "" : "class ")))
            .append(clazz.getName()).append(buildTypes(clazz.getTypeParameters(), "<", ",", ">"));
        
        System.out.println(sbud.toString());
    }
    /**
     * ���������ִ�
     * @param types ������
     * @param prefix ǰ׺
     * @param seprator �ָ�
     * @param suffix ��׺
     * @return rtnStr
     */
    private String buildTypes(Type[] types, String prefix, String seprator, String suffix) {
        if (types.length == 0) {
            return "";
        }
        StringBuilder sbud = new StringBuilder(prefix);
        for (Type type : types) {
            sbud.append(buildType(type)).append(seprator);
        }
        sbud.append(suffix);
        return sbud.toString();
    }
    /**
     * ������������ִ�
     * @param type
     * @return
     */
    private String buildType(Type type) {
        StringBuilder sbud = new StringBuilder();
        if (type instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) type;
            sbud.append(((TypeVariable) type).getName());
        }
        return sbud.toString();
    }
    public static void main(String[] args) {
        new GenericReflection(Pair.class.getName()).doWork();
    }
}

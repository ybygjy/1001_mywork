package org.ybygjy.jndi.ldap;

import java.util.Iterator;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

/**
 * Attribute����LDAPʵ��
 * @author WangYanCheng
 * @version 2011-4-29
 */
public class AttributeMgr4LDAP {
    /** ������� */
    private DirContext dirCtx;

    /**
     * ���캯��
     * @param dirCtx Ŀ¼�������
     */
    public AttributeMgr4LDAP(DirContext dirCtx) {
        this.dirCtx = dirCtx;
    }
    /**
     * �����޸ĸ���DN��Attribute
     * <p>��replace����ʱ��������һ���������ϸ��{@linkplain AttributeMgr4LDAPTest#testModifyAttr()}</p>
     * <p><strong>ע�⣺</strong>attribute type undefined����</p>
     * @param dn ָ��DN
     * @param flag �������
     * @param attrMap ���Լ�
     * @see DirContext.REPLACE_ATTRIBUTE��DirContext.ADD_ATTRIBUTE��DirContext.REMOVE_ATTRIBUTE
     */
    public void modifyAttr(String dn, int flag, Map<String, Object> attrMap) {
        ModificationItem[] modifyArray = new ModificationItem[attrMap.size()];
        int i = 0;
        for (Iterator<Map.Entry<String, Object>> iterator = attrMap.entrySet().iterator(); iterator
            .hasNext();) {
            Map.Entry<String, Object> entry = iterator.next();
            modifyArray[i++] = new ModificationItem(flag, new BasicAttribute(entry.getKey(), entry
                .getValue()));
        }
        modifyAttr(dn, modifyArray);
    }
    /**
     * �����޸ĸ���DN��Attribute
     * @param dn ָ��DN
     * @param flag �������
     * @param attributes ���Լ�
     */
    public void modifyAttr(String dn, int flag, Attributes attributes) {
        try {
            dirCtx.modifyAttributes(dn, flag, attributes);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    /**
     * �޸�ָ��DN��attribute
     * @param dn ָ��DN
     * @param key attribute����
     * @param value ֵ
     */
    public void updateAttr(String dn, String key, Object value) {
        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(key, value));
        modifyAttr(dn, mods);
    }

    /**
     * ��ָ��dn���Attribute
     * @param dn ָ��dn
     * @param key attributeId
     * @param value attributeValue
     */
    public void addAttr(String dn, String key, Object value) {
        ModificationItem[] mods = new ModificationItem[] {new ModificationItem(DirContext.ADD_ATTRIBUTE,
            new BasicAttribute(key, value))};
        modifyAttr(dn, mods);
    }

    /**
     * ɾ��ָ��dn��attribute
     * @param dn ָ��dn
     * @param name attribute ID
     * @param value attribute Value�ɴ���name��������⣬�磺
     * <ul>
     *  <li>dn:cn=userName,dc=daowoo,dc=com</li>
     *  <li>sn:Kevin</li>
     *  <li>sn:John</li>
     * </ul>
     */
    public void removeAttr(String dn, String key, String value) {
        BasicAttribute tmpAttr = new BasicAttribute(key);
        if (value != null) {
            tmpAttr.add(value);
        }
        ModificationItem[] mods = new ModificationItem[] {new ModificationItem(DirContext.REMOVE_ATTRIBUTE, tmpAttr)};
        modifyAttr(dn, mods);
    }

    /**
     * ��ѯָ��DN��Attribute��֧�ֹ��� <strong>ʾ����</strong>
     * <p>
     * Attributes attrArray = new AttributeMgr4LDAP(dirCtx).qryAttr("o=hr,dc=daowoo,dc=com", new String[]{"id","name"});
     * </p>
     * <p>
     * <strong>ע�⣺</strong>������ֵ�ĸ��Ĵ�����������
     * </p>
     * @param dn ָ��DN
     * @param filters ������
     */
    public Attributes qryAttr(String dn, String[] filters) {
        Attributes attrList = null;
        try {
            if (null == filters) {
                attrList = dirCtx.getAttributes(dn);
            } else {
                attrList = dirCtx.getAttributes(dn, filters == null ? new String[0] : filters);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return attrList;
    }

    /**
     * ��װAttribute�޸�{CRUD}
     * @param dn ָ��dn
     * @param mods �޸�ʵ��
     */
    private void modifyAttr(String dn, ModificationItem[] mods) {
        try {
            dirCtx.modifyAttributes(dn, mods);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}

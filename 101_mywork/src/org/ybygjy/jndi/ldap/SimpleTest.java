package org.ybygjy.jndi.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

/**
 * ������֤һЩ���ǵ�API
 * @author WangYanCheng
 * @version 2011-6-9
 */
public class SimpleTest {
    private DirContext dirCtx;
    private String dn = "ou=People,dc=daowoo,dc=com";

    /**
     * Constructor
     * @param dirCtx dirCtx
     */
    public SimpleTest(DirContext dirCtx) {
        this.dirCtx = dirCtx;
    }
    /**
     * ��֤��ȡָ����Ŀ�����ԣ�С��
     * <p>1������Ϊ�գ����ʾȡ��Ŀ�������ԣ�</p>
     * <p>2������ָ�����������飬����ȡ��ĳһС�����ԣ�</p>
     */
    public void testGetAttr() {
        String tmpDN = "cn=WangYanCheng,".concat(dn);
        try {
            Attributes attrs = this.dirCtx.getAttributes(tmpDN, null);
            LDAPUtils.printAttr((NamingEnumeration<Attribute>) attrs.getAll());
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���Բ�ѯAPI��С��
     * <p>
     * 1��search��ЩAPI������������������ģ�����������DN����һ��Ҷ�ӡ���N��Ҷ��;
     * </p>
     * <p>
     * 2����ѯ�Ķ�����Ը���DN��Ҷ�ӣ�������DN���������
     * </p>
     * <p>
     * 3����������ز�ѯ����������Ҷ��
     * </p>
     */
    public void testSearch() {
        try {
            Attributes attrs = new BasicAttributes(true);
            attrs.put(new BasicAttribute("cn", "WangYanCheng"));
            NamingEnumeration<SearchResult> searchResultEnum = this.dirCtx.search(dn, attrs,
                new String[] {"userPassword"});
            // LDAPUtils.printEnum(searchResultEnum);
            if (searchResultEnum.hasMore()) {
                SearchResult searchResult = searchResultEnum.next();
                LDAPUtils.printAttr((NamingEnumeration<Attribute>) searchResult.getAttributes().getAll());
            }

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        SimpleTest verifyInst = new SimpleTest(Constant.createCtx());
        verifyInst.testSearch();
        verifyInst.testGetAttr();
    }
}

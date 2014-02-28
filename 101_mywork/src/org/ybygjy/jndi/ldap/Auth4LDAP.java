package org.ybygjy.jndi.ldap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * LDAP��֤
 * @author WangYanCheng
 * @version 2011-5-12
 */
public class Auth4LDAP {
    /**LDAP�����ķ������*/
    private DirContext dirCtx;
    /**
     * Constructor
     * @param dirCtx dirCtx
     */
    public Auth4LDAP(DirContext dirCtx) {
        this.dirCtx = dirCtx;
    }
    /**
     * ��֤(��ѯ/�󶨷�ʽ)
     * @param userCode �û�����
     * @param passwd ����
     * @return {true����֤ͨ��;false����֤ʧ��}
     */
    public boolean auth4SBind(String userCode, String passwd) {
        boolean rtnFlag = false;
        //��ѯ
        String filterStr = "(&(uid=@UID@)(userPassword=*))".replaceAll("@UID@", userCode);
        SearchControls scInst = new SearchControls();
        scInst.setSearchScope(SearchControls.SUBTREE_SCOPE);
        scInst.setReturningAttributes(new String[]{""});
        Context tmpCtx = null;
        try {
            NamingEnumeration<SearchResult> resultEnum = dirCtx.search(Constant.BASEDN, filterStr, scInst);
            SearchResult searchResult = null;
            if (resultEnum.hasMore()) {
                searchResult = resultEnum.next();
                //������֤
                tmpCtx = Constant.createCtx(searchResult.getNameInNamespace(), passwd);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (tmpCtx != null) {
                rtnFlag = true;
                try {
                    tmpCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnFlag;
    }
    /**
     * �����֤(ʹ��ֱ�Ӱ�LDAP����������)
     * @param userDn ȫ����Ŀ��ַ
     * @param password ����
     * @return {true:��֤�ɹ�;false:��֤ʧ��}
     */
    public boolean auth4Bind(String userDn, String password) {
        return (null != Constant.createCtx(userDn, password));
    }
    /**
     * ȡ��ĳ���Ե�ֵ
     * @param attr �������Զ���
     * @return ����ֵ,����Զ��ŷָ�
     * @throws NamingException NamingException
     */
    public String getAttrValue(Attribute attr) throws NamingException {
        NamingEnumeration nameEnum = attr.getAll();
        StringBuilder sbud = new StringBuilder();
        while (nameEnum.hasMore()) {
            sbud.append(nameEnum.next().toString()).append(",");
        }
        return sbud.toString();
    }
}

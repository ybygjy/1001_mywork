package org.ybygjy.jndi.ldap;

import java.util.Iterator;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * ��װJNDI LDAPʵ��
 * @author WangYanCheng
 * @version 2010-12-31
 */
public class ContextMgr4LDAP {
    /** Context */
    private DirContext ctx;
    /**
     * InnerCompiler
     */
    static class InnerClass {
        /** Singleton Pattern */
        public static ContextMgr4LDAP cm4LDAP = new ContextMgr4LDAP();
    }

    /**
     * ����
     * Constructor
     */
    private ContextMgr4LDAP() {
        this.ctx = Constant.createCtx();
    }

    /**
     * �޸ĸ���DN��Ŀ������
     * <p><strong>ע�⣺��Ŀ����֧��userPassword����</strong></p>
     * @param dn ����DN
     * @param passwd ����
     * @param type ����洢��ʽ{MD5,SHA,etc}
     * @return rtnFlag {true���ɹ���false��ʧ��}
     */
    public boolean modifyPassword(String dn, String passwd, int type) {
        Attributes attrs = new BasicAttributes();
        attrs.put("userPassword", LDAPUtils.getOpenLDAPMD5(passwd));
        boolean rtnFlag = false;
        try {
            this.ctx.modifyAttributes(dn, DirContext.REPLACE_ATTRIBUTE, attrs);
            rtnFlag = true;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return rtnFlag;
    }

    /**
     * doRenaming
     * @param oldName oldName
     * @param newName newName
     * @return true/false
     */
    public boolean doRenaming(String oldName, String newName) {
        boolean rtnBool = false;
        try {
            ctx.rename(oldName, newName);
            rtnBool = true;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return rtnBool;
    }

    /**
     * doLookup
     * @param name name
     * @return rtnObj/null
     */
    public Object lookup(String name) {
        Object rtnObj = null;
        try {
            rtnObj = ctx.lookup(name);
        } catch (NamingException e) {
            System.err.println("Exception ContextMgr4LDAP#lookup\t".concat(e.getMessage()));
        }
        return rtnObj;
    }
    /**
     * ��ָ��dn�²�ѯ����ָ��attribute�Ķ���
     * @param dn ָ��dn
     * @param param ���Լ���
     * @return resultEnum {@link SearchResult}
     */
    public NamingEnumeration<SearchResult> search(String dn, Map<String, Object> param) {
        Attributes matchAttrs = new BasicAttributes(true);
        if (param != null) {
            Map.Entry<String, Object> tmpEntry = null;
            for (Iterator<Map.Entry<String, Object>> iterator = param.entrySet().iterator(); iterator.hasNext();) {
                tmpEntry = iterator.next();
                matchAttrs.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        NamingEnumeration<SearchResult> resultEnum = null;
        try {
            resultEnum = this.ctx.search(dn, matchAttrs);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return resultEnum;
    }

    /**
     * ��ָ��DN�²�ѯ���Ϲ��˱��ʽ���˵Ķ���
     * @param dn ָ��DN
     * @param filterExp ���˱��ʽ
     * @return resultEnum {@link SearchResult}
     */
    public NamingEnumeration<SearchResult> search(String dn, String filterExp) {
        return search(dn, filterExp, null, SearchControls.SUBTREE_SCOPE);
    }

    /**
     * ��ѯָ��DN���Ҿ����˱��ʽ���˵Ķ���
     * @param dn ָ��DN
     * @param filterExp ���˱��ʽ
     * @param filterAttr ָ����ȡ�Ķ�������
     * @param searchScope ��ѯ��Χ
     * @return resultEnum {@link SearchResult}
     */
    public NamingEnumeration<SearchResult> search(String dn, String filterExp, String[] filterAttr, int searchScope) {
        SearchControls sctls = new SearchControls();
        if (filterAttr != null) {
            sctls.setReturningAttributes(filterAttr);
        }
        sctls.setSearchScope(searchScope);
        return search(dn, filterExp, sctls);
    }

    /**
     * ��ѯָ��DN���Ҿ����˱��ʽ���˵Ķ���
     * @param dn ָ��DN
     * @param filterExp ���˱��ʽ
     * @param searchControl ���ö���
     * @return resultEnum {@link SearchResult}
     */
    public NamingEnumeration<SearchResult> search(String dn, String filterExp, SearchControls searchControl) {
        NamingEnumeration<SearchResult> resultEnum = null;
        try {
            resultEnum = this.ctx.search(dn, filterExp, searchControl);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return resultEnum;
    }
    /**
     * ȡ��ָ���������������
     * @param name �����ַ
     * @return ���Լ�
     */
    public Attributes getAttributes(String name) {
        Attributes rtnAttr = null;
        try {
            rtnAttr = this.ctx.getAttributes(name);
        } catch (NamingException ne) {
            ne.printStackTrace();
        }
        return rtnAttr;
    }

    /**
     * ȡ��ָ��Ŀ¼���µĶ���
     * <p><strong>ע�⣺</strong>��ctx.list()������subCtx����¿�ָ��""������baseCtx����²�����</p>
     * @param name Ŀ¼
     * @return nameEnum Ŀ¼���󼯺�
     * @throws NamingException NamingException
     */
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        NamingEnumeration<NameClassPair> nameEnum = null;
        try {
            nameEnum = ctx.list(name);
        } catch (NameNotFoundException e) {
            System.err.println(e.getMessage().concat("\t").concat(name));
        }
        return nameEnum;
    }

    /**
     * ȡ��ǰ�����Ļ�����Ŀ¼�µĶ���
     * <p>�����Ϊ��ȫ�ֶ���������Լ����ǲ���</p>
     * <p><strong>ע�⣺</strong>�÷�����ȽϺ�ʱ</p>
     * @param name Ŀ¼
     * @return Ŀ¼���󼯺�
     */
    public NamingEnumeration<Binding> listBinding(String name) {
        NamingEnumeration<Binding> nameEnum = null;
        try {
            nameEnum = ctx.listBindings(name);
        } catch (NamingException ne) {
            ne.printStackTrace();
        }
        return nameEnum;
    }

    /**
     * �󶨶���
     * @param key key
     * @param obj value
     * @return true/false
     */
    public boolean bind(String key, Object obj) {
        boolean rtnBool = false;
        if (null == this.ctx) {
            return rtnBool;
        }
        try {
            this.ctx.rebind(key, obj);
            rtnBool = true;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return rtnBool;
    }

    /**
     * doUnBind
     * @param name name
     * @return true/false
     */
    public boolean unBind(String name) {
        boolean rtnBol = false;
        try {
            ctx.unbind(name);
            rtnBol = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnBol;
    }
    /**
     * ɾ��ָ��DN
     * @param dn ָ��DN
     * @throws NamingException NamingException
     */
    public void destroyCtx(String dn) throws NamingException {
        //��dn�µ���dn
        NamingEnumeration<NameClassPair> resultList = list(dn);
        if (resultList == null) {
            return;
        }
        NameClassPair ncpInst = null;
        String tmpName = null;
        while (resultList.hasMore()) {
            ncpInst = resultList.next();
            tmpName = ncpInst.getNameInNamespace();
            destroyCtx(tmpName);
            this.ctx.destroySubcontext(tmpName);
        }
        this.ctx.destroySubcontext(dn);
    }

    /**
     * ɾ��DN���ݹ��ѯɾ��
     * <p><strong>ע�⣺</strong>֧��ɾ��Ҷ�ӽڵ�</p>
     * @param dn ָ��DN
     * @throws NamingException NamingException
     */
    public void deleteDN(String dn) throws NamingException {
        NamingEnumeration<SearchResult> nameEnum = ctx.search(dn, "(objectclass=*)", new SearchControls());
        SearchResult searchResult = null;
        while (nameEnum.hasMore()) {
            searchResult = nameEnum.next();
            deleteDN(searchResult.getNameInNamespace());
        }
        System.out.println(dn);
        ctx.destroySubcontext(dn);
    }

    /**
     * ȡ�÷���ʵ��
     * @return instance
     */
    public static ContextMgr4LDAP getInstance() {
        return InnerClass.cm4LDAP;
    }
    /**
     * ȡ��ǰĿ¼������ʵ��
     * @return ctx {@link DirContext}
     */
    public DirContext getCtx() {
        return this.ctx;
    }
}

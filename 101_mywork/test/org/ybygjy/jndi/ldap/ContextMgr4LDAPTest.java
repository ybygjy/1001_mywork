package org.ybygjy.jndi.ldap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ybygjy.jndi.ldap.model.LDAPTestUserEntity;

public class ContextMgr4LDAPTest implements Serializable {
    /**
     * serialized
     */
    private static final long serialVersionUID = 6206511843081011637L;
    /** ����Ŀ¼*/
    private String tmpSubCtxDN = "ou=Java,".concat(Constant.BASEDN);
    /** ȫ��ʵ������ */
    private transient ContextMgr4LDAP cmLDAPInst;
    /** ���β�������ʵ������*/
    private transient LDAPTestUserEntity tmpInst = new LDAPTestUserEntity("���ӳ�", "2011-10-20");

    /**
     * ���Ի�����ʼ��
     * @throws Exception �����쳣��Ϣ
     */
    @Before
    public void setUp() throws Exception {
        cmLDAPInst = ContextMgr4LDAP.getInstance();
        initTestCtx();
    }
    /**
     * �����ʼ�����Ի���
     */
    public void initTestCtx() {
        testBind();
        testDestroySubCtx();
    }
    /**
     * ������ɺ����������Դ
     * @throws Exception �����쳣��Ϣ
     */
    @After
    public void tearDown() throws Exception {
        testUnBind();
    }
    @Test
    public void testModifyPassword() {
        cmLDAPInst.modifyPassword(AttributeMgr4LDAPTest.testDN, "1234567", 0);
    }
    @Test
    public void testCreateSubCtx() {
        DirContext ctx = cmLDAPInst.getCtx();
        try {
            Attributes attrs = new BasicAttributes(true);
            Attribute attr = new BasicAttribute("objectclass");
            attr.add("top");
            attr.add("organizationalUnit");
            attrs.put(attr);
            ctx = ctx.createSubcontext(tmpSubCtxDN, attrs);
            attrs = new BasicAttributes(true);
            attr = new BasicAttribute("objectclass");
            attr.add("top");
            attr.add("person");
            attr.add("organizationalPerson");
            attr.add("inetOrgPerson");
            attrs.put(attr);
            attrs.put(new BasicAttribute("cn", "WangYanCheng"));
            attrs.put(new BasicAttribute("sn", "YanCheng"));
            ctx.bind("cn=WangYanCheng", new String("HelloWorld"), attrs);
            NamingEnumeration nameEnum = ctx.list("");
            LDAPUtils.printEnum(nameEnum);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDestroySubCtx() {
        try {
            if (null != cmLDAPInst.lookup(tmpSubCtxDN)) {
                cmLDAPInst.destroyCtx(tmpSubCtxDN);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    /**
     * ���Ի�ȡĳ���������
     */
    @Test
    public void testGetAttributes() {
        String tmpDN = "cn=WangYanCheng,ou=People,".concat(Constant.BASEDN);
        //UserEntity.getDN()
        NamingEnumeration<Attribute> nameArray = (NamingEnumeration<Attribute>) (cmLDAPInst.getAttributes(tmpDN)).getAll();
        System.out.println("����".concat(tmpDN).concat("�����ԣ�"));
        LDAPUtils.printAttr(nameArray);
    }
    /**
     * ��ѯĳĿ¼�µ��ӽڵ�
     * @throws NamingException NamingException
     */
    @Test
    public void testList() throws NamingException {
        NamingEnumeration<NameClassPair> nameEnum = cmLDAPInst.list(Constant.BASEDN);
        Assert.assertNotNull(nameEnum);
        System.out.println("���ڵ�[" + Constant.BASEDN + "]" + "Ŀ¼�µĶ���");
        LDAPUtils.printEnum(nameEnum);
    }

    /**
     * ��ǰ�����Ļ����µĶ���
     */
    @Test
    public void testListBinding() {
        NamingEnumeration<Binding> nameEnum = cmLDAPInst.listBinding(Constant.BASEDN);
        Assert.assertNotNull(nameEnum);
        System.out.println("���ڵ�[" + Constant.BASEDN + "]" + "Ŀ¼�µĶ���");
        LDAPUtils.printEnum(nameEnum);
    }

    /**
     * ������Ŀ¼�ṹ�ϰ󶨶���ʵ��
     */
    @Test
    public void testBind() {
        Assert.assertTrue(cmLDAPInst.bind(LDAPTestUserEntity.getDN(), tmpInst));
    }

    /**
     * ������Ŀ¼�����ϲ��Ҷ���
     */
    @Test
    public void testLookup() {
        Assert.assertEquals(cmLDAPInst.lookup(LDAPTestUserEntity.getDN()), tmpInst);
    }

    @Test
    public void testSearch() {
        String tmpDN = "ou=People,".concat(Constant.BASEDN);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("telephoneNumber", "15810779839");
        paramMap.put("sn", "Wang");
        NamingEnumeration<SearchResult> resultEnum = cmLDAPInst.search(tmpDN, paramMap);
        LDAPUtils.printEnum(resultEnum);
    }

    /**
     * ���ڹ��˱��ʽ��ѯ
     */
    @Test
    public void testSearch4Filter() {
        String tmpDN = "ou=People,".concat(Constant.BASEDN);
        String filterExp = "(&(sn=Wang)(telephoneNumber=15810779839))";
        NamingEnumeration<SearchResult> resultEnum = cmLDAPInst.search(tmpDN, filterExp);
        LDAPUtils.printEnum(resultEnum);
        System.out.println("���й���Attribute��search");
        String[] filterAttr = {"cn", "sn", "userPassword", "jpegPhoto"};
        resultEnum = cmLDAPInst.search(tmpDN, filterExp, filterAttr, SearchControls.SUBTREE_SCOPE);
        LDAPUtils.printResultEnum(resultEnum, tmpDN, filterExp);
    }
    @Test
    public void testSearch4Scope() {
        String filterExp = "(&(uid=WangYanCheng))";
        NamingEnumeration<SearchResult> resultEnum = cmLDAPInst.search(Constant.BASEDN, filterExp, null, SearchControls.SUBTREE_SCOPE);
        Assert.assertNotNull(resultEnum);
        LDAPUtils.printEnum(resultEnum);
    }
    @Test
    public void testSearch4ObjScope() {
        String tmpDN = "cn=WangYanCheng,ou=People,".concat(Constant.BASEDN);
        String[] filterAttr = {"cn", "sn", "uid", "jpegPhoto"};
        String filterExp = "(&(cn=*))";
        NamingEnumeration<SearchResult> resultEnum = cmLDAPInst.search(tmpDN, filterExp, filterAttr, SearchControls.OBJECT_SCOPE);
        Assert.assertNotNull(resultEnum);
        LDAPUtils.printResultEnum(resultEnum, tmpDN, filterExp);
    }

    @Test
    public void testSearchUsedControl() {
        String tmpDN = "ou=People,".concat(Constant.BASEDN);
        String filterExp = "(&(telephoneNumber=*))";
        SearchControls searchCtrl = new SearchControls();
        searchCtrl.setCountLimit(10);
        searchCtrl.setTimeLimit(500);
        
        NamingEnumeration<SearchResult> resultEnum = cmLDAPInst.search(tmpDN, filterExp, searchCtrl);
        Assert.assertNotNull(resultEnum);
        LDAPUtils.printResultEnum(resultEnum, tmpDN, filterExp);
    }

    /**
     * ���Խڵ����
     */
    @Test
    public void testRenaming() {
        String oldName = LDAPTestUserEntity.getDN();
        String newName = "cn=newUserEntry,".concat(Constant.BASEDN);
        Assert.assertTrue(cmLDAPInst.doRenaming(oldName, newName));
        Assert.assertTrue(cmLDAPInst.unBind(newName));
    }

    /**
     * �����Ƴ��ڵ�
     */
    @Test
    public void testUnBind() {
        Assert.assertTrue(cmLDAPInst.unBind(LDAPTestUserEntity.getDN()));
        try {
            cmLDAPInst.destroyCtx(tmpSubCtxDN);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testShowSupportedControls() {
        LDAPUtils.showSupportControls(cmLDAPInst.getCtx());
    }

}

package org.ybygjy.jndi.ldap;

import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JNDI�߼�����֮LDAP֧��
 * @author WangYanCheng
 * @version 2011-5-2
 */
public class BeyondBasic4LDAPTest {
    /**�������ʵ��*/
    private DirContext dirCtx;
    @Before
    public void before() {
        dirCtx = Constant.createCtx();
    }
    /**
     * Composite�����㺬��
     * <p>1��(Эͬ�������)JNDI֧��Ŀ¼��Ϲ�������ʹ��openLDAP����λ��ʹ��fileService����������ļ�</p>
     * <p>2��(��ϴ����﷨)���ƣ�cn=homeDir,dc=ybygjy,dc=org/HelloWorld.txt</p>
     */
    @Test
    public void testCompositeName() {
        try {
            Name cname = new CompositeName("cn=homedir,cn=WangYanCheng,ou=People,".concat(Constant.BASEDN).concat("/HelloWorld.txt"));
            Object obj = dirCtx.lookup(cname);
            System.out.println(obj.getClass());
            //cn=homedir,cn=Jon Ruiz,ou=People,o=JNDITutorial,dc=ybygjy,dc=org
            //obj = ((com.sun.jndi.fscontext.RefFSContext)obj).lookup("HelloWorld.txt");
            System.out.println(obj);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        } catch (NamingException ne) {
            ne.printStackTrace();
        }
    }
    @Test
    public void testConpositeNameStruct() {
        String name = "cn=userMgr,ou=hr,dc=dawoo,dc=com/userInfo.xls/password";
        try {
            CompositeName compositeName = new CompositeName(name);
            System.out.println(name + "����" + (compositeName.size()) + "��");
            System.out.println(compositeName.getPrefix(1));
            System.out.println(compositeName.getSuffix(2));
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testNameParser() throws NamingException {
        NameParser nameParser = dirCtx.getNameParser("");
        Name compoundName = nameParser.parse("cn=test,ou=People,o=JNDITutorial,".concat(Constant.BASEDN));
        for (int i = 0; i < compoundName.size(); i++) {
            System.out.println(compoundName.get(i));
        }
    }
    @After
    public void tearDown() {
        if (dirCtx != null) {
            try {
                dirCtx.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
            dirCtx = null;
        }
    }
}

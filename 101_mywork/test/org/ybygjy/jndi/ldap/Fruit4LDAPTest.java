package org.ybygjy.jndi.ldap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ybygjy.jndi.ldap.model.Fruit;

/**
 * ��ʵ��Reference�ӿڵ���ʵ���İ�
 * @author WangYanCheng
 * @version 2011-4-29
 */
public class Fruit4LDAPTest {
    /**����ʵ��*/
    private ContextMgr4LDAP cmLDAPInst;
    /**������Զ���*/
    private Fruit fruit;
    @Before
    public void setUP() {
        cmLDAPInst = ContextMgr4LDAP.getInstance();
        fruit = new Fruit("ƻ��");
    }
    @Test
    public void testBind() {
        cmLDAPInst.bind(Fruit.dn, fruit);
    }
    @Test
    public void testLookup() {
        Fruit fruit = (Fruit) cmLDAPInst.lookup(Fruit.dn);
        if (!this.fruit.equals(fruit)) {
            throw new RuntimeException("�󶨶���һ�¡���");
        }
    }
    @Test
    public void testUnbind() {
        cmLDAPInst.unBind(Fruit.dn);
    }
    @After
    public void tearDown() {
    }
}

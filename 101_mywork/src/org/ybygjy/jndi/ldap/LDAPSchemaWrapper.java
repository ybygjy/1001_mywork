package org.ybygjy.jndi.ldap;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

/**
 * LDAP Schema����
 * <p>
 *  <ol>
 *      <li>��ʹ��Directory����Ա��ɫ</li>
 *      <li><strong>OpenLDAP: </strong>OpenLDAP does not support the modification of the RFC 2252 schema descriptions. Instead, you must add any new schema or updated schema descriptions statically to the server's schema files. See the OpenLDAP documentation for details. </li>
 *      <li><strong>Windows Active Directory: </strong>Active Directory does not support the modification of the RFC 2252 schema descriptions. Instead, you must update Active Directory's internal schema representation. See the Windows 2000 Planning Guide for details on how to enable and perform schema updates. </li>
 *  </ol>
 * </p>
 * @author WangYanCheng
 * @version 2011-5-13
 */
public class LDAPSchemaWrapper {
    /**Ŀ¼����*/
    private DirContext dirCtx;
    /**
     * ���췽��
     * @param dirCtx dirCtx
     */
    public LDAPSchemaWrapper(DirContext dirCtx) {
        this.dirCtx = dirCtx;
    }
    /**
     * ȡ��ָ��DN������Schema
     * @param dn ����DN
     */
    public void getSchema(String dn) {
        DirContext tmpCtx = null;
        try {
            tmpCtx = this.dirCtx.getSchema(dn);
            NamingEnumeration<NameClassPair> nameResult = tmpCtx.list("");
            NameClassPair ncpTmp = null;
            while (nameResult.hasMore()) {
                ncpTmp = nameResult.next();
                System.out.println(ncpTmp.getName());
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    /**
     * ȡ����DN�µģ�schema������ϸ��Ϣ��������objectClassֵΪtop������top������
     * <p>����ʹ�õ��Ǳ���Attributes��ʽ���˷���Ҫ��LDAPSchemaWrapper#getSchemaClassDef4Lookup(String)Ч�ʸߡ�</p>
     * @param dn ����DN
     * @see LDAPSchemaWrapper#getSchemaClassDef4Lookup(String)
     */
    public void getSchemaClassDef(String dn) {
        DirContext tmpDir = null;
        try {
            tmpDir = dirCtx.getSchemaClassDefinition(dn);
            NamingEnumeration<NameClassPair> nameResult = tmpDir.list("");
            String tmpName = null;
            while (nameResult.hasMore()) {
                tmpName = nameResult.next().getName();
                Attributes attrs = tmpDir.getAttributes(tmpName);
                NamingEnumeration<Attribute> attrEnum = (NamingEnumeration<Attribute>) attrs.getAll();
                StringBuilder sbud = new StringBuilder();
                Attribute attr = null;
                while (attrEnum.hasMore()) {
                    attr = attrEnum.next();
                    sbud.append(attr.getID()).append("\t");
                    NamingEnumeration valueEnum = attr.getAll();
                    while (valueEnum.hasMore()) {
                        sbud.append(valueEnum.next()).append(",");
                    }
                    sbud.append("\n");
                }
                System.out.println(tmpName + "\n".concat(sbud.toString()));
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != tmpDir) {
                try {
                    tmpDir.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ȡ����DN��SchemaClass������ϸ����
     * <p><strong>ע�⣺</strong>�˷����������ǲ�ѯ����ӡ������DN(��Ŀ)����ʹ�õ���objectclass��ʾ����
     * <pre>
     * dn:cn=WangYanCheng,ou=HR,dc=ybygjy,dc=com
     * objectclass:organizationalPerson
     * objectclass:inetOrgPerson
     * objectclass:person
     * objectclass:top
     * </pre>
     * ��������Ŀ�����ԣ�������������
     * <table border="1">
     *  <tr>
     *      <th>����</th>
     *      <th>ֵ</th>
     *  </tr>
     *  <tr>
     *      <td>NAME</td>
     *      <td>inetOrgPerson</td>
     *  </tr>
     *  <tr>
     *      <td>SUP</td>
     *      <td>organizationalPerson</td>
     *  </tr>
     *  <tr>
     *      <td>NAME</td>
     *      <td>organizationalPerson</td>
     *  </tr>
     *  <tr>
     *      <td>SUP</td>
     *      <td>person</td>
     *  </tr>
     * </table>
     * <tr>���������˱����ݺܶ࣬�Ͳ�һһ�г���</tr>
     * </p>
     * @param dn ָ��DN
     * @see LDAPSchemaWrapper#getSchemaClassDef(String)
     */
    public void getSchemaClassDef4Lookup(String dn) {
        DirContext tmpCtx = null;
        try {
            tmpCtx = dirCtx.getSchemaClassDefinition(dn);
            NamingEnumeration<NameClassPair> nameEnum = tmpCtx.list("");
            NameClassPair ncpInst = null;
            while (nameEnum.hasMore()) {
                ncpInst = nameEnum.next();
                DirContext tmpCtxx = (DirContext) tmpCtx.lookup(ncpInst.getName());
                Attributes attrs = tmpCtxx.getAttributes("");
                NamingEnumeration<Attribute> attrEnum = (NamingEnumeration<Attribute>) attrs.getAll();
                Attribute attr = null;
                StringBuilder sbud = new StringBuilder();
                while (attrEnum.hasMore()) {
                    attr = attrEnum.next();
                    sbud.append(attr.getID()).append("\t");
                    NamingEnumeration valueEnum = attr.getAll();
                    while (valueEnum.hasMore()) {
                        sbud.append(valueEnum.next()).append(",");
                    }
                    sbud.append("\n");
                }
                System.out.println(sbud.toString());
                tmpCtxx.close();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (tmpCtx != null) {
                try {
                    tmpCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ȡ����DN�µ�����ObjectClass������
     * @param dn ָ��DN
     */
    public void getSchemaClassDef4Search(String dn) {
        DirContext tmpCtx = null;
        try {
            tmpCtx = dirCtx.getSchemaClassDefinition(dn);
            NamingEnumeration<SearchResult> resultEnum = tmpCtx.search("", null);
            SearchResult tmpResult = null;
            Attributes attrs = null;
            StringBuilder sbud = new StringBuilder();
            while (resultEnum.hasMore()) {
                tmpResult = resultEnum.next();
                attrs = tmpResult.getAttributes();
                NamingEnumeration<Attribute> attrEnum = (NamingEnumeration<Attribute>) attrs.getAll();
                Attribute attr = null;
                while (attrEnum.hasMore()) {
                    attr = attrEnum.next();
                    sbud.append(attr.getID()).append("\t");
                    NamingEnumeration valueEnum = attr.getAll();
                    while (valueEnum.hasMore()) {
                        sbud.append(valueEnum.next()).append(",");
                    }
                    sbud.append("\n");
                }
                sbud.append("\n");
            }
            System.out.println(dn + "\n" + sbud.toString());
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != tmpCtx) {
                try {
                    tmpCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ���Ҹ���DN�µ�ָ��objectClass�ṹ����
     * @param dn ����DN
     * @param objectClass ����objectClass
     */
    public void lookUp4ClassDef(String dn, String objectClass) {
        String regStr = "ClassDefinition/@OBJ@";
        DirContext rtnCtx = null, schemaCtx = null;
        try {
            schemaCtx = dirCtx.getSchema(dn);
            rtnCtx = (DirContext) schemaCtx.lookup(regStr.replaceAll("@OBJ@", objectClass));
            Attributes attrs = rtnCtx.getAttributes("");
            NamingEnumeration<Attribute> attrEnum = (NamingEnumeration<Attribute>) attrs.getAll();
            Attribute attr = null;
            while (attrEnum.hasMore()) {
                attr = attrEnum.next();
                NamingEnumeration<String> nameEnum = (NamingEnumeration<String>) attr.getAll();
                StringBuilder sbud = new StringBuilder();
                while (nameEnum.hasMore()) {
                    sbud.append(nameEnum.next()).append(",");
                }
                System.out.println(attr.getID() + "\t" + sbud.toString());
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null == schemaCtx) {
                try {
                    schemaCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
            if (null == rtnCtx) {
                try {
                    rtnCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ��ָ��DN���objectclass
     * <p>
     *  <strong>ע�⣺</strong>
     *  1������Ŀ¼��������Ӧ�汾����ȷ�϶�Ŀ¼�淶<a href="http://www.ietf.org/rfc/rfc2252.txt">RFC 2252</a>��֧��
     * </p>
     * @param dn ָ��DN
     * @param className objectClass�ṹ����
     * @param attrs objectClass�ṹ����
     * @return rtnFlag {true:�ɹ����;false:δ�ɹ�}
     */
    public boolean addNewObjectClass(String dn, String className, Attributes attrs) {
        DirContext tmpCtx = null;
        boolean rtnFlag = false;
        try {
            tmpCtx = dirCtx.getSchema(dn);
            DirContext schemaRef = tmpCtx.createSubcontext("ClassDefinition/".concat(className), attrs);
            if (null != schemaRef) {
                rtnFlag = true;
                schemaRef.close();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != tmpCtx) {
                try {
                    tmpCtx.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnFlag;
    }
    /**
     * ɾ������DN�µ���ӦobjectClass
     * @param dn ����DN
     * @param className Ҫɾ����objectClass����
     * @return true/false
     * @see LDAPSchemaWrapper#addNewObjectClass(String, String, Attributes)
     */
    public boolean removeObjectClass(String dn, String className) {
        boolean rtnFlag = false;
        DirContext dirCtx = null;
        try {
            dirCtx = this.dirCtx.getSchema(dn);
            dirCtx.destroySubcontext("ClassDefinition/".concat(className));
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != dirCtx) {
                try {
                    dirCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnFlag;
    }
    /**
     * �޸ĸ���DN�µ�objectClass
     * 
     * @param dn ����DN
     * @param objectClassName Ҫ�޸ĵ�objectclass����
     * @param opFlag �������(ADD��REMOVE��REPLACE��etc..)
     * @param attrs �ṹ��Ϣ
     * @return true/false
     */
    public boolean modifyObjectClass(String dn, String objectClassName, int opFlag, Attributes attrs) {
        boolean rtnFlag = false;
        DirContext tmpCtx = null;
        try {
            tmpCtx = dirCtx.getSchema(dn);
            tmpCtx.modifyAttributes("ClassDefinition/".concat(objectClassName), opFlag, attrs);
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != tmpCtx) {
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
     * ��ѯ����DN�µ�Attribute�ṹ����
     * @param dn ָ��DN
     * @param attrName Ҫ��ѯ��Attribute�ṹ����
     */
    public void getSchemaAttributeDef4Lookup(String dn, String attrName) {
        DirContext tmpCtx = null;
        try {
            tmpCtx = dirCtx.getSchema(dn);
            DirContext tmpCtxx = (DirContext) tmpCtx.lookup("AttributeDefinition/".concat(attrName));
            Attributes attrs = tmpCtxx.getAttributes("");
            NamingEnumeration<Attribute> nameEnum = (NamingEnumeration<Attribute>) attrs.getAll();
            Attribute attr = null;
            StringBuilder sbud = new StringBuilder();
            while (nameEnum.hasMore()) {
                attr = nameEnum.next();
                sbud.append(attr.getID()).append("\t");
                NamingEnumeration valueEnum = attr.getAll();
                while (valueEnum.hasMore()) {
                    sbud.append(valueEnum.next()).append(",");
                }
                sbud.append("\n");
            }
            System.out.println(sbud.toString());
            tmpCtxx.close();
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != tmpCtx) {
                try {
                    tmpCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ȡAttribute Schema
     * <p><strong>�������̣�</strong>
     * ��λDN��Ŀ==>ȡ��Ŀ���Լ�==>�������Լ�/ȡ����==>ȡ���Խṹ��Ϣ==>ȡ�ṹ��Ϣ��(�ṹ�����Լ�)==>�������Լ�/ȡ����==>����ȡ����ֵ
     * </p>
     * @param dn ����DN
     */
    public void getSchemaAttributeDef(String dn) {
        DirContext tmpDir = null;
        try {
            tmpDir = (DirContext) dirCtx.lookup(dn);
            Attributes attrs = tmpDir.getAttributes("");
            NamingEnumeration<Attribute> attrEnum = (NamingEnumeration<Attribute>) attrs.getAll();
            Attribute attr = null;
            StringBuilder sbud = new StringBuilder();
            while (attrEnum.hasMore()) {
                attr = attrEnum.next();
                sbud.append(attr.getID()).append("\n");
                DirContext tmpDirr = attr.getAttributeDefinition();
//                attrs = tmpDirr.getAttributes("", new String[]{"NAME", "NUMERICOID"});
                attrs = tmpDirr.getAttributes("");
                NamingEnumeration<Attribute> attrEnumm = (NamingEnumeration<Attribute>) attrs.getAll();
                while (attrEnumm.hasMore()) {
                    attr = attrEnumm.next();
                    //����Ϊ�����˼��ȷ�������˻�ȡֵ���ڲ�ѭ����ɿ���ʹ�ã���ʵ����sbud.append(attr).append("\n");
                    NamingEnumeration<Attribute> attrValueEnum = (NamingEnumeration<Attribute>) attr.getAll();
                    sbud.append(attr.getID()).append("\t");
                    while (attrValueEnum.hasMore()) {
                        sbud.append(attrValueEnum.next()).append(",");
                    }
                    sbud.append("\n");
                }
                sbud.append("\n");
                tmpDirr.close();
            }
            System.out.println(sbud.toString());
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (tmpDir != null) {
                try {
                    tmpDir.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ɾ������DN�µ����Խṹ
     * <p><strong>ע�⣺</strong>
     * 1����ȷ��LDAP������֧�ֶ�Schema�ṹ��ɾ����OpenLDAP 2.4 �ǿ���֧�ֵĵ�2.3������
     * 2����ο�LDAP����������˵���ĵ�
     * </p>
     * @param dn ָ��DN
     * @param attrName ���Խṹ����
     */
    public void removeAttributeSchema(String dn, String attrName) {
        DirContext tmpCtx = null;
        try {
            tmpCtx = dirCtx.getSchema(dn);
            tmpCtx.destroySubcontext("AttributeDefinition/".concat(attrName));
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != tmpCtx) {
                try {
                    tmpCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ��ѯsyntaxDefinition����
     * @return true/false
     */
    public boolean getSchemaSyntaxDef(String syntaxId) {
        DirContext tmpDir = null;
        boolean rtnFlag = false;
        try {
            tmpDir = dirCtx.getSchema("");
            DirContext tmpDirr = (DirContext) tmpDir.lookup("SyntaxDefinition/".concat(syntaxId));
            Attributes attrs = tmpDirr.getAttributes("");
            NamingEnumeration<Attribute> attrEnum = (NamingEnumeration<Attribute>) attrs.getAll();
            Attribute attr = null;
            StringBuilder sbud = new StringBuilder();
            while (attrEnum.hasMore()) {
                attr = attrEnum.next();
                sbud.append(attr.getID()).append("\t");
                NamingEnumeration valueEnum = attr.getAll();
                while (valueEnum.hasMore()) {
                    sbud.append(valueEnum.next()).append(",");
                }
                sbud.append("\n");
            }
            System.out.println(sbud.toString());
            tmpDirr.close();
            rtnFlag = true;
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            if (null != tmpDir) {
                try {
                    tmpDir.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnFlag;
    }
    /**
     * ȡ�ø���DN�µ����������õ����﷨
     * @param dn
     * @return
     */
    public boolean getSchemaSyntaxDefByDN(String dn) {
        return false;
    }
    /**
     * ����������objectClass
     * @return rtnAttrs objectClass�ṹ����
     */
    public Attributes createTestObjectClass() {
        Attributes rtnAttrs = new BasicAttributes(true);
        rtnAttrs.put("NUMERICOID", "1.3.6.1.4.1.4754.2.99.1");
        rtnAttrs.put("NAME", "daowooUser");
        rtnAttrs.put("DESC", "helloWorld");
        rtnAttrs.put("SUP", "top");
        rtnAttrs.put("STRUCTURAL", "true");
        Attribute mustAttr = new BasicAttribute("MUST", "cn");
        mustAttr.add("objectclass");
        rtnAttrs.put(mustAttr);
        Attribute mayAttr = new BasicAttribute("MAY", "uid");
        rtnAttrs.put(mayAttr);
        return rtnAttrs;
    }
    /**
     * ���������޸�objectClass
     * @return rtnAttrs objectClass�ṹ����
     */
    public Attributes createModifyObjectClass() {
        Attributes rtnAttrs = new BasicAttributes(false);
        Attribute attr = new BasicAttribute("MAY", "description");
        rtnAttrs.put(attr);
        return rtnAttrs;
    }
}

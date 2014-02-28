package org.ybygjy.jndi.ldap;

import java.io.UnsupportedEncodingException;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

import org.ybygjy.basic.security.SecurityUtils;

/**
 * ��װ��������
 * @author WangYanCheng
 * @version 2011-6-9
 */
public class LDAPUtils {
    /**
     * ȡ����ֵ,����Զ��ŷָ��userPhone=phone1,phone2,phone3
     * @param attr ��Ŀ���Զ���
     * @return ��Ŀֵ
     */
    public static String getAttributeValue(Attribute attr) {
        StringBuffer sbud = new StringBuffer();
        try {
            NamingEnumeration<?> nameEnum = attr.getAll();
            Object obj = null;
            while (nameEnum.hasMore()) {
                obj = nameEnum.next();
                // ע�� obj���Ϳ������ֽڵ���������
                if (obj instanceof String) {
                    sbud.append(obj.toString()).append(",");
                } else if (obj instanceof byte[]) {
                    sbud.append(new String((byte[]) obj)).append(",");
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        if (sbud.length() > 0) {
            sbud.setLength(sbud.length() - 1);
        }
        return sbud.length() == 0 ? null : sbud.toString();
    }

    /**
     * ����Ŀ����ȡ��Ŀֵ����ֵ�Զ��ŷָ�
     * @param attrs ������Ŀ��
     * @param attrID ��ĿID
     * @return ��Ŀֵ
     */
    public static String getAttributeValue(Attributes attrs, String attrID) {
        Attribute attr = attrs.get(attrID);
        return attr != null ? (getAttributeValue(attr)) : null;
    }

    /**
     * ����������OpenLDAP����Ŀ����
     * @param srcPassStr ����
     * @return cipherStr ���봮
     */
    public static String getOpenLDAPEntryPassword(String srcPassStr) {
        String rtnStr = SecurityUtils.encodeBase64(SecurityUtils.encodeMD5(srcPassStr));
        return "{MD5}".concat(rtnStr);
    }

    /**
     * ���߷����������ӡ����ֵ��
     * @param resultEnum �����
     * @param tmpDN ָ��DN
     * @param filterExp ���˱��ʽ
     */
    public static void printResultEnum(NamingEnumeration<SearchResult> resultEnum, String tmpDN,
                                       String filterExp) {
        try {
            if (resultEnum.hasMore()) {
                printAttr((NamingEnumeration<Attribute>) ((resultEnum.next().getAttributes()).getAll()));
                while (resultEnum.hasMore()) {
                    printAttr((NamingEnumeration<Attribute>) ((resultEnum.next().getAttributes()).getAll()));
                }
            } else {
                System.out.println("δ���ҵ�ƥ�䣺\n\tDN��".concat(tmpDN).concat("\n\tfilterExp��")
                    .concat(filterExp));
            }
        } catch (SizeLimitExceededException see) {
            System.err.println("����ָ��limit��Χ".concat(see.getMessage()));
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���߷���,�����ӡ�����е�Ԫ��
     * @param resultEnum nameEnum
     */
    public static void printEnum(NamingEnumeration<?> resultEnum) {
        while (resultEnum.hasMoreElements()) {
            NameClassPair ncpInst = (NameClassPair) resultEnum.nextElement();
            System.out.println("\t" + ncpInst.getNameInNamespace());
        }
    }

    /**
     * ���߷���,�����ӡ���Լ���Ԫ��
     * <p>
     * <strong>ע�⣺</strong>
     * </p>
     * @param nameArray nameEnum
     */
    public static void printAttr(NamingEnumeration<Attribute> nameArray) {
        Attribute attr = null;
        try {
            while (nameArray.hasMore()) {
                attr = nameArray.next();
                StringBuilder sbud = new StringBuilder();
                sbud.append(attr.getID()).append("=");
                for (NamingEnumeration<?> ne = attr.getAll(); ne.hasMore();) {
                    Object obj = ne.next();
                    sbud.append(obj.toString()).append("[").append(obj.getClass().getSimpleName()).append(
                        "]");
                }
                System.out.println(sbud.toString());
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����������OpenLDAPServer�洢��MD5�����ַ���
     * @param srcPasswdStr ����
     * @return rtnStr ������ɵ����봮
     */
    public static String getOpenLDAPMD5(String srcPasswdStr) {
        byte[] md5Bytes = SecurityUtils.encodeMD5(srcPasswdStr);
        String rtnStr = SecurityUtils.encodeBase64(md5Bytes);
        rtnStr = "{MD5}".concat(rtnStr);
        return rtnStr;
    }

    /**
     * ������Դ�ַ���ת��Ϊ������OpenLDAPMD5��ʽ�洢���ַ���
     * <p>
     * <strong>�˷���Ӧ����LDIF�ļ����ݣ���������MD5���ܵ��ַ������Դ洢����Ŀ�������(��userPassword)�С�</strong>
     * </p>
     * <p>
     * OpenLDAPMD5��ʽ����ԭ��
     * <ol>
     * <li>OpenLDAP��MD5���ܷ�ʽ�洢�����봮�Ǿ���Base64����֮����ַ����������ǵ�����MD5���ܺ��16���Ƹ�ʽ���ַ�����</li>
     * <li>ת����ʽ��Ҫ��������
     * <p>
     * ��һ����Դ�ַ�������MD5ת��
     * </p>
     * <p>
     * �ڶ���Base64�Խ���MD5ת���õ����ֽ�����б���
     * </p>
     * <p>
     * ������ͨ��JNDI(������LDAP Client)��������ɵ��ַ����洢���ض�Entry��Attribute��
     * </p>
     * </li>
     * </ol>
     * </p>
     * @param srcStr Դ�ַ���(���봮)
     * @return rtnStr ת����ɵ��ַ���
     */
    public static String getOpenLDAPMD5LDIF(String srcStr) {
        String rtnStr = getOpenLDAPMD5(srcStr);
        try {
            rtnStr = SecurityUtils.encodeBase64(rtnStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtnStr;
    }
    /**
     * Finding out of the list of controls that an LDAP server supports.
     * @param ctx DirContext
     */
    public static void showSupportControls(DirContext ctx) {
        Attributes attrs = null;
        try {
            attrs = ctx.getAttributes("", new String[]{"supportedcontrol"});
        } catch (NamingException e) {
            e.printStackTrace();
        }
        if (attrs != null) {
            printAttr((NamingEnumeration<Attribute>) attrs.getAll());
        }
    }
}

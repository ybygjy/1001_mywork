package org.ybygjy.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.Entry;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

/**
 * KeyStore����
 * <p>1��KeyStore������Ĺ���</p>
 * <p>2����Ŀ�����ӡ�ɾ��</p>
 * @author WangYanCheng
 * @version 2013-10-7
 */
public class KeyStoreManager {
    private KeyStore keyStore;
    private File keyStoreFile;
    private String keyStorePass;

    /**
     * ���췽��
     * @param keyStoreFile KeyStore�ļ�·��
     * @param keyStorePass KeyStore�ļ�����
     */
    public KeyStoreManager(File keyStoreFile, String keyStorePass) {
        this.keyStoreFile = keyStoreFile;
        this.keyStorePass = keyStorePass;
        this.keyStore = createKeyStore(keyStoreFile, keyStorePass);
    }

    /**
     * ����KeyStore�ļ�����
     * @param keyStoreFile �ļ�ʵ��
     * @param keyStorePass ������Ϣ
     * @return rtnKeyStore {@link KeyStore}
     */
    private KeyStore createKeyStore(File keyStoreFile, String keyStorePass) {
        KeyStore keyStore = null;
        FileInputStream fisInst = null;
        try {
            fisInst = new FileInputStream(keyStoreFile);
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(fisInst, keyStorePass.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fisInst) {
                try {
                    fisInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return keyStore;
    }

    /**
     * �������֤��
     * @param trustFile ֤���ļ�
     */
    public void addTrustItem(File trustFile) {
        Certificate certificate = createCertificate(trustFile);
        KeyStore.TrustedCertificateEntry tceInst = new KeyStore.TrustedCertificateEntry(certificate);
        try {
            this.keyStore.setEntry("com.alipay." + (certificate.hashCode()), tceInst, null);
            this.restoreKeyStore(this.keyStore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ��������������Ŀ
     * @param alias
     */
    public void deleteTrustItem(String alias) {
        try {
            this.keyStore.deleteEntry(alias);
            restoreKeyStore(this.keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����֤��ʵ��
     * @return certInst {@link Certificate}
     */
    private Certificate createCertificate(File trustFile) {
        Certificate rtnCertificate = null;
        FileInputStream fisInst = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            fisInst = new FileInputStream(trustFile);
            rtnCertificate = certificateFactory.generateCertificate(fisInst);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fisInst) {
                try {
                    fisInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnCertificate;
    }

    /**
     * ��ӡ��ǰJVM��ѡ��ȫʵ���б�
     */
    public void showSecurityProvider() {
        Provider[] securityProviderArray = Security.getProviders();
        for (Provider provider : securityProviderArray) {
            System.out.println(provider.toString());
        }
    }

    /**
     * ��ӡ��ǰKeyStore������Ϣ
     */
    private void showItemList() {
        Enumeration<String> aliasEnum = null;
        try {
            aliasEnum = keyStore.aliases();
            while (aliasEnum.hasMoreElements()) {
                String aliasName = aliasEnum.nextElement();
                System.out.println("��ĿBegin==>" + aliasName);
                Entry entry = keyStore.getEntry(aliasName, null);
                if (entry instanceof KeyStore.SecretKeyEntry) {
                    KeyStore.SecretKeyEntry kskeInst = (KeyStore.SecretKeyEntry) entry;
                    System.out.println(kskeInst.toString());
                } else if (entry instanceof KeyStore.TrustedCertificateEntry) {
                    KeyStore.TrustedCertificateEntry kskeInst = (KeyStore.TrustedCertificateEntry) entry;
                    System.out.println(kskeInst.toString());
                } else if (entry instanceof KeyStore.PrivateKeyEntry) {
                    KeyStore.PrivateKeyEntry kskeInst = (KeyStore.PrivateKeyEntry) entry;
                    System.out.println(kskeInst.toString());
                }
                System.out.println("��ĿEnd==>" + aliasName);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }
    }

    /**
     * ת��KeyStore
     * @param keyStore {@link KeyStore}
     */
    private void restoreKeyStore(KeyStore keyStore) {
        FileOutputStream fous = null;
        try {
            fous = new FileOutputStream(keyStoreFile);
            this.keyStore.store(fous, keyStorePass.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fous) {
                try {
                    fous.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * �߼����
     * @param args �����б�
     */
    public static void main(String[] args) {
        File keyStoreFile = new File("D:\\dev\\trust.jks");
        File certFile = new File("D:\\dev\\alipay.com.cer");
        String keyStorePass = "1";
        KeyStoreManager kmInst = new KeyStoreManager(keyStoreFile, keyStorePass);
        // �������֤��
        kmInst.addTrustItem(certFile);
        // ɾ������֤��
        //kmInst.deleteTrustItem("org.ybygjy.autocert_22697208");
        // ��ӡ֤�����Ŀ��Ϣ
        kmInst.showItemList();
    }
}

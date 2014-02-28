package org.ybygjy.ftp;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * FTPServer��������
 * @author WangYanCheng
 * @version 2011-6-1
 */
public class FTPServerMgrTest {
    private static String parentDirPath;
    private static File keyStoreFile;
    private static File userInfoFile;
    private static FTPServerMgr ftpServerMgr;
    @BeforeClass
    public static void startupFTP() {
        parentDirPath = FTPServerMgrTest.class.getResource("").getFile();
        keyStoreFile = new File(parentDirPath, "ftpserver.jks");
        userInfoFile = new File(parentDirPath, "users.properties");
        ftpServerMgr = new FTPServerMgr();
    }

    @AfterClass
    public static void closeFTP() {
        FTPServerMgr.stopFTPServer();
        System.out.println("�ر�FTPServer���");
    }

    @Test
    public void testStartupFTPServer() {
        ftpServerMgr.startupSimpleFTPServer(21, userInfoFile);
    }
    @Test
    public void testStartupFTP4SSL() {
        String keyStorePassword = "password";
        ftpServerMgr.startupFTPServer4SSL(21, keyStoreFile, keyStorePassword, userInfoFile);
        System.out.println("SSL/TLS FTPServer ������ɡ�����");
    }
    public void testAddUser() {
    }
}

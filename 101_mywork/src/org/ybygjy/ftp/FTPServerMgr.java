package org.ybygjy.ftp;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.FtpStatistics;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.FtpIoSession;
import org.apache.ftpserver.impl.FtpServerContext;
import org.apache.ftpserver.impl.ServerFtpStatistics;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.ybygjy.ftp.event.AbstractFileObserver;

/**
 * ���������FTP���������Ӷ������������
 * @author WangYanCheng
 * @version 2011-5-27
 */
public class FTPServerMgr {
    /** ��ַ */
    private static String hostName = "127.0.0.1";
    /** �˿� */
    private static int hostPort = 21;
    /** �û��� */
    private static String userName = "admin";
    /** ���� */
    private static String password = userName;
    /** ftpServer */
    private static FtpServer ftpServer;
    /** ftpServerMonitor*/
    private static FtpServerMonitor ftpServerMonitor = null;
    /** ftpServer*/
    private static FtpServerFactory ftpServerFactory = null;
    /**
     * Constructor
     */
    public FTPServerMgr() {
        ftpServerMonitor = new FtpServerMonitor();
        ftpServerMonitor.start();
    }
    /**
     * destroy ftp connection
     * @param ftpClient
     */
    public static void destroyFTPClient(FTPClient ftpClient) {
        try {
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����FTP����
     * @param port �˿ں�
     * @param userInfoFile userInfoFile
     */
    public void startupSimpleFTPServer(final int port, final File userInfoFile) {
        Thread tmpThread = new Thread(new Runnable() {
            public void run() {
                while (null == ftpServer) {
                    ftpServerFactory = new FtpServerFactory();
                    ListenerFactory listenerFactory = new ListenerFactory();
                    listenerFactory.setPort(port < 21 ? 21 : port);
                    ftpServerFactory.addListener("default", listenerFactory.createListener());
                    ftpServerFactory.setUserManager(createUserMgr(userInfoFile));
                    ftpServer = ftpServerFactory.createServer();
                    try {
                        ftpServer.start();
                    } catch (FtpException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            tmpThread.start();
            tmpThread.join();
            tmpThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����SSL/TLS���Ƶ�FTP����
     * @param port �˿ں�
     * @param keyStoreFile ��ȫ֤��
     * @param keyStorePassword ֤������
     * @param userInfoFile ftp�����û���Ϣ�ļ�
     */
    public void startupFTPServer4SSL(final int port, final File keyStoreFile,
                                     final String keyStorePassword, final File userInfoFile) {
        Thread tmpThread = new Thread(new Runnable() {
            public void run() {
                while (null == ftpServer) {
                    ftpServerFactory = new FtpServerFactory();
                    ListenerFactory listenerFactory = new ListenerFactory();
                    listenerFactory.setPort(port);
                    SslConfigurationFactory sslFactory = new SslConfigurationFactory();
                    sslFactory.setKeystoreFile(keyStoreFile);
                    sslFactory.setKeystorePassword(keyStorePassword);
                    listenerFactory.setSslConfiguration(sslFactory.createSslConfiguration());
                    listenerFactory.setImplicitSsl(true);
                    ftpServerFactory.addListener("default", listenerFactory.createListener());
//                    ftpServerFactory.setUserManager(createUserMgr(userInfoFile));
                    ftpServerFactory.setUserManager(new CNGIFtpUserManager("admin", new Md5PasswordEncryptor()));
                    ftpServer = ftpServerFactory.createServer();
                    try {
                        ftpServer.start();
                        ftpServerMonitor.setFtpServer(ftpServer);
                        registerFtplet(ftpServer);
                    } catch (FtpException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            tmpThread.start();
            tmpThread.join();
            tmpThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create UserManager Instance
     * @param userInfoFile userInfoFile
     * @return userManager/null;
     */
    private static UserManager createUserMgr(File userInfoFile) {
        PropertiesUserManagerFactory userMgrFactory = new PropertiesUserManagerFactory();
        userMgrFactory.setFile(userInfoFile);
        return userMgrFactory.createUserManager();
    }

    /**
     * ֹͣFTP����
     */
    public static void stopFTPServer() {
        if (null != ftpServer) {
            ftpServer.stop();
        }
    }

    /**
     * ������FTPServer������״̬
     * @author WangYanCheng
     * @version 2011-6-14
     */
    class FtpServerMonitor extends Thread {
        public boolean runFlag;
        private DefaultFtpServer defFtpServer;
        private FtpServerMonitor() {
            this.setDaemon(true);
            runFlag = true;
        }
        public boolean setFtpServer(FtpServer ftpServer) {
            defFtpServer = (ftpServer instanceof DefaultFtpServer) ? (DefaultFtpServer) ftpServer : null;
            return defFtpServer != null;
        }
        public void run() {
            while (runFlag) {
                if (null != defFtpServer) {
                    showUsers();
                    showActiveUser();
                    FtpStatistics ftpStatis = defFtpServer.getServerContext().getFtpStatistics();
                    int connNum = ftpStatis.getCurrentConnectionNumber();
                    int loginNum = ftpStatis.getCurrentLoginNumber();
                    System.out.println("��ǰ������\t" + connNum + "\n��ǰ��½��\t" + loginNum);
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ע��{@link Ftplet}�¼�����
     * @param dfs {@link FtpServer}
     */
    private void registerFtplet(FtpServer ftpServer) {
        DefaultFtpServer dfs = (DefaultFtpServer) ftpServer;
        FtpServerContext serverCtx = dfs.getServerContext();
        ServerFtpStatistics serverFtpStat = (ServerFtpStatistics)serverCtx.getFtpStatistics();
        serverFtpStat.setFileObserver(new AbstractFileObserver(){
            @Override
            public void notifyUpload(FtpIoSession session, FtpFile file, long size) {
                System.out.println(session.getUser());
                //super.notifyUpload(session, file, size);
                System.out.println("�������ϴ��ļ�����==>\n"/*.concat(session.getUserArgument())*/.concat("\n").concat(file.getAbsolutePath()).concat("\t").concat("" + file.getSize()).concat("\n" + size));
            }
        });
    }
    /**
     * ��ǰ����(��½)�û�����
     * <p>��Ϊftp�û��������Ҫ��Ӧ��ϵͳģ�鼯�ɣ�����ftp������������������˵��û���û���</p>
     */
    public void showUsers() {
        if (null == ftpServerFactory) {
            return;
        }
        String[] userNames = null;
        try {
            userNames = ftpServerFactory.getUserManager().getAllUserNames();
        } catch (FtpException e) {
            e.printStackTrace();
        }
        if (userNames != null && userNames.length > 0) {
            for (String userName : userNames) {
                System.out.println("�û���\t" + userName);
            }
        }
    }
    /**
     * ȡ��������ǰ��û�����
     */
    public void showActiveUser() {
        Map<String, Listener> ftpListener = ftpServerFactory.getListeners();
        String[] tmpStrArr = new String[ftpListener.size()];
        tmpStrArr = ftpListener.keySet().toArray(tmpStrArr);
        Listener tmpListener = null;
        for (String tmpStr : tmpStrArr) {
            tmpListener = ftpListener.get(tmpStr);
            System.out.println("��ǰ�����ģ�".concat(tmpStr).concat("\t").concat("��û���\t" + tmpListener.getActiveSessions().size()));
        }
    }
    public void addUser(User user) {
    }
}

package org.ybygjy.ftp;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.security.KeyStore;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.ybygjy.basic.security.KeyStoreUtil;

/**
 * �����װFtpClient�Ĵ���
 * @author WangYanCheng
 * @version 2011-6-3
 */
public class FTPClientMgr {
    private String hostName = "localhost";
    private int hostPort = 21;
    private String userName = "admin";
    private String password = "admin";
    private String characterEncoding = "UTF-8";
    /**
     * Constructor
     */
    public FTPClientMgr(){
    }
    /**
     * Constructor
     * @param hostName ������ַ
     * @param hostPort �˿�
     */
    public FTPClientMgr(String hostName, int hostPort) {
        this.hostName = hostName;
        this.hostPort = hostPort;
    }
    public FTPClientMgr(String hostName, int hostPort, String userName, String userPass) {
        this(hostName, hostPort);
        this.userName = userName;
        this.password = userPass;
    }
    /**
     * ȡFTP����
     * @return ftpClient ftpClient
     */
    public FTPClient createFTPClient() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(hostName, hostPort);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new RuntimeException("��֤ʧ�ܣ�");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient;
    }

    /**
     * ��������SSL
     * @param clientKeyStore
     * @param keyStorePassword
     * @return ftpsClient/null
     */
    public FTPClient createFTPClient4SSL(File clientKeyStore, String keyStorePassword) {
        FTPSClient ftpClient = new FTPSClient(true);
        char[] keyStorePassWD = keyStorePassword.toCharArray();
        KeyStore keyStore = KeyStoreUtil.loadKeyStore(clientKeyStore, keyStorePassWD);
        ftpClient.setKeyManager(KeyStoreUtil.getKeyManager(keyStore, keyStorePassWD));
        ftpClient.setTrustManager(KeyStoreUtil.getTrustKeyManager(keyStore));
        ftpClient.setAuthValue("SSL");
        ftpClient.setEnabledProtocols(new String[] {"SSLv3"});
        ftpClient.setControlEncoding(characterEncoding);
        ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
            public void protocolReplyReceived(ProtocolCommandEvent event) {
                System.out.println("FTPSClient ��Ӧ��Ϣ:\t".concat(event.getMessage().trim()));
            }

            public void protocolCommandSent(ProtocolCommandEvent event) {
                System.out.println("FTPSClient ��Ӧ��Ϣ:\t".concat(event.getMessage().trim()));
            }
        });
        try {
            ftpClient.connect(hostName, hostPort);
            // ftpClient.login(userName, password);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (ftpClient.isConnected() ? ftpClient : null);
    }

    /**
     * �����֤
     * @param ftpClient �Ự����
     * @param userName �û���
     * @param password ����
     * @return true/false
     */
    public static boolean login(FTPClient ftpClient, String userName, String password) {
        boolean rtnFlag = false;
        try {
            rtnFlag = ftpClient.login(userName, password);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return rtnFlag;
    }
}

package org.ybygjy.basic.oracle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * java�ⲿϵͳ����ģʽ�������ݿ�
 * @author WangYanCheng
 * @version 2010-6-19
 */
public class BackupOracleDatabase {
    /**
     * ��װִ�����
     */
    public void doWork() {
        InnerClass innerInst =
            new InnerClass("DB_USER", "DB_PASS", "192.168.0.7:1521/VERSION", "E:/dbback/BACK201006.DMP");
        Thread thInst = new Thread(innerInst);
        thInst.start();
    }

    /**
     * ���ڷֵ�ִ������
     * @author WangYanCheng
     * @version 2010-6-19
     */
    class InnerClass implements Runnable {
        /** �û��� */
        private String userName;
        /** ���� */
        private String userPass;
        /** �����ַ */
        private String serverAddr;
        /** ����ļ�·�� */
        private String outFilePath;

        /**
         * constructor
         * @param userName userName
         * @param userPass userPass
         * @param serverAddr �����ַ�����<strong>�˿�</strong>\<strong>SID</strong>
         * @param outFilePath outPath
         */
        public InnerClass(String userName, String userPass, String serverAddr, String outFilePath) {
            this.userName = userName;
            this.userPass = userPass;
            this.serverAddr = serverAddr;
            this.outFilePath = outFilePath;
        }

        /**
         * �ṩִ�е����
         * @return commStr ���
         */
        private List<String> doBuildCommand() {
            List<String> rtnList = new ArrayList<String>();
            rtnList.add("EXP");
            rtnList.add("@USER@/@PASSWORD@@@SERVER@".replaceAll("@USER@", this.userName)
                .replaceAll("@PASSWORD@", this.userPass).replaceAll("@SERVER@", this.serverAddr));
            rtnList.add("FILE=\"@FILE@\"".replaceAll("@FILE@", this.outFilePath));
            rtnList.add("TABLES=(SY_TABLE_DEF)");
            return rtnList;
        }
        /**
         * ����ProcessBuilderʵ��
         * @param workDir ��ǰ���̹���Ŀ¼
         * @return pbInst ProcessBuilderʵ��
         * @see ProcessBuilder
         */
        private ProcessBuilder buildProcessBuilder(File workDir) {
            new ProcessBuilder();
            List commandArray = (doBuildCommand());
            ProcessBuilder pbInst = new ProcessBuilder(commandArray);
            pbInst.command(commandArray);
            Map envMap = pbInst.environment();
            envMap.clear();
            envMap.putAll(System.getenv());
            pbInst.directory(workDir);
            pbInst.redirectErrorStream(true);
            return pbInst;
        }
        /**
         * �߳�ִ�����
         */
        public void run() {
            File tmpOutFile = new File(this.outFilePath);
            File outDir = tmpOutFile.getParentFile();
            ProcessBuilder pbInst = buildProcessBuilder(outDir);
            try {
                Process proInst = pbInst.start();
                final InputStream ins = proInst.getInputStream();
                File outFile = new File(outDir, "exp.log");
                outFile.createNewFile();
                final FileChannel focInst = new FileOutputStream(outFile).getChannel();
                Thread innerTh = new Thread() {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    public void run() {
                        ReadableByteChannel rbcObj = Channels.newChannel(ins);
                        try {
                            while (rbcObj.read(byteBuffer) != -1) {
                                byteBuffer.flip();
                                focInst.write(byteBuffer);
                                byteBuffer.clear();
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                };
                // innerTh.setDaemon(true);
                innerTh.start();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        new BackupOracleDatabase().doWork();
    }
}

package org.ybygjy.basic.file.cv2Swf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ʹ��FlashPaper��ָ���ļ�ת��swf
 * @author WangYanCheng
 * @version 2009-11-25
 */
public class ConvertFile2Swf {
    /**�����ⲿ��������*/
    private static String commandStr = "FlashPrinter.exe";
    /**ת������ļ�����*/
    private String fileName = "";
    /**
     * ת�����
     * @param filePath �ļ�·��
     * @param outputPath ���·��
     * @param logFileObj ��־�ļ�
     * @throws IOException io�쳣
     */
    public void doConvert(String filePath, String outputPath, File logFileObj) throws IOException {
        File fileObj = new File(filePath), outFile = new File(outputPath);
        if (fileObj.isFile() && fileObj.canRead()) {
            if (!logFileObj.exists()) {
                outFile.mkdirs();
                logFileObj.createNewFile();
            }
            List<String> commandList = new ArrayList<String>();
            commandList.add(commandStr);
            commandList.add(filePath);
            commandList.add("-o");
            fileName = fileObj.getName().replaceAll("[.]{1}.*$", ".swf");
            commandList.add(outputPath.concat(fileName));
            ProcessBuilder proBuild = new ProcessBuilder();
            proBuild.command(commandList);
            Map<String, String> proBEnv = proBuild.environment();
            proBEnv.clear();
            proBEnv.putAll(System.getenv());
            proBuild.directory(outFile);
            proBuild.redirectErrorStream(true);
            Process proObj = proBuild.start();
            try {
                proObj.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int exitValue = proObj.exitValue();
            System.out.println(exitValue);
            System.out.println("ת�����");
        }
    }
    /**
     * ����������Ϣ�߳�
     * @author WangYanCheng
     * @version 2009-11-26
     */
    class InputThread implements Runnable {
        /**readable channel*/
        private ReadableByteChannel rbcChObj = null;
        /**writable channel*/
        private WritableByteChannel wriChObj = null;
        /**runnable flag*/
        private boolean runFlag = true;
        /**byte buffer*/
        private ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
        /**
         * the runFlag to set
         * @param runFlagRef runFlag{true/false}
         */
        public void setRunFlag(boolean runFlagRef) {
            this.runFlag = runFlagRef;
        }
        /**
         * Constructor
         * @param insObj insObj
         * @param logFile logFile
         */
        public InputThread(InputStream insObj, File logFile) {
            rbcChObj = Channels.newChannel(insObj);
            try {
                wriChObj = Channels.newChannel(new FileOutputStream(logFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Thread currThread = new Thread(this);
            currThread.setDaemon(true);
            currThread.start();
        }
        /**
         * {@inheritDoc}
         */
        public void run() {
            while (runFlag) {
                try {
                    while (rbcChObj.read(byteBuffer) != -1) {
                        byteBuffer.flip();
                        wriChObj.write(byteBuffer);
                        byteBuffer.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ��¼��־�߳�
     * @author WangYanCheng
     * @version 2009-11-25
     */
    class OutputThread implements Runnable {
        /**writable channel*/
        private WritableByteChannel writeChannelObj = null;
        /**log channel*/
        private FileChannel fileChannelObj = null;
        /**thread flag*/
        private boolean runFlag = true;
        /**global cache*/
        private ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
        /**
         * the runFlag to set
         * @param runFlagRef ref
         */
        public void setRunFlag(boolean runFlagRef) {
            this.runFlag = runFlagRef;
        }
        /**
         * Constructor
         * @param outRef outRef
         * @param logFileRef logFileRef
         * @throws IOException IOException
         */
        public OutputThread(OutputStream outRef, File logFileRef) throws IOException {
            this.writeChannelObj = Channels.newChannel(outRef);
            this.fileChannelObj = new FileOutputStream(logFileRef).getChannel();
            Thread currT = new Thread(this);
            currT.setDaemon(true);
            currT.start();
        }
        /**
         * {@inheritDoc}
         */
        public void run() {
            while (runFlag) {
                try {
                    while (writeChannelObj.write(byteBuffer) != 0) {
                        byteBuffer.flip();
                        fileChannelObj.write(byteBuffer);
                        byteBuffer.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            doReleaseMemory();
        }
        /**
         * release memory
         */
        public void doReleaseMemory() {
            this.byteBuffer.clear();
            try {
                this.fileChannelObj.close();
                this.writeChannelObj.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * �׳��쳣��Log
     * @param args arguments lists
     */
    public static void main(String[] args) {
        ConvertFile2Swf cf2sObj = new ConvertFile2Swf();
        String filePath = "D:\\work\\workspace\\mywork\\src\\org\\ybygjy\\file\\cv2Swf\\file\\servlet-2_3-fcs-spec.pdf";
        String fileOutPath = "D:\\work\\workspace\\mywork\\src\\org\\ybygjy\\file\\cv2Swf\\file\\";
        try {
            cf2sObj.doConvert(filePath, fileOutPath, new File(fileOutPath, "execute.log"));
//            cf2sObj.doConvert(args[0], args[1], new File(args[2], "execute.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

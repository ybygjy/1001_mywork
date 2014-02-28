package org.ybygjy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Jar�ļ�����
 * @author WangYanCheng
 * @version 2013-1-21
 */
public class JarFileUtil {

    /**
     * ��ѹJar�ļ�
     * @param jarFile ָ��jar�ļ�
     * @param tarDir ָ����ѹ�ļ���
     * @throws IOException �׳��쳣
     */
    public static void uncompress(File jarFile, File tarDir) throws IOException {
        JarFile jfInst = new JarFile(jarFile);
        Enumeration<JarEntry> enumEntry = jfInst.entries();
        while (enumEntry.hasMoreElements()) {
            JarEntry jarEntry = enumEntry.nextElement();
            //�����ѹ�ļ�ʵ��
            File tarFile = new File(tarDir, jarEntry.getName());
            //�����ļ�
            makeFile(jarEntry, tarFile);
            if (jarEntry.isDirectory()) {
                continue;
            }
            //���������
            FileChannel fileChannel = new FileOutputStream(tarFile).getChannel();
            //ȡ������
            InputStream ins = jfInst.getInputStream(jarEntry);
            transferStream(ins, fileChannel);
        }
    }

    /**
     * ������
     * @param ins ������
     * @param targetChannel �����
     */
    private static void transferStream(InputStream ins, FileChannel targetChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
        ReadableByteChannel rbcInst = Channels.newChannel(ins);
        try {
            while (-1 != (rbcInst.read(byteBuffer))) {
                byteBuffer.flip();
                targetChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (null != rbcInst) {
                try {
                    rbcInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != targetChannel) {
                try {
                    targetChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ��ӡjar�ļ�������Ϣ
     * @param fileInst jar�ļ�
     */
    public static void printJarEntry(File fileInst) {
        JarFile jfInst = null;;
        try {
            jfInst = new JarFile(fileInst);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<JarEntry> enumEntry = jfInst.entries();
        while (enumEntry.hasMoreElements()) {
            System.out.println((enumEntry.nextElement()));
        }
    }

    /**
     * �����ļ�
     * @param jarEntry jarʵ��
     * @param fileInst �ļ�ʵ��
     * @throws IOException �׳��쳣
     */
    public static void makeFile(JarEntry jarEntry, File fileInst) {
        if (!fileInst.exists()) {
            if (jarEntry.isDirectory()) {
                fileInst.mkdirs();
            } else {
                try {
                    fileInst.createNewFile();
                } catch (IOException e) {
                    System.out.println("�����ļ�ʧ��>>".concat(fileInst.getPath()));
                }
            }
        }
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        File jarFile = new File("D:\\DEV\\works\\HelloAnt\\build\\SmartPage-Web.jar");
        //JarFileUtil.printJarEntry();
        File targetDir = new File("E:\\test");
        try {
            JarFileUtil.uncompress(jarFile, targetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

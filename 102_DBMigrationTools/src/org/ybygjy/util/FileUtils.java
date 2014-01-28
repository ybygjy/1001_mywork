package org.ybygjy.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ���ڲ���ϵͳ�ļ���������Ĺ�����
 * @author WangYanCheng
 * @version 2012-4-10
 */
public class FileUtils {
    /**
     * ��ȡָ��Ŀ¼�µ��ļ�����
     * <p>
     * 1����������Ŀ¼����
     * <p>
     * 2�������ļ���չ��
     * <p>
     * 3���ļ�����ȫ��ת���ɴ�д
     * @param dirInst Ŀ¼
     * @return rtnFileNameArray �ļ����Ƽ�
     */
    public static String[] fetchFileName(File dirInst) {
        List<String> rtnFileNames = new ArrayList<String>();
        File[] rtnFileArray = dirInst.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() ? false : true;
            }
        });
        for (File fileInst : rtnFileArray) {
            String fileName = fileInst.getName();
            fileName = fileName.replaceAll("\\..*$", "");
            rtnFileNames.add(fileName.toUpperCase());
        }
        String[] rtnFileNameArr = new String[rtnFileNames.size()];
        rtnFileNameArr = rtnFileNames.toArray(rtnFileNameArr);
        return rtnFileNameArr;
    }

    /**
     * �����ļ�Ŀ¼�µ��ļ����ļ�����Ӻ�׺
     * @param fileDir �ļ�Ŀ¼
     * @param prefix ��׺
     */
    public static void addedSuffix4FileName(String fileDir, String prefix) {
        File fileDirInst = new File(fileDir);
        File[] fileInstArray = fileDirInst.listFiles(new FileFilter() {
            @Override
            public boolean accept(File fileInst) {
                return !fileInst.isDirectory();
            }
        });
        for (File tmpFile : fileInstArray) {
            String tmpName = tmpFile.getName();
            tmpName = getFileName(tmpName, prefix);
            boolean rtnFlag = tmpFile.renameTo(new File(tmpFile.getParentFile(), tmpName));
            System.out.println(rtnFlag);
        }
    }

    /**
     * ���ļ�����ʽ���ַ�����Ӻ�׺
     * <p>ʾ��
     * <p>���룺ԭ�ļ���abc.txt����׺�ַ���exe
     * <p>�����abcexe.txt
     * <p>˼·��
     * <p>1���ֽ��ļ���ȡ�ļ���ǰ׺����׺
     * <p>2�������׺�ַ���
     * <p>3��������ս��=ǰ׺+��׺+��׺
     * @param fileName �ļ����ƿɴ���չ��
     * @param extStr ������ַ���
     * @return rtnFlag fileName
     */
    public static String getFileName(String fileName, String extStr) {
        fileName = fileName.toUpperCase();
        String prefixStr = fileName.replaceAll("\\..*$", "");
        if (prefixStr.endsWith(extStr)) {
            System.out.println("���ƣ�".concat(fileName).concat("�Ѿ�����ָ����׺��".concat(extStr)));
        }
        String suffixStr = fileName.lastIndexOf('.') != -1 ? fileName.substring(fileName.lastIndexOf('.')) : "";
        String tmplStr = prefixStr.concat(extStr).concat(suffixStr);
        return tmplStr;
    }

    /**
     * �Ӹ��������ƺ����������ȡ���ʸ��ֽ����ļ���IO������
     * <p>1����������className��Ҫת�������·��
     * <p>2������{@linkplain ClassLoader#getResourceAsStream(String)}
     * <p>ʾ����
     * <p><blockquote>ȡ��FileUtils.class�ļ���<pre>
     * FileUtils.getClassStream("org.ybygjy.FileUtils", FileUtils.class.getClassLoader());
     * </pre></blockquote>
     * @param className ������
     * @param classLoader �������
     * @return rtnStream ������/null
     */
    public static InputStream getClassStream(String className, ClassLoader classLoader) {
        className = className.replaceAll("\\.", "/").concat(".class");
        return classLoader.getResourceAsStream(className);
    }
    
    /**
     * ��������ת�����ļ�ϵͳ
     * @param dir ָ��Ŀ¼
     * @param ins ������{@link InputStream}
     * @return rtnFile ת�����ļ�ϵͳ���ļ�ʵ��
     * @throws IOException {@link IOException}
     */
    public static File restoreInputStream(String dir, String fileName, InputStream ins) throws IOException {
        File fileInst = new File(dir, fileName);
        if (fileInst.exists()) {
            fileInst.delete();
        }
        BufferedOutputStream bos = null;
        byte[] buff = new byte[1024*1024];
        try {
            bos = new BufferedOutputStream(new FileOutputStream(fileInst, false));
            int flag = -1;
            while ((flag = ins.read(buff)) != -1) {
                bos.write(buff, 0, flag);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
        return fileInst;
    }
    /**
     * ȡ�ļ�����������·��
     * @param fileInst {@link File}
     * @return fileName
     */
    public static String getFileName(File fileInst) {
        return fileInst.getName();
    }
}

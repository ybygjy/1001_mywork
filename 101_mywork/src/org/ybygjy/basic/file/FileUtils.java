package org.ybygjy.basic.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��װ�����ļ���ز���
 * @author WangYanCheng
 * @version 2011-11-1
 */
public class FileUtils {
    /***
     * ��ָ����·���¼���ͬ���ļ����統ǰ·����org.ybygjy.HelloWorld����Ҫ��HelloWorld.classͬ������Դ
     * @param classInst �������
     * @param fileName �ļ���
     * @return fileInst/null
     */
    public static File lookup(Class classInst, String fileName) {
        classInst = classInst == null ? FileUtils.class : classInst;
System.out.println("������·����" + classInst);
        URL urlInst = classInst.getResource(fileName);
System.out.println("�����ļ���\n ������·����" + classInst.getResource("") + "\n �ļ�·����" + urlInst);
        return new File(urlInst.getFile());
    }
    /**
     * �ļ�����ƥ�����
     * <p>���ļ����ݽ���Ԥ��������ƥ�䣬�����ƥ����</p>
     * @param fileInst {@link File}
     * @param pattern {@link Pattern}
     * @param fileEncoding �ļ�����
     */
    public static void findAndOutput(File fileInst, Pattern pattern, String fileEncoding, FileUtilsCallback fucbInst) {
        //���ļ�
        //�ļ�����
        String fileContent = readFile(fileInst, fileEncoding);
        //����ƥ��
        Matcher matcher = pattern.matcher(fileContent);
        //������
        while (matcher.find()) {
            int groupCount = matcher.groupCount();
            groupCount = groupCount + 1;
            String[] groupArr = new String[groupCount];
            for (int i = 0; i < groupCount; i++) {
                groupArr[i] = (matcher.group(i));
            }
            if (null != fucbInst) {
                fucbInst.doFindAndOutput(groupArr);
            }
        }
    }
    /**
     * ���ļ�����
     * @param fileInst {@link File}
     * @param fileEncoding �ļ�����
     * @return fileContent
     */
    private static String readFile(File fileInst, String fileEncoding) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        byte[] buff = new byte[1024];
        try {
            fis = new FileInputStream(fileInst);
            baos = new ByteArrayOutputStream();
            int flag = -1;
            while ((flag = fis.read(buff)) != -1) {
                baos.write(buff, 0, flag);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String fileContent = null;
        if (null != baos && baos.size() > 0) {
            try {
                fileContent = baos.toString(fileEncoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return fileContent;
    }
    public static void main(String[] args) throws IOException {
        File matcherResultFile = new File("D:\\MatcherResultFile.txt");
        final FileWriter fileWriter = new FileWriter(matcherResultFile);
        FileUtilsCallback fucall = new FileUtilsCallback(){
            @Override
            public void doFindAndOutput(String[] matcherGroups) {
                try {
                    fileWriter.write(matcherGroups[0]);
                    fileWriter.write('\r');
                    fileWriter.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        String regexpStr = "\\({2}@SNAME@\\)([^\"]+(\"[^\"]+\").*\\)\\))\\.([^;]+);";
        File baseDir = new File("D:\\DEV\\03_project\\20140414_����ϵͳ���\\1007_����ǰ��Service_����");
        File[] serviceFiles = baseDir.listFiles();
        for (File tmpFile : serviceFiles) {
            String fileName = tmpFile.getName();
            String[] tmpArr = fileName.split("_");
            String serviceName = tmpArr[tmpArr.length - 1].replaceAll("\\.[^\\.]+", "");
            serviceName = regexpStr.replaceFirst("@SNAME@", serviceName);
            Pattern pattern = Pattern.compile(serviceName);
            fileWriter.write("FileMatcher Begin=>>" + fileName);
            fileWriter.write('\r');
            fileWriter.write('\n');
            FileUtils.findAndOutput(tmpFile, pattern, "GBK", fucall);
            fileWriter.write("FileMatcher End=>>" + tmpFile.getName());
            fileWriter.write('\r');
            fileWriter.write('\n');
        }
        fileWriter.flush();
        fileWriter.close();
    }
}
interface FileUtilsCallback {
    /**
     * ƥ��ɹ��ص�
     * @param matcherGroups ƥ��ɹ���������
     */
    public void doFindAndOutput(String[] matcherGroups);
}
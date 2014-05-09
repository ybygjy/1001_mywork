package org.ybygjy.basic.file.fileedit;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����Class��·�����Զ���������
 * @author WangYanCheng
 * @version 2014-4-22
 */
public class AutomaticFillPackage4Class {
    /**��Ŀ¼*/
    private File sourceDir;
    private String fileEncode = "UTF-8";
    public AutomaticFillPackage4Class() {
        sourceDir = new File("D:\\DEV\\02_work\\02_financial_server_v1\\src\\main\\java");
        if (!sourceDir.isDirectory()) {
            throw new RuntimeException("����ָ���ļ��У�");
        }
    }
    public void doWork() {
        innerDoWork(sourceDir);
    }
    /**
     * ����Ŀ¼ȡ.java�ļ�
     * @param file
     */
    private void innerDoWork(File file) {
        //1��ȡĿ¼�����ļ�
        File[] tmpFiles = file.listFiles(new CustomerFileFilter());
        //2����֤�ļ��Ƿ�ɶ�/д���Ƿ���.java�ļ�
        for (File tmpFile : tmpFiles) {
            if (tmpFile.isDirectory()) {
                innerDoWork(tmpFile);
            } else if (tmpFile.canWrite()) {
                doUpdateFilePackage(tmpFile);
            }
        }
    }
    /**
     * �����ļ�package
     * @param tmpFile
     */
    private void doUpdateFilePackage(File tmpFile) {
        //2.1��ȡ.java�ļ�����ƥ��packageһ��
        //2.2������.java�ļ�·����ȡpackage������
        //2.3������������ʽ�����滻
        //2.4��д���ļ���ˢ�»�����
        BufferedInputStream rafInst = null;
        ByteArrayOutputStream baos = null;
        FileOutputStream fout = null;
        try {
            rafInst = new BufferedInputStream(new FileInputStream(tmpFile));
            baos = new ByteArrayOutputStream();
            byte[] cbuf = new byte[2048];
            int flag = -1;
            //�����ļ�����
            while ((flag = rafInst.read(cbuf)) != -1) {
                baos.write(cbuf, 0, flag);
            }
            String fileContent = baos.toString(fileEncode);
            rafInst.close();
            baos.close();
            //�����ļ�·����ȡ��·��
            String pagepath = "package " + analysisPackagePath(tmpFile) + ";";
            //�滻/д��
            fileContent = fileContent.replaceFirst("package([^\r\n]+)", pagepath);
            fout = new FileOutputStream(tmpFile, false);
            fout.write(fileContent.getBytes(fileEncode));
            fout.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * �����ļ�·��ȡpackage·��
     * @param tmpFile
     * @return
     */
    private String analysisPackagePath(File tmpFile) {
        String filePath = tmpFile.getParentFile().getPath();
        filePath =
            Pattern.compile(File.separator, Pattern.LITERAL).matcher(filePath).replaceAll(Matcher.quoteReplacement("."));
        Matcher matcher = Pattern.compile(".*(com(\\.\\w+)+)").matcher(filePath);
        String rtnpackagePath = filePath;
        if (matcher.find()) {
            rtnpackagePath = matcher.group(1);
        }
        return rtnpackagePath;
    }
    public static void main(String[] args) {
        new AutomaticFillPackage4Class().doWork();
    }
}
/**
 * �ļ�����
 * @author WangYanCheng
 * @version 2014-4-22
 */
class CustomerFileFilter implements FileFilter {
    @Override
    public boolean accept(File fileInst) {
        return fileInst.isDirectory() || fileInst.getName().matches(".*\\.java$");
    }
    
}

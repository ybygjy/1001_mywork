package org.ybygjy.basic.file;

import java.io.File;
import java.net.URL;

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
}

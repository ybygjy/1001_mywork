package org.ybygjy.ant.openfile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant������Զ����ļ�
 * @author WangYanCheng
 * @version 2012-2-6
 */
public class OpenFile extends Task {
    /**���ļ�ʵ��*/
    private File fileInst;
    
    /**
     * @return the fileInst
     */
    public File getFileInst() {
        return fileInst;
    }

    /**
     * @param fileInst the fileInst to set
     */
    public void setFileInst(File fileInst) {
        this.fileInst = fileInst;
    }

    /* (non-Javadoc)
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {
        OpenFile4OS of4OsInst = new OpenFile4OS();
        if (null == fileInst) {
            throw new RuntimeException("�������ļ�ʵ��Ϊ�ղ��ܴ��ļ�!\t�ļ���ַ{@P}".replaceAll("@P", fileInst.getAbsolutePath()));
        } else if (!fileInst.isFile() || !fileInst.exists()) {
            throw new RuntimeException("�������ļ�ʵ�岻���ļ��򲻴���!\t�ļ���ַ{@P}".replaceAll("@P", fileInst.getAbsolutePath()));
        }
        try {
            of4OsInst.doOpenFile(getFileInst());
        } catch (IOException e) {
            throw new RuntimeException("���ļ�ʱ����!\t�ļ���ַ{@P}".replaceAll("@P", fileInst.getAbsolutePath()));
        }
    }

    /**
     * �������
     * @param args �����б�
     * @throws IOException �׳��쳣��Log
     */
    public static void main(String[] args) throws IOException {
        String tmpFile = "C:\\Documents and Settings\\dell\\����\\Oracle��ϵ�ṹ.ppt";
        String tmpFile2 = "C:\\build\\DBCompare.LOG";
        File file = new File(tmpFile);
        OpenFile4OS of4OsInst = new OpenFile4OS();
        of4OsInst.doOpenFile(file);
        of4OsInst.doOpenURL(new URL("http://www.baidu.com"));
    }

    /**
     * ����ϵͳ���ļ�������
     * @author WangYanCheng
     * @version 2011-10-18
     */
    static class OpenFile4OS {

        /**
         * ���캯��
         */
        public OpenFile4OS() {
        }

        /**
         * ���ļ�
         * @param fileInst �ļ�ʵ��
         * @return rtnFlag true/false
         * @throws IOException �׳��쳣��Log
         */
        public boolean doOpenFile(File fileInst) throws IOException {
            if (osName.startsWith(osMac)) {
                return of4Mac.openFile(fileInst);
            } else if (osName.startsWith(osWindow)) {
                return of4Win.openFile(fileInst);
            } else {
                if (of4jInst.openFile(fileInst)) {
                    System.out.println("�ļ�������");
                    return true;
                }
            }
            return false;
        }

        /**
         * �����ӵ�ַ
         * @param urlInst ��ַʵ��
         * @throws IOException IOException
         */
        public boolean doOpenURL(URL urlInst) throws IOException {
            if (osName.startsWith(osWindow)) {
                return of4Win.browse(urlInst);
            } else if (osName.startsWith(osMac)) {
                return of4Mac.browse(urlInst);
            } else {
                return of4jInst.browse(urlInst);
            }
        }
    }

    /**
     * Linux/Unixϵͳ�´��ļ�������
     * @author WangYanCheng
     * @version 2011-10-19
     */
    static class OpenFile4Linux {
        public boolean browse(URL urlInst) throws IOException {
            for (String cmd : unixBrowseCmds) {
                if (unixCommandExists(cmd)) {
                    Runtime.getRuntime().exec(new String[] { cmd, urlInst.toString() });
                    return true;
                }
            }
            return false;
        }

        public boolean openFile(File fileInst) throws IOException {
            for (String cmd : unixOpenCmds) {
                if (unixCommandExists(cmd)) {
                    Runtime.getRuntime().exec(new String[] { cmd, fileInst.getAbsolutePath() });
                    return true;
                }
            }
            return false;
        }

        public boolean unixCommandExists(String cmd) throws IOException {
            Process procInst = Runtime.getRuntime().exec(new String[] { "which", cmd });
            boolean finished = false;
            do {
                try {
                    procInst.waitFor();
                    finished = true;
                } catch (InterruptedException ine) {
                    ine.printStackTrace();
                }
            } while (!finished);
            return procInst.exitValue() == 0;
        }
    }

    /**
     * Window����ϵͳ�´��ļ�������
     * @author WangYanCheng
     * @version 2011-10-18
     */
    static class OpenFile4Window {
        /**
         * �����ַ
         * @param urlInst ��ַʵ��
         * @return rtnFlag true/false
         * @throws IOException IOException
         */
        public boolean browse(URL urlInst) throws IOException {
            Runtime.getRuntime().exec(
                new String[] { "rundll32", "url.dll,FileProtocolHandler", urlInst.toString() });
            return true;
        }

        /**
         * ���ļ�
         * @param fileInst �ļ�ʵ��
         * @return rtnFlag true/false
         * @throws IOException IOException
         */
        public boolean openFile(File fileInst) throws IOException {
            Runtime.getRuntime().exec(
                new String[] { "rundll32", "shell32.dll,ShellExec_RunDLL",
                        fileInst.getAbsoluteFile().toString() });
            return true;
        }
    }

    /**
     * Mac OS ϵͳʵ��
     * @author WangYanCheng
     * @version 2011-10-18
     */
    static class OpenFile4Mac {
        /**
         * Mac OSϵͳ�������ַ
         * @param urlInst ��ַʵ��
         * @return rtnFlag true/false
         */
        public boolean browse(URL urlInst) {
            Class tmpClass = getAppleFileManagerClass();
            if (null == tmpClass) {
                return false;
            }
            Method openURL;
            try {
                openURL = tmpClass.getDeclaredMethod("openURL", String.class);
                openURL.invoke(null, urlInst.toString());
                return true;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        /**
         * Mac OSϵͳ�´��ļ�
         * @param fileInst �ļ�ʵ��
         * @return rtnFlag true/false
         * @throws IOException IOException
         */
        public boolean openFile(File fileInst) throws IOException {
            return browse(new URL(fileInst.getAbsolutePath()));
        }

        /**
         * ȡϵͳ����ʵ��
         * @return rtnClass rtnClass
         */
        public Class getAppleFileManagerClass() {
            try {
                return Class.forName("com.apple.eio.FileManager");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * ʹ��java.awt.Desktop
     * @author WangYanCheng
     * @version 2011-10-18
     */
    static class OpenFile4Java {
        public boolean browse(URL urlInst) {
            Class desktopClass = getDesktopClass();
            if (null == desktopClass) {
                return false;
            }
            Object desktopObj = getDesktopClassInst(desktopClass);
            if (null == desktopObj) {
                return false;
            }
            try {
                Method browseMethod = desktopClass.getDeclaredMethod("browse", URI.class);
                browseMethod.invoke(desktopObj, new URI(urlInst.toExternalForm()));
                return true;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * ���ļ�
         * @param fileInst �ļ�ʵ��
         * @return rtnFlag true/false
         * @throws IOException �׳��쳣��Log
         */
        public boolean openFile(File fileInst) throws IOException {
            Class desktopClass = getDesktopClass();
            if (null == desktopClass) {
                return false;
            }
            Object desktopInstance = getDesktopClassInst(desktopClass);
            if (null == desktopInstance) {
                return false;
            }
            try {
                Method openMethod = desktopClass.getDeclaredMethod("open", File.class);
                openMethod.invoke(desktopInstance, fileInst);
                return true;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * ȡ�ඨ��ʵ��
         * @return rtnClass rtnClass
         */
        private Class getDesktopClass() {
            try {
                return Class.forName("java.awt.Desktop");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * ȡ��ʵ��
         * @param classTmpl �ඨ��ʵ��
         * @return rtnObj rtnObj
         */
        private Object getDesktopClassInst(Class classTmpl) {
            try {
                Method isDesktopSupportedMethod = classTmpl.getDeclaredMethod("isDesktopSupported");
                boolean isDesktopSupported = (Boolean) isDesktopSupportedMethod.invoke(null);
                if (!isDesktopSupported) {
                    System.out.println("��ǰJDK�汾��֧��java.awt.Desktop���ԣ�");
                }
                Method getDesktopMethod = classTmpl.getDeclaredMethod("getDesktop");
                return getDesktopMethod.invoke(null);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /** ΢��ϵͳ */
    public static String osWindow = "Windows";
    /** ƻ��ϵͳ */
    public static String osMac = "Mac OS";
    /** ����ϵͳ���� */
    public static final String osName = System.getProperty("os.name");;
    /** ����JDK1.6���Դ�/����ļ� */
    private static OpenFile4Java of4jInst = new OpenFile4Java();
    /** Windowƽ̨�´�/����ļ� */
    private static OpenFile4Window of4Win = new OpenFile4Window();
    /** Mac OSƽ̨�´�/����ļ� */
    private static OpenFile4Mac of4Mac = new OpenFile4Mac();
    /** Linux ƽ̨�´�/����ļ� */
    /** UNIXϵͳ���������� */
    public static String[] unixBrowseCmds = { "www-browser", "firefox", "opera", "konqueror",
            "mozilla", "netscape", "w3m" };
    /** Unixϵͳ�ļ��򿪷�ʽ��� */
    public static String[] unixOpenCmds = { "run-mailcap", "pager", "less", "more" };
}

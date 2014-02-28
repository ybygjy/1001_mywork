package org.ybygjy.basic.jvm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

/**
 * Java�ֽ���Ĳ���֮��̬����
 * @author WangYanCheng
 * @version 2011-2-17
 */
public class AutoCompiler {
    /** Դ���� */
    private String source;
    /** ������ */
    private String className;
    /** �������·�� */
    private String outPath = ".";
    /** ��ȡ������ */
    private Pattern packPattern = Pattern.compile("^package\\s+([a-z0-9.]+);");
    /** ��ȡ������ */
    private Pattern classNamePattern = Pattern.compile("class\\s+([^{]+)");

    /**
     * Constructor
     * @param source Դ����
     * @param outPath ��̬�����ļ������·��
     */
    public AutoCompiler(String source, String outPath) {
        this.outPath = outPath;
        this.setSource(source);
    }

    /**
     * ����
     * @return ������ true/false
     * @throws Exception �׳��쳣��Ϣ
     */
    private boolean doCompile() throws Exception {
        return new InnerCompiler(new URI(className), Kind.SOURCE, this.source).compile();
    }

    /**
     * ����
     * @param methodName ��������
     * @return result ���ý��
     */
    public Object doInvoke(String methodName) {
        ClassLoader classLoader = InnerCompiler.class.getClassLoader();
        try {
            Class classDef = classLoader.loadClass(className);
            Method method = classDef.getMethod(methodName, new Class[] {});
            Object result = method.invoke(null, new Object[] {});
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * �Զ�����
     * @param methodName ��������
     * @return resultObj ���ý��
     * @throws Exception ���������쳣�����������
     */
    public Object autoInvoke(String methodName) throws Exception {
        Object result = null;
        if (this.doCompile()) {
            return doInvoke(methodName);
        }
        return result;
    }

    /**
     * �趨��������Դ����
     * @param source compiled source
     */
    public void setSource(String source) {
        String tmpName = analyseClassName(source);
        this.className = tmpName.trim();
        this.source = source;
    }

    /**
     * ����������
     * @param source Դ�ַ���
     * @return className ������/���ַ���
     */
    private String analyseClassName(String source) {
        String tmpName = "";
        Matcher matcher = packPattern.matcher(source);
        if (matcher.find()) {
            tmpName = (matcher.group(1)).concat(".");
        }
        matcher = classNamePattern.matcher(source);
        if (matcher.find()) {
            tmpName = tmpName.concat(matcher.group(1));
        }
        return tmpName;
    }

    /**
     * ָ��������
     * @param className ������
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * �趨���·��
     * @param outPath ���·��
     */
    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AutoCompiler [className=");
        builder.append(className);
        builder.append(", source=");
        builder.append(source);
        builder.append("]");
        return builder.toString();
    }

    /**
     * �����Զ�����
     * @author WangYanCheng
     * @version 2011-2-17
     */
    class InnerCompiler extends SimpleJavaFileObject {
        /** content */
        private String content;

        /**
         * Contructor
         * @param uri uri
         * @param kind kind
         * @param content content
         */
        public InnerCompiler(URI uri, Kind kind, String content) {
            this(uri, kind);
            this.content = content;
        }

        /**
         * Constructor
         * @param uri ����Դ�ļ�·��
         * @param kind �ļ�����
         */
        protected InnerCompiler(URI uri, Kind kind) {
            super(uri, kind);
        }

        /*
         * (non-Javadoc)
         * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
         */
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.content;
        }

        /**
         * ����
         * @return result �ɹ�������{true|false}
         */
        public boolean compile() {
            boolean result = false;
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> fileObject = Arrays.asList(this);
            Iterable options = Arrays.asList("-d", outPath);
            CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObject);
            result = task.call();
            return result;
        }
    }

    /**
     * �������
     * @param args �����б�
     * @throws Exception �׳��쳣
     */
    public static void main(String[] args) throws Exception {
        String methodName = "compilerTest";
        StringBuilder source = new StringBuilder(
            "package org.ybygjy.basic.jvm; class TestCompiler {public static String compilerTest(){return \"HelloWorld\";}}");
        AutoCompiler dctInst = new AutoCompiler(source.toString(), "./webRoot/WEB-INF/classes");
        System.out.println(dctInst.autoInvoke(methodName));
        // if (dctInst.doCompile()) {
        // Object obj = dctInst.doInvoke(methodName);
        // System.out.println(obj);
        // }
    }
}

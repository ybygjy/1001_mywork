package org.ybygjy.basic.jvm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

/**
 * ���Զ�̬���룬�����������
 * <p>1�������������֮������ļ��洢·��(��<code>options -d</code>����)<p>
 * <p>1.1�������������涨Ϊ��ѡ</p>
 * <p>1.2��Ĭ�ϱ�����class�ļ���洢��Ŀ¼����</p>
 * <p>1.3������˵��</p>
 * <table cellpadding=1 cellspacing=1 border=1 summary="Capturing group numberings">
 * <tr><th>�趨ֵ</th><th>��Ӧ���</th></tr>
 * <tr><td>.</td><td>��Զ���Ŀ¼·�������������ʹ��������ʱ��������������ʹ��IDE����ʱ�������Ŀ¼·������ȷ��</td></tr>
 * <tr><td>����·����(C://)</td><td>��Ӧ�����C://�������ǵ�ClassLoader����ʱ��Ҫָ����Ŀ¼��������Ҫ���鷳���ʿ����Բ�ǿ��</td></tr>
 * <tr><td>����Ŀ¼����·����(D:\\mywork\\webRoot\\classes)</td><td>������Ҳ���Ƽ����������ڽ�������ֲʱ����ͻ�͹��</td></tr>
 * <tr><td>ʹ��Ŀ¼���·��(./webRoot/WEB-INF/classes)</td><td>�Ƽ�������������ʹ�ô˰취</td></tr>
 * <tr><td>������Ϊһ��Bean������</td><td>�˷������ǽ����������Ĳ���ѡ��</td></tr>
 * </table>
 * @author WangYanCheng
 * @version 2011-2-18
 */
public class DynamicCompiler {
    private static double calculate(String exp) throws Exception {
        String className = "org.ybygjy.basic.jvm.CaculatorMain";
        String methodName = "calculate";
        String source = "package org.ybygjy.basic.jvm;class CaculatorMain {public static double calculate(){return (@EXP@);}}"
            .replaceAll("@EXP@", exp);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        StringSourceJavaObject ssjoInst = new StringSourceJavaObject(source.replaceAll("@EXP@", exp),
            className);
        Iterable fileObjects = Arrays.asList(ssjoInst);
        Iterable options = Arrays.asList("-d", /*"D:\\work\\workspace\\mywork\\webRoot\\WEB-INF\\classes"*/"./webRoot/WEB-INF/classes");
        CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObjects);
        boolean result = task.call();
        if (result) {
            ClassLoader loader = DynamicCompiler.class.getClassLoader();
            try {
                Class<?> clazz = loader.loadClass(className);
                Method method = clazz.getMethod(methodName, new Class<?>[] {});
                Object value = method.invoke(null, new Object[] {});
                return (Double) value;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    static class StringSourceJavaObject extends SimpleJavaFileObject {
        private String content;

        public StringSourceJavaObject(String content, String name) throws Exception {
            super(new URI(name), Kind.SOURCE);
            this.content = content;
        }

        /*
         * (non-Javadoc)
         * @see javax.tools.SimpleJavaFileObject#getCharContent(boolean)
         */
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.content;
        }
    }

    /**
     * �������
     * @param args args
     */
    public static void main(String[] args) throws Exception {
        System.out.println(DynamicCompiler.calculate("1.1 + 1.2"));
    }
}

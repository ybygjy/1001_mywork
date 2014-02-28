package org.ybygjy.basic.jvm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * ����̬����
 * <p>
 * 1������Ҫ��target�ļ���classpath�����ҵ�(����δͨ��)
 * </p>
 * <p>
 * 2����ͨ��ָ��target�ļ�ȫ·���ķ�ʽ�ҵ�Դ�ļ�
 * </p>
 * <p>
 * 3��ע��<code>javac</code>��<code>java</code>�������ò�����
 * </p>
 * <p>
 * 3.1��classpath��������·��(������class�ļ�)�����Ҳ�������ұ������java�ļ�
 * </p>
 * <p>
 * 3.2��sourcepath: ����Դ�ļ�·����ָ��������������Դ�ļ�(.java)
 * </p>
 * @author WangYanCheng
 * @version 2011-2-18
 */
public class Compiler {
    /**
     * �������
     * @param args args
     * @throws FileNotFoundException FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // ע��·��ʹ��ȫ·��
        String fullClassPath = "d:/work/workspace/mywork/src/org/ybygjy/basic/jvm/CompilerTarget.java";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        FileOutputStream err = new FileOutputStream("C:\\err.txt");
        int compilationResult = compiler.run(null, null, err, "-sourcepath",
            "d:\\work\\workspace\\mywork\\", "-verbose", fullClassPath);
        if (compilationResult == 0) {
            System.out.println("Done.");
        } else {
            System.out.println("Fail.");
        }
    }
}

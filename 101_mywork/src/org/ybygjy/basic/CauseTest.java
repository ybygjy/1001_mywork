package org.ybygjy.basic;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * �쳣�����ѧϰ
 * <p>
 * 1���Զ����ܼ��쳣
 * <p>
 * 2���Զ�����ܼ��쳣
 * <p>
 * 3���ܼ�����ܼ��쳣��ת��
 * @author WangYanCheng
 * @version 2012-11-15
 */
public class CauseTest {
    public void doWork() {
        doTestUncheckedException();
    }

    public void doTestCheckedException() throws CheckedException {
        throw new CheckedException("checked Exception", null);
    }

    public void doTestUncheckedException() {
        try {
            doTestCheckedException();
        } catch (CheckedException e) {
            throw new UnCheckedException(e);
        }
    }
    public static void main(String[] args) {
        new CauseTest().doWork();
    }
}

class CheckedException extends Exception {
    public CheckedException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public CheckedException(Throwable cause) {
        super(cause);
    }
}

class UnCheckedException extends RuntimeException {
    public UnCheckedException(Throwable cause) {
        super(cause);
    }
}

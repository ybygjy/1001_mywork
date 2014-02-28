package org.ybygjy.basic.collect.maptest;


/**
 * java.util.Dictionary����<br>
 * <strong>��֤ĳ�������ڼ̳и�����ĳ������ʵ�ֽӿ������ķ����غ�ʱ�Ĵ���</strong>
 * @author WangYanCheng
 * @version 2010-8-16
 */
public class DictionaryTest {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        InnerClassATestPart icatpInst = new InnerClassATestPart();
        System.out.println(icatpInst.getInnerSize4Interface().size());
        System.out.println(icatpInst.size());
    }
}
/**
 * ʵ��������
 * @author WangYanCheng
 * @version 2010-8-16
 */
abstract class InnerClassA {
    /**
     * ��������ʵ�ֽӿ�
     * @return rtnSize rtnSize
     */
    public abstract int size();
}
/***
 * �ӿ�����
 * @author WangYanCheng
 * @version 2010-8-16
 */
interface InnerInterfaceA {
    /**
     * ����ʵ�ֽӿ�
     * @return rtnSize rtnSize
     */
    String size();
    //String size();
}
/**
 * InnerClassATestPart
 * @author WangYanCheng
 * @version 2010-8-16
 */
class InnerClassATestPart extends InnerClassA {
    @Override
    public int size() {
        return 0;
    }
    /**
     * get innerSize
     * @return innerSizeInst
     */
    public InnerSizeClass getInnerSize4Interface() {
        return new InnerSizeClass();
    }
    /**
     * InnerSizeClass
     * @author WangYanCheng
     * @version 2010-8-16
     */
    protected class InnerSizeClass implements InnerInterfaceA {
        /**
         * {@inheritDoc}
         */
        public String size() {
            return "Hello World. This is a debug message,come from InnerSizeClass";
        }
    }
}

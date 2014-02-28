package org.ybygjy.memcached;

import java.io.Serializable;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * Memcached���ʵ��
 * <p>1��Memcached����</p>
 * @author WangYanCheng
 * @version 2011-5-6
 */
public class SampleTest {
    /**Memcached��������ַ*/
    public static final String[] SERVER_ADDRS= {"172.16.5.37:11211"};
    /**Ȩ��*/
    public static Integer[] weights = {1};
    /**MemCached����*/
    private MemCachedClient mccInst;
    private SockIOPool sockPool;
    /**��������û�������*/
    private String[][] testData;
    /**��������û�����������*/
    private TestEntity[] testData4Obj;
    /**
     * ���췽�������ڳ�ʼ�����Ի���
     */
    public SampleTest() {
        mccInst = new MemCachedClient();
        sockPool = SockIOPool.getInstance(true);
        sockPool.setServers(SERVER_ADDRS);
        sockPool.setWeights(weights);
        sockPool.initialize();
        generalTestData(10);
    }
    /**
     * ���������û�������
     * @param size ���ݼ���С
     */
    public void generalTestData(int size) {
        testData = new String[size][2];
        testData4Obj = new TestEntity[size];
        for (int i = size - 1; i>=0; i--) {
            testData[i][0] = "Key_" + i;
            testData[i][1] = "Value����" + Math.random();
            testData4Obj[i] = new TestEntity("KeyObj_" + i, "��" + i + "������");
        }
    }
    /**
     * ����set����
     */
    public void testSet() {
        for (int i = testData.length - 1; i >= 0; i--) {
            mccInst.set(testData[i][0], testData[i][1]);
        }
    }
    /**
     * ����set�������
     */
    public void testSetObj() {
        for (int i = testData4Obj.length - 1; i >=0; i--) {
            mccInst.set(testData4Obj[i].getSerialCode(), testData4Obj[i]);
        }
    }
    /**
     * ����get����
     */
    public void testGet() {
        Object[] obj = new Object[testData.length];
        for (int i = obj.length - 1; i >= 0; i--) {
            obj[i] = mccInst.get(testData[i][0]);
        }
        for (int i = 0; i < obj.length; i++) {
            System.out.println(testData[i][0] + "\t" + obj[i]);
        }
    }
    /**
     * ����get����
     */
    public void testGetObj() {
        Object[] obj = new Object[testData4Obj.length];
        for (int i = 0; i < testData4Obj.length; i++) {
            obj[i] = mccInst.get(testData4Obj[i].getSerialCode());
        }
        for (int i = 0; i < obj.length; i++) {
            System.out.println(testData[i][0] + "\t" + obj[i]);
        }
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        SampleTest st = new SampleTest();
        st.testSet();
        st.testGet();
        st.testSetObj();
        st.testGetObj();
    }
}
/**
 * ������Ե���ʱ����
 * @author WangYanCheng
 * @version 2011-5-6
 */
class TestEntity implements Serializable {
    /**
     * serialVersion
     */
    private static final long serialVersionUID = 4011683150758956871L;
    /**����*/
    private String serialCode;
    /**��Ϣ����*/
    private String testInfo;
    public TestEntity(String serialCode, String testInfo) {
        super();
        this.serialCode = serialCode;
        this.testInfo = testInfo;
    }
    
    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(String testInfo) {
        this.testInfo = testInfo;
    }

    @Override
    public String toString() {
        return "TestEntity [serialCode=" + serialCode + ", testInfo=" + testInfo + "]";
    }
}

package org.ybygjy.jcip.chap5;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ����ʵ��{@link HashMap}��{@link ConcurrentHashMap}���������ܲ���
 * @author WangYanCheng
 * @version 2014��7��31��
 */
public class HashMapPerformanceTesting {
    /** �������贴�����߳�����*/
    private int threadNums;
    /** ���߳�ѭ������*/
    private int loopNums;
    /** ����Ŀ�������Ķ�/д����*/
    private float rwRatio;
    /** ����������*/
    private final Map<Object, Object> targetContainer;
    /**
     * ���췽��
     * @param threadNums �߳�����
     * @param loopNums ѭ������
     * @param rwRatio ��/д����
     * @param targetContainer ����������
     */
    public HashMapPerformanceTesting(int threadNums, int loopNums, float rwRatio, Map<Object, Object> targetContainer) {
        this.threadNums = threadNums;
        this.loopNums = loopNums;
        this.rwRatio = rwRatio;
        this.targetContainer = targetContainer;
    }
    /**
     * �����������
     */
    public void doTest() {
        
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        //���������ĳ���
        //�������#��ʼ��
        //�������#������
        //�����ʼ��ڣ����𴴽��߳�
    }
}

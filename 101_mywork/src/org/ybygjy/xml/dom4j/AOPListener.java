package org.ybygjy.xml.dom4j;

import org.dom4j.Element;

/**
 * ����ʵ��AOP���ƵĽӿڶ���,�����ض�xml�ĵ������ļ���
 * @author WangYanCheng
 * @version 2010-8-3
 */
public interface AOPListener {
    /**
     * �����ĵ�����֮ElementԪ��
     * @param elemInst elemInst
     */
    void afterParseElement(Element elemInst);
    /**
     * �����ĵ�����֮ElementԪ��
     * @param elemInst elemInst
     */
    void beforeParseElement(Element elemInst);
}

package org.ybygjy.web.log;

import org.apache.log4j.PropertyConfigurator;


/**
 * ��־����
 * @author WangYanCheng
 * @version 2010-8-16
 */
public class LogMgr {
    /**
     * ��ʼ����־������
     * @param confFileUrl �����ļ���ַ
     */
    public void doInitLogProperty(String confFileUrl) {
        System.out.println("��־������==>".concat(confFileUrl));
        PropertyConfigurator.configure(confFileUrl);
    }
}

package org.ybygjy.gui.springswing;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * �������������ʼ��Swing���
 * @author WangYanCheng
 * @version 2011-2-16
 */
public class Launcher {
    /**
     * �������spring�������
     */
    public void launch() {
        String[] contextPaths = new String[] {"org/ybygjy/gui/springswing/app-context.xml"};
        ClassPathXmlApplicationContext cpxac = new ClassPathXmlApplicationContext(contextPaths);
    }
}

package org.ybygjy.gui.springswing;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * �����
 * @author WangYanCheng
 * @version 2011-2-16
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    /**
     * �����ʼ��ܻ�������
     */
    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setState(JFrame.NORMAL);
        setVisible(true);
    }
}

package org.ybygjy.ui;

import javax.swing.SwingUtilities;

/**
 * ����Ǩ�Ƴ���ͼ�ν������
 * @author WangYanCheng
 * @version 2012-4-13
 */
public class UIDataMigration {

    /**
     * ִ�����
     * @param args �����б�
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                UIMainJFrame uimjInst = new UIMainJFrame();
                uimjInst.setVisible(true);
            }
        });
    }
}

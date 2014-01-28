package org.ybygjy.ui.layout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * GridBagLayoutѧϰ
 * @author WangYanCheng
 * @version 2012-9-3
 */
public class GridBagLayoutTest_02 extends JPanel {
    /**
     * serial number
     */
    private static final long serialVersionUID = 3806062656882037283L;
    private JButton btn1,btn2,btn3;
    private JButton btn4;
    private JButton btn5;
    private JButton btn6;
    private JButton btn7;
    private JButton btn8;
    private JButton btn9,btn10;
    /**
     * Constructor
     */
    public GridBagLayoutTest_02() {
        GridBagLayout gblInst = new GridBagLayout();
        GridBagConstraints gbcInst = new GridBagConstraints();
        this.setLayout(gblInst);
        gbcInst.fill = GridBagConstraints.BOTH;//�������չ��/��
        gbcInst.weightx = 1.0;//�������Ȩ��(Ȩ��Ĭ��ֵΪ0)
        gbcInst.weighty = 1.0;
        //gbcInst.insets = new Insets(5, 5, 5, 5);
        gbcInst.ipady = 50;
        makeButton("Btn1", gblInst, gbcInst);
        makeButton("Btn2", gblInst, gbcInst);
        makeButton("Btn3", gblInst, gbcInst);
        gbcInst.gridwidth = GridBagConstraints.REMAINDER;//��ʶ����������һ��������������Table��colspan/rowspan
        makeButton("Btn4", gblInst, gbcInst);
        gbcInst.weightx = 0.0;//�������Ȩ��(Ĭ��ֵ) reset to the default
        makeButton("Btn5", gblInst, gbcInst);
        gbcInst.gridwidth = GridBagConstraints.RELATIVE;//��ʶ������ǵ����ڶ���
        makeButton("Btn6", gblInst, gbcInst);
        gbcInst.gridwidth = GridBagConstraints.REMAINDER;//��ʶ����������һ��
        makeButton("Btn7", gblInst, gbcInst);
        gbcInst.gridwidth = 1;//���ø����ռ��һ��
        gbcInst.gridheight = 2;//���ø����ռ�ö���
        gbcInst.weighty = 0.0;//��������Ȩ��(Ȩ��Ĭ��ֵΪ0)
        makeButton("Btn8", gblInst, gbcInst);
        gbcInst.weighty = 0;//����Ȩ�ػָ�Ĭ��ֵ
        gbcInst.gridwidth = GridBagConstraints.REMAINDER;//��ʶ����������һ��
        gbcInst.gridheight = 1;//���ø����ռ��һ��
        makeButton("Btn9", gblInst, gbcInst);
        makeButton("Btn10", gblInst, gbcInst);
    }
    /**
     * ��װButton�������
     * @param name �������
     * @param gridBag {@link GridBagLayout}
     * @param gridBagConstraint {@link GridBagConstraints}
     */
    private void makeButton(String name, GridBagLayout gridBag, GridBagConstraints gridBagConstraint) {
        JButton btn = new JButton(name);
        gridBag.setConstraints(btn, gridBagConstraint);
        this.add(btn);
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        JFrame jframeInst = new JFrame();
        jframeInst.setLayout(new BorderLayout());
        jframeInst.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframeInst.getContentPane().add(new GridBagLayoutTest_02());
        jframeInst.pack();
        jframeInst.setSize(jframeInst.getPreferredSize());
        jframeInst.setVisible(true);
    }
}

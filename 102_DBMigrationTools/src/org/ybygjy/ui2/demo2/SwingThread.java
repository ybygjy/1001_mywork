package org.ybygjy.ui2.demo2;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * �߳���Swing
 * @author WangYanCheng
 * @version 2012-10-31
 */
public class SwingThread extends JFrame implements Observer {
    /**
     * serial ID
     */
    private static final long serialVersionUID = 6365731511993325046L;
    /** ȫ�ֶ��� */
    private Map<String, JTextField> context;
    /** ���������IDǰ׺ */
    private static String INPUT_FIELD_PREFIX = "INS_";
    /** ��������IDǰ׺ */
    private static String OUTPUT_FIELD_PREFIX = "OUS_";

    /**
     * Constructor
     */
    public SwingThread() {
        context = new HashMap<String, JTextField>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel ctxPanel = new JPanel();
        ctxPanel.setBorder(new TitledBorder("����쳲���������"));
        System.out.println(getContentPane().getLayout());
        GridLayout glInst = new GridLayout(10, 1);
        ctxPanel.setLayout(glInst);
        getContentPane().add(ctxPanel);
        createTask(ctxPanel);
        pack();
    }

    /**
     * ��������
     * @param mainPanel �����
     */
    private void createTask(JPanel mainPanel) {
        for (int i = 1; i <= 10; i++) {
            JLabel tmplJLIn = new JLabel("���룺");
            JLabel tmplJLOu = new JLabel("�����");
            JPanel jp = new JPanel();
            jp.setBorder(new TitledBorder("��".concat(String.valueOf(i)).concat("��")));
            jp.add(tmplJLIn);
            JTextField jtf = new JTextField(5);
            final String inputKey = INPUT_FIELD_PREFIX.concat(String.valueOf(i));
            regInsComp(inputKey, jtf);
            jp.add(jtf);
            jp.add(tmplJLOu);
            JTextField jtfo = new JTextField(5);
            final String outputKey = OUTPUT_FIELD_PREFIX.concat(String.valueOf(i));
            regInsComp(outputKey, jtfo);
            jp.add(jtfo);
            JButton jtnBtn = new JButton("����");
            jtnBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    FibBean fbInst = new FibBean();
                    fbInst.setInputKey(inputKey);
                    fbInst.setOutputKey(outputKey);
                    fbInst.setFibTerm(getInsValue(inputKey));
                    new FibonacciTask(SwingThread.this, fbInst);
                }
            });
            jp.add(jtnBtn);
            mainPanel.add(jp);
        }
    }

    /**
     * ע�����
     * @param key key
     * @param jtf obj
     */
    private void regInsComp(String key, JTextField jtf) {
        context.put(key, jtf);
    }

    /**
     * ȡ���ֵ
     * @param key key
     * @return rtnV Ĭ��Ϊ0
     */
    private long getInsValue(String key) {
        return context.containsKey(key) ? Integer.parseInt(context.get(key).getText()) : 0;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SwingThread().setVisible(true);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof FibBean) {
            final FibBean fibBean = (FibBean) arg;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    context.get(fibBean.getOutputKey()).setText(String.valueOf(fibBean.getFibValue()));
                }
            });
        }
    }
}

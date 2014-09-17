package org.ybygjy.jcip.chap9;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Swingִ�г�ʱ������ķ�ʽ
 * <p>����������Swingִ�к�ʱ����ĳ��÷�ʽ</p>
 * <p>1������SwingUtilities#invokeLater��invokeAndWait����swing���״̬�ķ��ʷ�յ��¼��߳�</p>
 * <p>2������ִ�з����ṩ�����ĺ���߳�����ִ�к�ʱ����</p>
 * @author WangYanCheng
 * @version 2014��9��8��
 */
public class LongTermTaskPart1 {
	/** ����ִ��������̳߳�*/
	private static ExecutorService backgroundExec = Executors.newCachedThreadPool();
	/**
	 * �������
	 * @param args �����б�
	 */
	public static void main(String[] args) {
		JFrame jframe = new JFrame("��ʱ������");
		jframe.setSize(300, 300);
		final JButton startBtn = new JButton("StartBtn");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog((JButton) e.getSource(), "HelloWorld");
				startBtn.setEnabled(false);
				startBtn.setText("Busy");
				backgroundExec.execute(new Runnable() {
					public void run() {
						int step = 1;
						while (true) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println(step);
							if (step % 20 == 0) {
								break;
							}
							step++;
						}
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								startBtn.setEnabled(true);
								startBtn.setText("complete");
							}
						});
					}
				});
			}
		});
		jframe.getContentPane().add(startBtn);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
}

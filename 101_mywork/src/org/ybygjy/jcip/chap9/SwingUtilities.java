package org.ybygjy.jcip.chap9;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * ʹ��Executorʵ��SwingUtilities
 * <p>1��ʹ��Executorʵ��Swing��SwingUtilitiesʵ�ֻ���</p>
 * <p>2��ע��invokeLater��invokeAndWait������</p>
 * @author WangYanCheng
 * @version 2014��9��8��
 */
public class SwingUtilities {
	/** ����executorService*/
	private static final ExecutorService executorService = Executors.newCachedThreadPool(new SwingThreadFactory());
	/** swing thread*/
	private static volatile Thread swingThread;
	/** �̴߳�������*/
	private static class SwingThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable r) {
			swingThread = new Thread(r);
			return swingThread;
		}
	}
	public static boolean isEventDispatchThread() {
		return Thread.currentThread() == swingThread;
	}
	public static void invokeLater(Runnable task) {
		executorService.execute(task);
	}
	public static void invokeAndWait(Runnable task) throws InterruptedException, InvocationTargetException {
		try {
			executorService.submit(task).get();
		} catch (ExecutionException e) {
			throw new InvocationTargetException(e);
		}
	}
	/**
	 * 
	 */
	public static void shutdown() {
		executorService.shutdown();
	}
	/**
	 * �������
	 * @param args �����б�
	 */
	public static void main(String[] args) {
		//����ʮ���̣߳�ÿ���߳��ύʮ������
		//�������������ȴ������߳�ȫ��ִ����ϵ�����
		final CountDownLatch endGate = new CountDownLatch(10);
		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable(){
				public void run() {
					for (int i = 0; i < 10; i++) {
//						try {
//							SwingUtilities.invokeAndWait(new Runnable() {
//								@Override
//								public void run() {
//									for (int i = 0; i < 10; i++) {
//										try {
//											Thread.currentThread().sleep(1000);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
//										System.out.println(Thread.currentThread().getName() + "#" + Calendar.getInstance().getTime() + "#" + SwingUtilities.isEventDispatchThread());
//									}
//									endGate.countDown();
//								}
//							});
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
						try {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									for (int i = 0; i < 10; i++) {
										try {
											Thread.currentThread().sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										System.out.println(Thread.currentThread().getName() + "#" + Calendar.getInstance().getTime() + "#" + SwingUtilities.isEventDispatchThread());
									}
									endGate.countDown();
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}, "Thread_" + i).start();
		}
		try {
			endGate.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
System.out.println("���������");
		SwingUtilities.shutdown();
	}
}

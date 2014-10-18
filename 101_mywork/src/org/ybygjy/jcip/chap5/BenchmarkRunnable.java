package org.ybygjy.jcip.chap5;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * ����ִ�и��������Ĳ�������
 * @author WangYanCheng
 * @version 2014��10��15��
 */
public class BenchmarkRunnable implements Runnable {
	/**����������*/
	private BenchmarkMapWrapper container;
	/**��������洢Ԫ�ظ���*/
	private int maxSize;
	/**ѭ������*/
	private int loopTimes;
	/**��ʱͳ��*/
	private long timeConsume;
	/**�����������ڲ���������ͳһ�����¼�*/
	private CountDownLatch latch;
	/**�ص�*/
	private BenchmarkRunnableCallback callback;
	/**
	 * ���캯��
	 * @param container {@link BenchmarkMapWrapper}
	 * @param maxSize ��������洢Ԫ�ظ���
	 * @param loopTimes ѭ������
	 */
	public BenchmarkRunnable(BenchmarkMapWrapper container, int maxSize, int loopTimes, CountDownLatch latch, BenchmarkRunnableCallback callback) {
		super();
		this.container = container;
		this.maxSize = maxSize;
		this.loopTimes = loopTimes;
		this.latch = latch;
		this.callback = callback;
	}

	@Override
	public void run() {
		final long beginningTime = System.nanoTime();
		innerDoWork();
		this.timeConsume = System.nanoTime() - beginningTime;
		if (this.callback != null) {
			this.callback.callback(this);
		}
	}

	/**
	 * �������ִ�в����߼�
	 */
	private void innerDoWork() {
		//1.��ʼ�����������
		Random random = new Random();
		//2.ѭ��ָ������
		int i = 0;
//		int writeTimes = 0;
		while (i <= this.loopTimes) {
			Object key = random.nextInt(this.maxSize);
			if (this.container.get(key) == null) {
				this.container.put(key, key);
//				writeTimes++;
			}
			i++;
		}
//		System.out.println(Thread.currentThread().getName() + ":writeTimes:" + writeTimes);
		//���±�����ֵ
		this.latch.countDown();
	}
	/**
	 * ȡ������ʱ�����룩
	 * @return
	 */
	public long getTimeConsume() {
		return this.timeConsume;
	}
}

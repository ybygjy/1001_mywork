package org.ybygjy.jcip.chap5;

import java.util.Random;

/**
 * ����ִ�и��������Ĳ�������
 * @author WangYanCheng
 * @version 2014��10��15��
 */
public class BenchMarkRunnable implements Runnable {
	/**����������*/
	private BenchMarkMapWrapper container;
	/**��������洢Ԫ�ظ���*/
	private int maxSize;
	/**ѭ������*/
	private int loopTimes;
	/**��ʱͳ��*/
	private long timeConsume;
	/**
	 * ���캯��
	 * @param container {@link BenchMarkMapWrapper}
	 * @param maxSize ��������洢Ԫ�ظ���
	 * @param loopTimes ѭ������
	 */
	public BenchMarkRunnable(BenchMarkMapWrapper container, int maxSize, int loopTimes) {
		super();
		this.container = container;
		this.maxSize = maxSize;
		this.loopTimes = loopTimes;
	}

	@Override
	public void run() {
		final long beginningTime = System.currentTimeMillis();
		innerDoWork();
		this.timeConsume = System.currentTimeMillis() - beginningTime;
	}

	/**
	 * �������ִ�в����߼�
	 */
	private void innerDoWork() {
		//1.��ʼ�����������
		Random random = new Random();
		//2.ѭ��ָ������
		int i = 0;
		int writeTimes = 0;
		while (i <= this.loopTimes) {
			Object key = random.nextInt(this.maxSize);
			if (this.container.get(key) == null) {
				this.container.put(key, key);
				writeTimes++;
			}
			i++;
		}
		System.out.println(Thread.currentThread().getName() + ":writeTimes:" + writeTimes);
	}
	/**
	 * ȡ������ʱ�����룩
	 * @return
	 */
	public long getTimeConsume() {
		return this.timeConsume;
	}
}

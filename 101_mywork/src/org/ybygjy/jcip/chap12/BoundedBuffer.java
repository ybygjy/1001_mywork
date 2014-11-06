package org.ybygjy.jcip.chap12;

import java.util.concurrent.Semaphore;

/**
 * �����ź������н绺��
 * <p>1.ʹ��{@link Semaphore}���������Ϊ���еı߽�</p>
 * <p>2.</p>
 * @author WangYanCheng
 * @version 2014��11��6��
 */
public class BoundedBuffer<E> {
	/** ���п�������������*/
	private Semaphore availableItems;
	/** ���п��ÿռ�*/
	private Semaphore availableSpaces;
	/** ���巺���������ڴ洢������*/
	private final E[] items;
	/** ����λ��*/
	private int putPosition = 0;
	/** ����λ��*/
	private int takePosition = 0;
	/**
	 * ���캯��
	 * @param capacity ���г���
	 */
	public BoundedBuffer(int capacity) {
		this.availableItems = new Semaphore(0);
		this.availableSpaces = new Semaphore(capacity);
		this.items = ((E[]) new Object[capacity]);
	}
	/**
	 * �����Ƿ�Ϊ��
	 * @return rtnBoolean true/false
	 */
	public boolean isEmpty() {
		return this.availableItems.availablePermits() == 0;
	}
	/**
	 * �����Ƿ�����
	 * @return rtnBoolean true/false
	 */
	public boolean isFull() {
		return this.availableSpaces.availablePermits() == 0;
	}
	/**
	 * ���
	 * @param x
	 * @throws InterruptedException
	 */
	public void put(E x) throws InterruptedException {
		this.availableSpaces.acquire();
		this.doInsert(x);
		this.availableItems.release();
	}
	/**
	 * ����
	 * @return
	 * @throws InterruptedException
	 */
	public E take() throws InterruptedException {
		this.availableItems.acquire();
		E item = doExtract();
		this.availableSpaces.release();
		return item;
	}
	/**
	 * ���
	 * @param x
	 */
	private synchronized void doInsert(E x) {
		int i = this.putPosition;
		items[i] = x;
		this.putPosition = (++i == this.items.length) ? 0 : i;
	}
	/**
	 * ����
	 * @return rtnObj
	 */
	private synchronized E doExtract() {
		int i = this.takePosition;
		E x = this.items[i];
		items[i] = null;
		this.takePosition = (++i == items.length) ? 0 : i;
		return x;
	}
}

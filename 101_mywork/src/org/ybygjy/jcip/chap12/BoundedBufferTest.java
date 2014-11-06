package org.ybygjy.jcip.chap12;

import junit.framework.TestCase;

/**
 * �������ܲ���
 * @author WangYanCheng
 * @version 2014��11��6��
 */
public class BoundedBufferTest extends TestCase {
	/**
	 * ����Ϊ��
	 */
	public void testIsEmptyWhenConstructed() {
		BoundedBuffer<Integer> boundedBuffObj = new BoundedBuffer<Integer>(10);
		assertTrue(boundedBuffObj.isEmpty());
	}
	/**
	 * ������
	 * @throws InterruptedException
	 */
	public void testIsFullAfterPuts() throws InterruptedException {
		BoundedBuffer<Integer> boundedBuffObj = new BoundedBuffer<Integer>(10);
		for (int i = 0; i < 10; i++) {
			boundedBuffObj.put(i);
		}
		assertTrue(boundedBuffObj.isFull());
		assertFalse(boundedBuffObj.isEmpty());
	}
	/**
	 * ����������Ϊ�Լ����жϵ���Ӧ��
	 * <p>�Ӳ����̴߳���һ���������̣߳����̸߳����������Ե����顣</p>
	 * <p>���߳̿����ô����Ķ����߳�״̬ȷ�������Ƿ�ɹ�</p>
	 */
	public void testTakeBlocksWhenEmpty() {
		final BoundedBuffer<Integer> boundedBuffObj = new BoundedBuffer<Integer>(10);
		Thread taker = new Thread(new Runnable(){
			public void run() {
				try {
					int unused = boundedBuffObj.take();
					fail("Fail=>" + unused);
				} catch (InterruptedException e) {
				}
			}
		}, "InnerBlockTestThread");
		try {
			taker.start();
			Thread.sleep(2000);
			taker.interrupt();
			taker.join(1000);
			assertFalse(taker.isAlive());
		} catch (Exception ex) {
			fail();
		}
	}
}

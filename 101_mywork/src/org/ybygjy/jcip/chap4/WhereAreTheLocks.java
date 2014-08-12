package org.ybygjy.jcip.chap4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ������������⡣
 * <p>1���˴�����֤�ͻ�����ȷ�Ͳ���ȷ�����ĳ���</p>
 * <p>2�����Թ۲쵽addIfAbsent��δ�������ǵ�Ҫ�󣬱Ƚ�����Կ����ظ�ֵ</p>
 * @author WangYanCheng
 * @version 2014��8��13��
 */
public class WhereAreTheLocks {
	private static final CountDownLatch latch = new CountDownLatch(6);
	/**
	 * ����ִ�����
	 * @param args �����б�
	 */
	public static void main(String[] args) {
		CorrectAndAnCorrectLocked calInst = new CorrectAndAnCorrectLocked();
		for (int i = 0; i < 6; i++) {
			new TestThread("WhereAreTheLocks_" + i, calInst).start();
		}
	}
	/**
	 * �����߳���
	 * @author WangYanCheng
	 * @version 2014��8��13��
	 */
	static class TestThread extends Thread {
		private CorrectAndAnCorrectLocked calInst;
		/**
		 * ���캯��
		 * @param threadName �߳�����
		 * @param calInst {@link CorrectAndAnCorrectLocked}
		 */
		public TestThread(String threadName, CorrectAndAnCorrectLocked calInst) {
			super(threadName);
			this.calInst = calInst;
		}
		@Override
		public void run() {
			for (int i = 0; i < 21; i++) {
				long v = (long) (Math.random() * 10);
				if ((long)(Math.random() * 10) % 2 == 0) {
					calInst.add(v);
				} else {
					calInst.addIfAbsent(v);
				}
				try {
					sleep((long) (Math.random() * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			latch.countDown();
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(getName() + ">>" + calInst.toString());
		}
	}
}
class CorrectAndAnCorrectLocked {
	/** 1.����֧�ֲ���������list����*/
	//TODO �������
	public List<Object> list = (List<Object>) Collections.synchronizedList(new ArrayList<Object>());
	/**
	 * ��ͨ
	 * @param obj
	 */
	public void add(Object obj) {
		//TODO �������
		synchronized(list) {
			list.add(obj);
		}
	}
	/**
	 * �����ǰlist�в����ڸ�Ԫ������룬�������κβ���
	 * @param obj
	 * @return rtnFlag {true:����δ���,false:�����������}
	 */
	public boolean addIfAbsent(Object obj) {
		//TODO �������
		synchronized(this) {
			boolean isAbsent = !this.list.contains(obj);
			if (!isAbsent) {
				this.list.add("T_" + obj);
			}
			return isAbsent;
		}
	}
	public String toString() {
		return list.toString();
	}
}
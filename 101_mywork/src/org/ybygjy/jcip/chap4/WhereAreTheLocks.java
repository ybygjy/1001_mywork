package org.ybygjy.jcip.chap4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ������������⡣
 * <p>1���˴�����֤�ͻ�����ȷ�Ͳ���ȷ�����ĳ���</p>
 * <p>2�����Թ۲쵽addIfAbsent��δ�������ǵ�Ҫ�󣬴ӽ�����Կ����ظ�ֵ</p>
 * <p>
 * 3����������
 * 3.1������һ��List����չ�࣬���ฺ����չList�Ĺ��ܡ�
 * 3.2������չ�����¼���addIfAbsent�������ú��������ʹ��������������չ��list�����Բ������̰߳�ȫ��
 * 3.3�����������߳��࣬����ģ����Ի���
 * 3.4����������̣߳�������ȡ��д�����ݵ�list����չʵ����
 * 3.5����ӡlist��չʵ�����ݣ��۲���Խ��
 * <p>
 * 4��С��
 * 4.1��ͨ�����ԭ�Ӳ�������չ�ִ���ǳ���������Ϊ��Ὣ��ļ�������ֲ���������С�
 * 4.2���ͻ��˼������Ӵ�������Ϊ����ԭʼ��ļ�������ŵ���ԭʼ����ȫ�޹ص��������С�
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
		ListHelper calInst = new ListHelper();
		//����6���߳�
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
		private ListHelper calInst;
		/**
		 * ���캯��
		 * @param threadName �߳�����
		 * @param calInst {@link ListHelper}
		 */
		public TestThread(String threadName, ListHelper calInst) {
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
/**
 * ����List�����չ�࣬���ฺ����չList�Ĺ��ܣ����Ǹ�����Ϊʹ���˴�����������Բ������̰߳�ȫ�ԣ��������������ͬ���ļ���
 * @author WangYanCheng
 * @version 2014��8��13��
 */
class ListHelper {
	/**
	 * 1.����֧�ֲ���������list����
	 * 2.ע��List��ʵ�ֿͻ��˼������ⲿ����ʱʹ��ͬһ������
	 */
	public List<Object> list = (List<Object>) Collections.synchronizedList(new ArrayList<Object>());
	/**
	 * ��ͨ
	 * @param obj
	 */
	public void add(Object obj) {
		synchronized(list) {
			if (list.contains(obj)) {
				return;
			}
			list.add(obj);
		}
	}
	/**
	 * �����ǰlist�в����ڸ�Ԫ������룬�������κβ���
	 * <p>����listʹ����һ��������������״̬�����ǿ���ȷ�����ǣ������������</p>
	 * @param obj
	 * @return rtnFlag {true:����δ���,false:�����������}
	 */
	public boolean addIfAbsent(Object obj) {
		synchronized(this) {
			boolean isAbsent = !this.list.contains(obj);
			String tmpStr = "T_" + obj;
			if (!isAbsent) {
				this.list.add(tmpStr);
			}
			return isAbsent;
		}
	}
	public String toString() {
		return list.toString();
	}
}
package org.ybygjy.jcip.chap5;

import java.util.HashMap;
import java.util.Map;

/**
 * ��֤���������¶�Map�����ĵ����׳�ConcurrentModificationException�쳣
 * <p>1.���ڵĲ���������HashTable</p>
 * @author WangYanCheng
 * @version 2014��10��19��
 */
public class MapConcurrentModificationException {
	private final Map<String, String> hashTableObj;
	/**
	 * ���캯����ʹ��
	 */
	public MapConcurrentModificationException() {
		this.hashTableObj = new HashMap<String, String>();
	}
	/**
	 * �������
	 */
	public void doWork() {
		//��������̸߳���д��
		for (int i = 0; i < 3; i++) {
			new Thread(){
				public void run() {
					while (true) {
						String key = String.valueOf(Math.random());
						hashTableObj.put(key, key);
						try {
							sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		//��������̸߳����������
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread() {
				public void run() {
					while (true) {
						System.out.println(hashTableObj.toString());
						/*
						Iterator<Map.Entry<String, String>> iterator = hashTableObj.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry<String, String> entry = iterator.next();
							System.out.println(entry.getKey() + ":" + entry.getValue());
						}
						*/
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread.start();
		}
	}
	/**
	 * �������
	 * @param args �����б�
	 */
	public static void main(String[] args) {
		MapConcurrentModificationException mmeInst = new MapConcurrentModificationException();
		mmeInst.doWork();
	}
}

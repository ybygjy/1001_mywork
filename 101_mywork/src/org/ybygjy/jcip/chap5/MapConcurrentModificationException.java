package org.ybygjy.jcip.chap5;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * ��֤���������¶�Map�����ĵ����׳�ConcurrentModificationException�쳣
 * <p>1.���ڵĲ���������HashTable��������������ʵ���������й����ķ�������һ����</p>
 * <p>2.</p>
 * HashTable��HashMap��toString�����е�ʵ���߼���ͬ,��HashMap�ļ̳нṹ������HashMap�������ϲ�AbstractMap��toString��������HashTableʵ�����Լ���toString������
 * HashMap����Iteratorģʽ���Ի���ConcurrentModificationException��
 * HashTableҲ�ǵ��͵�Iteratorģʽ����HashTable���Ⱪ¶�Ĺ��������Ǽ����ģ���put��toString��
 * ��Ψ��entrySet��values����δֱ�Ӽ���������������ʹ�õ���ͬ������������ʽ��ͬ��������������Ϊ���̰߳�ȫ���������Ӳ�����ȫ��֧�֣����ƴ���ģʽΪ�ض�������IOC/AOP֧��һ������
 * ����HashTable�е�entrySet��values��������ͬ�������������ⷢ�������¹���Ķ��󣬷ֱ���Entry��Collection������Ҫע����������ʵ��ʹ�õ�����HashTableʵ������һ�¡�
 * @author WangYanCheng
 * @version 2014��10��20��
 */
public class MapConcurrentModificationException {
	/**ʵ������*/
	private final Map<String, String> containerObj;
	/**
	 * ���캯����ʹ��
	 */
	public MapConcurrentModificationException() {
		this.containerObj = new Hashtable<String, String>();
		//this.containerObj = new HashMap<String, String>();
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
						containerObj.put(key, key);
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
						System.out.println(containerObj.toString());
						//HashTable������toString�Ǽ����ģ��ڴ�ͨ��sleep����cpu�ӳٶ������ĵ��������ڼ�д�̻߳�д���µ�Ԫ�أ������������ĵ�������ConcurrentModificationException�Ļ��ʾ͸����ˡ�
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Iterator<Map.Entry<String, String>> iterator = containerObj.entrySet().iterator();
						while (iterator.hasNext()) {
							Map.Entry<String, String> entry = iterator.next();
							System.out.println(entry.getKey() + ":" + entry.getValue());
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

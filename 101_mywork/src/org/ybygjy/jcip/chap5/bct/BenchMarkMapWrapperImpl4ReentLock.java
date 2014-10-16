package org.ybygjy.jcip.chap5.bct;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.ybygjy.jcip.chap5.BenchMarkMapWrapper;

/**
 * ��������ʵ��#����������ʵ�������Ĳ�������
 * @author WangYanCheng
 * @version 2014��10��15��
 */
public class BenchMarkMapWrapperImpl4ReentLock implements BenchMarkMapWrapper {
	private HashMap<Object,Object> container;
	private Lock reentLock;
	public BenchMarkMapWrapperImpl4ReentLock() {
		this.container = new HashMap<Object, Object>();
		this.reentLock = new ReentrantLock();
	}
	@Override
	public void put(Object key, Object value) {
		this.reentLock.lock();
		try {
			this.container.put(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.reentLock.unlock();
		}
	}

	@Override
	public Object get(Object key) {
		this.reentLock.lock();
		try {
			return this.container.get(key);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			this.reentLock.unlock();
		}
		return null;
	}

	@Override
	public void clear() {
		this.reentLock.lock();
		try {
			this.container.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.reentLock.unlock();
		}
	}

	@Override
	public String getName() {
		return "ReentLockContainer";
	}

}

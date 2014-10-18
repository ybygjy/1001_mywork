package org.ybygjy.jcip.chap5.bct;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.ybygjy.jcip.chap5.BenchmarkMapWrapper;

/**
 * ��������ʵ��#���ÿ������д��������������
 * @author WangYanCheng
 * @version 2014��10��15��
 */
public class BenchmarkMapWrapperImpl4RWLock implements BenchmarkMapWrapper {
	private HashMap<Object, Object> container;
	private Lock readLock;
	private Lock writeLock;
	/**
	 * ���캯��
	 */
	public BenchmarkMapWrapperImpl4RWLock() {
		this.container = new HashMap<Object, Object>();
		final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
		this.readLock = rwLock.readLock();
		this.writeLock = rwLock.writeLock();
	}
	@Override
	public void put(Object key, Object value) {
		this.writeLock.lock();
		try {
			this.container.put(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.writeLock.unlock();
		}
	}

	@Override
	public Object get(Object key) {
		this.readLock.lock();
		try {
			return this.container.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.readLock.unlock();
		}
		return null;
	}

	@Override
	public void clear() {
		this.writeLock.lock();
		try {
			this.container.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.writeLock.unlock();
		}
	}

	@Override
	public String getName() {
		return "RWReentLockContainer";
	}

}

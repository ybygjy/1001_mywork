package org.ybygjy.jcip.chap5.bct;

import java.util.concurrent.ConcurrentHashMap;

import org.ybygjy.jcip.chap5.BenchmarkMapWrapper;

/**
 * ��������ʵ��#��������ʵ��
 * @author WangYanCheng
 * @version 2014��10��15��
 */
public class BenchmarkMapWrapperImpl4ConcurrentMap implements BenchmarkMapWrapper {
	private ConcurrentHashMap<Object, Object> container;
	/**
	 * Constructor
	 */
	public BenchmarkMapWrapperImpl4ConcurrentMap() {
		this.container = new ConcurrentHashMap<Object, Object>();
	}
	@Override
	public void put(Object key, Object value) {
		this.container.put(key, value);
	}

	@Override
	public Object get(Object key) {
		return this.container.get(key);
	}

	@Override
	public void clear() {
		this.container.clear();
	}

	@Override
	public String getName() {
		return "ConcurrentMapContainer";
	}
}

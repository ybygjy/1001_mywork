package org.ybygjy.cache;

/**
 * �����ӿ����ڳ����崴��{@link Cache}��Ϊ
 * @author WangYanCheng
 * @version 2014-7-11
 */
public interface CacheClientFactory {
	public Cache createCache() throws Exception;
}

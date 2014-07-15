package org.ybygjy.cache;

/**
 * �����ӿ����ڳ����崴��{@link CacheClient}��Ϊ
 * @author WangYanCheng
 * @version 2014-7-11
 */
public interface CacheClientFactory {
	/**
	 * ����{@link CacheClient}ʵ��
	 * @return cacheClient {@link CacheClient}
	 * @throws Exception
	 */
	public CacheClient createCacheClient() throws Exception;
}

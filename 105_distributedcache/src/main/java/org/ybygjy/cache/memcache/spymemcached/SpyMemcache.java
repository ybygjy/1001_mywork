package org.ybygjy.cache.memcache.spymemcached;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import net.spy.memcached.MemcachedClient;

import org.ybygjy.cache.Cache;
import org.ybygjy.cache.CacheExceptionHandler;
import org.ybygjy.cache.LoggingMemcacheExceptionHandler;
/**
 * {@link Cache}��SypMemcacheʵ��
 * @author WangYanCheng
 * @version 2014-7-11
 */
public class SpyMemcache implements Cache {
	private static final Logger log = Logger.getLogger(SpyMemcache.class.getName());
	private static CacheExceptionHandler exceptionHandler = new LoggingMemcacheExceptionHandler();
	/** SpyMemcache�ͻ���ʵ����ͨ�����캯����ʼ��*/
	private final MemcachedClient memcachedClient;
	/**
	 * ���캯��
	 * @param memcachedClient
	 */
	public SpyMemcache(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public Object get(String key) {
		try {
			log.info("org.ybygjy.cache.memcache.spymemcached.SpyMemcache.get(String)=>" + key);
			return memcachedClient.get(key);
		} catch (Exception e) {
			exceptionHandler.handleErrorOnGet(key, e);
		}
		return null;
	}

	public Map<String, Object> getMulti(String... keys) {
		try {
			log.info("org.ybygjy.cache.memcache.spymemcached.SpyMemcache.getMulti(String...)=>" + Arrays.toString(keys));
			return memcachedClient.getBulk(keys);
		} catch (Exception e) {
			exceptionHandler.handleErrorOnGet(Arrays.toString(keys), e);
		}
		return null;
	}

	public void set(String key, int cacheTimeSeconds, Object o) {
		try {
			log.info("org.ybygjy.cache.memcache.spymemcached.SpyMemcache.set(String, int, Object)=>" + key + "#" + o + "#" + cacheTimeSeconds);
			memcachedClient.set(key, cacheTimeSeconds, o);
		} catch (Exception e) {
			exceptionHandler.handleErrorOnSet(key, cacheTimeSeconds, o, e);
		}
	}

	public void delete(String key) {
		try {
			log.info("org.ybygjy.cache.memcache.spymemcached.SpyMemcache.delete(String)=>" + key);
			memcachedClient.delete(key);
		} catch (Exception e) {
			exceptionHandler.handleErrorOnDelete(key, e);
		}
	}

	public void incr(String key, int factor, int startingValue) {
		try {
			log.info("org.ybygjy.cache.memcache.spymemcached.SpyMemcache.incr(String, int, int)=>" + key + "#" + factor + "#" + startingValue);
		} catch (Exception e) {
			exceptionHandler.handleErrorOnIncr(key, factor, startingValue, e);
		}
	}

	public void shutdown() {
		log.info("org.ybygjy.cache.memcache.spymemcached.SpyMemcache.shutdown()");
		memcachedClient.shutdown();
	}

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public static void setExceptionHandler(CacheExceptionHandler exceptionHandler) {
		SpyMemcache.exceptionHandler = exceptionHandler;
	}
}
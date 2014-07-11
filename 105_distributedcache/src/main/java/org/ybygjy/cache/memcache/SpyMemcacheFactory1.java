package org.ybygjy.cache.memcache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Locator;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

/**
 * Memcached�ͻ��˹����ӿ�����
 * <p>���ƻ��桢��־������������Ϣ��������������ʵķ���Ӧ�ŵ�����������ʱֱ�Ӽ��ز���ʼ��������ȵ�����ҵ���߼���ʼʹ��ʱ�ٽ��г�ʼ����</p>
 * @author WangYanCheng
 * @version 2014-7-8
 */
public class SpyMemcacheFactory1 {
	public static int DEFAULT_TIMEOUT = 5000;
	public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;
/*
	hibernate.memcached.servers=10.1.7.199:12000 10.1.7.199:13000
	search.memcached.servers=10.1.200.139:12000,10.1.200.139:13000,10.1.200.139:14000,10.1.200.139:15000
*/
	/** �������Ⱥ*/
	private String memcachedServers;
	/** SpyMemcached�ͻ��˲���API*/
	private MemcachedClient memcachedClient;
	/**
	 * Constructor
	 * @param memcachedServers �����������Ⱥ
	 */
	public SpyMemcacheFactory1(String memcachedServers) {
		this.memcachedServers = memcachedServers;
	}
	/**
	 * ��������ʵ����MemcachedClient
	 * @throws IOException
	 */
	public void init() throws IOException {
		if (memcachedClient != null) {
			return;
		}
		ConnectionFactoryBuilder connFactory = new ConnectionFactoryBuilder();
		connFactory.setFailureMode(FailureMode.Redistribute);
		connFactory.setDaemon(true);
		connFactory.setProtocol(Protocol.TEXT);
		connFactory.setLocatorType(Locator.CONSISTENT);
		connFactory.setHashAlg(DefaultHashAlgorithm.KETAMA_HASH);
		connFactory.setOpTimeout(DEFAULT_TIMEOUT);
		memcachedClient = new MemcachedClient(connFactory.build(), AddrUtil.getAddresses(this.memcachedServers));
	}
	/**
	 * ����MemcachedClient
	 */
	public void disconn() {
		if (memcachedClient == null) {
			return;
		}
		memcachedClient.shutdown();
	}
	/**
	 * ȡ{@link MemcachedClient}�ͻ���
	 * @return memcachedClient
	 */
	public MemcachedClient getMemcachedClient() {
		return this.memcachedClient;
	}
}

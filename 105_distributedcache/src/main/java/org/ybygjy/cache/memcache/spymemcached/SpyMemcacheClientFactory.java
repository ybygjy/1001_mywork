package org.ybygjy.cache.memcache.spymemcached;

import java.util.Properties;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import org.ybygjy.cache.Cache;
import org.ybygjy.cache.CacheClientFactory;

/**
 * �򵥹��������𴴽�SpyMemcacheʵ�ֵ�{@link Cache}
 * @author WangYanCheng
 * @version 2014-7-11
 */
public class SpyMemcacheClientFactory implements CacheClientFactory {
	private Properties properties;
	public SpyMemcacheClientFactory(Properties properties) {
		this.properties = properties;
	}
	public Cache createCache() throws Exception {
		ConnectionFactory connectionFactory = getConnectionFactory();
		MemcachedClient memcachedClient = new MemcachedClient(connectionFactory, AddrUtil.getAddresses(getServerList()));
		Cache rtnCache = new SpyMemcache(memcachedClient);
		return rtnCache;
	}
	private ConnectionFactory getConnectionFactory() {
		if (connectionFactoryNameEquals(DefaultConnectionFactory.class)) {
			return buildDefaultConnectionFactory();
		}
		/*
		if (connectionFactoryNameEquals(KetamaConnectionFactory.class)) {
			return buildKetamaConnectionFactory();
		}
		if (connectionFactoryNameEquals(BinaryConnectionFactory.class)) {
			return buildBinaryConnectionFactory();
		}*/
		throw new IllegalArgumentException("δ��֧�ֵĻ��湤�����ͣ�Key:" + PROP_CONNECTION_FACTORY + "Value:" + getConnectionFactoryName());
	}
	private boolean connectionFactoryNameEquals(Class<?> clazz) {
System.out.println("clazz.getSimpleName()=>" + clazz.getName());
		return clazz.getName().equals(getConnectionFactoryName());
	}
	private String getConnectionFactoryName() {
		String rtnValue = properties.getProperty(PROP_CONNECTION_FACTORY);
		if (rtnValue == null) {
			rtnValue = DefaultConnectionFactory.class.getSimpleName();
		}
System.out.println("rtnValue=>" + rtnValue);
		return rtnValue;
	}
	private DefaultConnectionFactory buildDefaultConnectionFactory() {
		DefaultConnectionFactory rtnConnectionFactory = new DefaultConnectionFactory(){
			@Override
			public AuthDescriptor getAuthDescriptor() {
				return createAuthDescriptor();
			}
			@Override
			public long getOperationTimeout() {
				// TODO Auto-generated method stub
				return super.getOperationTimeout();
			}

			@Override
			public boolean isDaemon() {
				// TODO Auto-generated method stub
				return super.isDaemon();
			}
		};
		return rtnConnectionFactory;
	}
	private AuthDescriptor createAuthDescriptor() {
		String userName = properties.getProperty(PROP_USERNAME);
		String password = properties.getProperty(PROP_PASSWORD);
		if (null == userName || null == password) {
			return null;
		}
		return new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(userName, password));
	}
	/**
	 * ȡ����������б�
	 * @return rtnServerList
	 */
	private String getServerList() {
		return properties.getProperty(PROP_SERVERS);
	}
	/** ����ǰ׺*/
	private static final String PROP_PREFIX = "cache.distribution.";
	/** �����������ַ*/
	private static final String PROP_SERVERS = PROP_PREFIX + "servers";
	/** ���湤��*/
	private static final String PROP_CONNECTION_FACTORY = PROP_PREFIX + "connectionFactory";
	/** �����֤��ʶ_USERNAME*/
	private static final String PROP_USERNAME = PROP_PREFIX + "username";
	/** �����֤��ʶ_PASSWORD*/
	private static final String PROP_PASSWORD = PROP_PREFIX + "password";
}

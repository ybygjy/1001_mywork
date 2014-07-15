package org.ybygjy.cache.memcache.spymemcached;

import java.util.Properties;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.HashAlgorithm;
import net.spy.memcached.KetamaConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import org.ybygjy.cache.CacheClient;
import org.ybygjy.cache.CacheClientFactory;

/**
 * �򵥹��������𴴽�SpyMemcacheʵ�ֵ�{@link CacheClient}
 * @author WangYanCheng
 * @version 2014-7-11
 */
public class SpyMemcacheClientFactory implements CacheClientFactory {
	/** ������Ϣ*/
	private Properties properties;
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
	/** ������ʱ(����)*/
	public static final String PROP_OPERATION_TIMEOUT = PROP_PREFIX + "operationTimeout";
	/** ��̨����ģʽ*/
	public static final String PROP_DAEMON_MODE = PROP_PREFIX + "daemonMode";
	/** �������г���*/
	public static final String PROP_OPERATION_QUEUE_LENGTH = PROP_PREFIX + "operationQueueLength";
	/** ��������С*/
	public static final String PROP_READ_BUFFER_SIZE = PROP_PREFIX + "readBufferSize";
	/** ָ��Hash�㷨*/
	public static final String PROP_HASH_ALGORITHM = PROP_PREFIX + "hashAlgorithm";
	/**
	 * ���췽��
	 * @param properties {@link Properties}
	 */
	public SpyMemcacheClientFactory(Properties properties) {
		this.properties = properties;
	}
	/**
	 * {@inheritDoc}
	 */
	public CacheClient createCacheClient() throws Exception {
		ConnectionFactory connectionFactory = getConnectionFactory();
		MemcachedClient memcachedClient = new MemcachedClient(connectionFactory, AddrUtil.getAddresses(getServerList()));
		CacheClient rtnCache = new SpyMemcache(memcachedClient);
		return rtnCache;
	}
	/**
	 * ����������Ϣ����ֲ�ʽ�������ӹ���ʵ��
	 * @return connectionFactory {@link ConnectionFactory}
	 */
	private ConnectionFactory getConnectionFactory() {
		String connectionFactoryName = getConnectionFactoryName();
		if (connectionFactoryName.equals(DefaultConnectionFactory.class.getName())) {
			return buildDefaultConnectionFactory();
		}
		if (connectionFactoryName.equals(KetamaConnectionFactory.class.getName())) {
			return buildKetamaConnectionFactory();
		}
		if (connectionFactoryName.equals(BinaryConnectionFactory.class.getName())) {
			return buildBinaryConnectionFactory();
		}
		throw new IllegalArgumentException("δ��֧�ֵĻ��湤�����ͣ�Key:" + PROP_CONNECTION_FACTORY + "Value:" + getConnectionFactoryName());
	}
	/**
	 * ���ڶ���������Э��Ļ�������칤��
	 * @return rtnConnFactory {@link BinaryConnectionFactory}
	 */
	private ConnectionFactory buildBinaryConnectionFactory() {
		BinaryConnectionFactory binaryConnectionFactory = new BinaryConnectionFactory(getOperationQueueLength(), getReadBufferSize(), getHashAlgorithm()){
			@Override
			public long getOperationTimeout() {
				return getOperationTimeoutMilli();
			}
			@Override
			public boolean isDaemon() {
				return isDaemonMode();
			}
			@Override
			public AuthDescriptor getAuthDescriptor() {
				return createAuthDescriptor();
			}
		};
		return binaryConnectionFactory;
	}
	/**
	 * ����һ���Թ�ϣ�㷨�Ļ�������칤��
	 * @return rtnFactory {@link KetamaConnectionFactory}
	 */
	private ConnectionFactory buildKetamaConnectionFactory() {
		KetamaConnectionFactory ketamaConnectionFactory = new KetamaConnectionFactory() {
			@Override
			public long getOperationTimeout() {
				return getOperationTimeoutMilli();
			}

			@Override
			public boolean isDaemon() {
				return isDaemonMode();
			}

			@Override
			public AuthDescriptor getAuthDescriptor() {
				return createAuthDescriptor();
			}
		};
		return ketamaConnectionFactory;
	}
	/**
	 * Ĭ�ϻ���������ӹ���
	 * @return rtnConnectionFactory {@link DefaultConnectionFactory}
	 */
	private DefaultConnectionFactory buildDefaultConnectionFactory() {
		DefaultConnectionFactory rtnConnectionFactory = new DefaultConnectionFactory(getOperationQueueLength(), getReadBufferSize(), getHashAlgorithm()){
			@Override
			public AuthDescriptor getAuthDescriptor() {
				return createAuthDescriptor();
			}
			@Override
			public long getOperationTimeout() {
				return getOperationTimeoutMilli();
			}

			@Override
			public boolean isDaemon() {
				return isDaemonMode();
			}
		};
		return rtnConnectionFactory;
	}
	/**
	 * ��ϣ�㷨
	 * @return hashAlgorithm
	 */
	public HashAlgorithm getHashAlgorithm() {
		String tmpValue = properties.getProperty(PROP_HASH_ALGORITHM);
		if (tmpValue == null) {
			return DefaultHashAlgorithm.KETAMA_HASH;
		}
		return Enum.valueOf(DefaultHashAlgorithm.class, tmpValue);
	}
	/**
	 * ���泤�ȣ��ó������ڻ���ͻ����뻺������ͨ��ʱSocket�Ļ�������С
	 * @return buffSizeInt
	 */
	public int getReadBufferSize() {
		String tmpValue = properties.getProperty(PROP_READ_BUFFER_SIZE);
		return tmpValue == null ? DefaultConnectionFactory.DEFAULT_READ_BUFFER_SIZE : Integer.parseInt(tmpValue);
	}
	/**
	 * �������г���
	 * @return rtnValueInt
	 */
	public int getOperationQueueLength() {
		String tmpValue = properties.getProperty(PROP_OPERATION_QUEUE_LENGTH);
		return tmpValue == null ? DefaultConnectionFactory.DEFAULT_OP_QUEUE_LEN : Integer.parseInt(tmpValue);
	}
	/**
	 * ���÷��������֤
	 * @return authDescriptor {@link AuthDescriptor}
	 */
	protected AuthDescriptor createAuthDescriptor() {
		String userName = properties.getProperty(PROP_USERNAME);
		String password = properties.getProperty(PROP_PASSWORD);
		if (null == userName || null == password) {
			return null;
		}
		return new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler(userName, password));
	}
	/**
	 * ����������б�
	 * @return rtnServerList
	 */
	public String getServerList() {
		return properties.getProperty(PROP_SERVERS);
	}
	/**
	 * ��̨����ģʽ
	 * @return isDaemon true/false
	 */
	public boolean isDaemonMode() {
		String tmpValue = properties.getProperty(PROP_DAEMON_MODE);
		return null == tmpValue ? false : Boolean.parseBoolean(tmpValue);
	}
	/**
	 * ������ʱʱ��(Millisecond)
	 * @return rtnLong
	 */
	public long getOperationTimeoutMilli() {
		String tmpValue = properties.getProperty(PROP_OPERATION_TIMEOUT);
		return null == tmpValue ? DefaultConnectionFactory.DEFAULT_OPERATION_TIMEOUT : Long.parseLong(tmpValue);
	}
	/**
	 * ȡ���õĻ������ӹ��������ƣ����������ϢΪ����ȡ{@link DefaultConnectionFactory}
	 * @return connFactoryName
	 */
	public String getConnectionFactoryName() {
		String rtnValue = properties.getProperty(PROP_CONNECTION_FACTORY);
		if (rtnValue == null) {
			rtnValue = DefaultConnectionFactory.class.getSimpleName();
		}
		return rtnValue;
	}
}

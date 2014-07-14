package org.ybygjy.cache.memcache.spymemcached;

import java.io.IOException;
import java.util.Properties;

/**
 * ��������
 * <p>1��װ��{@link Properties}</p>
 * @author WangYanCheng
 * @version 2014-7-14
 */
public class TestUtil {
	private String configFileName = "cache.distributed.properties";
	/**
	 * ȡָ�������ļ��Ĺ���ʵ��
	 * @param configFileLocation �����ļ���ַ
	 * @return properties {@link Properties}
	 */
	public Properties getProperties(String configFileLocation) {
		Properties properties = new Properties();
		try {
			properties.load(TestUtil.class.getClassLoader().getResourceAsStream(configFileLocation));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("����");
		}
		return properties;
	}
	/**
	 * ȡĬ��������Ϣ����ʵ��
	 * @return properties {@link Properties}
	 */
	public Properties getProperties() {
		return this.getProperties(configFileName);
	}
}

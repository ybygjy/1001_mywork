package org.ybygjy.cache;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ������־����ģ���¼�쳣��Ϣ
 * @author WangYanCheng
 * @version 2014-7-11
 */
public class LoggingMemcacheExceptionHandler implements CacheClientExceptionHandler {
	private static Logger logger = Logger.getLogger(LoggingMemcacheExceptionHandler.class.getName());
	public void handleErrorOnGet(String key, Exception e) {
		logger.log(Level.WARNING, "����_ȡ�����쳣{key:" + key + "}", e.getCause());
	}

	public void handleErrorOnSet(String key, int cacheTimeSeconds, Object o, Exception e) {
		logger.log(Level.WARNING, "����_�������쳣{key:" + key + "}", e.getCause());
	}

	public void handleErrorOnDelete(String key, Exception e) {
		logger.log(Level.WARNING, "����_ɾ�����쳣{key:" + key + "}", e.getCause());
	}

	public void handleErrorOnIncr(String key, int factor, int startingValue, Exception e) {
		logger.log(Level.WARNING, "����_�����쳣{key:" + key + ",factor:" + factor + "}", e.getCause());
	}

}

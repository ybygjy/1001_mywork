package org.ybygjy.jcip.chap5;

/**
 * ������������������Ϊ
 * @author WangYanCheng
 * @version 2014��10��15��
 */
public interface BenchMarkMapWrapper {
	/**
	 * ��ֵ�Դ洢
	 * @param key
	 * @param value
	 */
	public void put(Object key, Object value);
	/**
	 * ������ȡֵ
	 * @param key
	 * @return rtnObj
	 */
	public Object get(Object key);
	/**
	 * �������
	 */
	public void clear();
	/**
	 * ȡ��Ʒ��ʶ
	 * @return rtnStr
	 */
	public String getName();
}

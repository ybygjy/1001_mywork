package org.ybygjy.jcip.chap5.bct;

import java.util.Hashtable;
import java.util.Map;

import org.ybygjy.jcip.chap5.BenchMarkMapWrapper;

/**
 * �������Խӿ�HashTableʵ��
 * @author WangYanCheng
 * @version 2014��10��15��
 */
public class BenchMarkMapWrapperImpl4HashTable implements BenchMarkMapWrapper {
    private final Map<Object, Object> map;

    /**
     * Constructor
     */
    public BenchMarkMapWrapperImpl4HashTable() {
        map = new Hashtable<Object, Object>();
    }
    @Override
    public void clear() {
        map.clear();
    }
    @Override
    public Object get(final Object key) {
        return map.get(key);
    }
    @Override
    public void put(final Object key, final Object value) {
        map.put(key, value);
    }
    @Override
    public String getName() {
        return "HashTable Container";
    }
}

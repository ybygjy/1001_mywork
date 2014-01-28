package org.ybygjy.ctx;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ybygjy.MigrationContext;

/**
 * ������Ĭ��ʵ��<strong>���̰߳�ȫ</strong>
 * @author WangYanCheng
 * @version 2012-10-17
 */
public class MigrationContextImpl implements MigrationContext {
    /**���������Լ�*/
    private static Map<String, Object> attributes = new HashMap<String, Object>();
    @Override
    public Object getAttribute(String attrName) {
        return attributes.get(attrName);
    }

    @Override
    public void setAttribute(String attrName, Object attrValue) {
        attributes.put(attrName, attrValue);
    }

    @Override
    public void appendSortedAttr(String attrName, String attrValue) {
        Object tmpObj = MigrationContextFactory.getInstance().getCtx().getAttribute(attrName);
        SortedSet<String> tmpList = tmpObj == null ? new TreeSet<String>() : (SortedSet<String>) tmpObj;
        tmpList.add(attrValue);
        MigrationContextFactory.getInstance().getCtx().setAttribute(attrName, tmpList);
    }
}

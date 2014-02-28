package org.ybygjy.basic.collect.maptest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.ybygjy.basic.TestInterface;

/**
 * �Զ���ӿ�ΪMap����ز���
 * @author WangYanCheng
 * @version 2010-8-23
 */
public class MapTest {
    /** innerClass */
    private static InnerClass[] ic = new InnerClass[10];

    /**
     * Constructor
     */
    public MapTest() {
        Random rand = new Random();
        for (int index = 9; index >= 0; index--) {
            ic[index] = new InnerClass(String.valueOf(index), String.valueOf(rand.nextLong()));
        }
    }

    /**
     * doBuildTestInstArray
     * @return testInstArray
     */
    public TestInterface[] doBuildTestInst() {
        TestInterface[] ti = new TestInterface[10];
        ti[0] = new TreeMapTest();
        ti[1] = new LinkedHashMapTest();
        return ti;
    }

    /**
     * doFillMap
     * @param mapInst targetMapInst
     */
    private void doFillMap(Map mapInst) {
        for (int index = ic.length - 1; index >= 0; index--) {
            InnerClass ict = ic[index];
            mapInst.put(ict.id, ict.label);
        }
    }

    /**
     * ����Map#collection()���������ۣ�<br>
     * <li>ͨ��Collection#toArray(Type[] type)��ʵ�ֽ�Map����ת������</li>
     */
    public void doTestCollection() {
        Map<String, String> mapInst = new HashMap<String, String>();
        mapInst.put("A", "AA");
        mapInst.put("B", "BB");
        String[] tmpStr = new String[mapInst.size()];
        tmpStr = mapInst.values().toArray(tmpStr);
        for (String str : tmpStr) {
            System.out.println(str);
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        /*
         * MapTest mtIns = new MapTest(); TestInterface[] tiArray =
         * mtIns.doBuildTestInst(); for (int i = 0; i < tiArray.length; i++) {
         * if (tiArray[i] != null) { tiArray[i].doTest(); } }
         */
        MapTest mtInst = new MapTest();
        mtInst.doTestCollection();
    }

    /**
     * InnerCompiler
     * @author WangYanCheng
     * @version 2010-8-23
     */
    private class InnerClass {
        /** id */
        String id;
        /** label */
        String label;

        /**
         * Constructor
         * @param id id
         * @param label label
         */
        public InnerClass(String id, String label) {
            this.id = id;
            this.label = label;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("InnerCompiler [id=");
            builder.append(id);
            builder.append(", label=");
            builder.append(label);
            builder.append("]");
            return builder.toString();
        }
    }

    /**
     * doPrint MapInst Content
     * @param tmInst tmInst
     */
    private void doPrint(Map tmInst) {
        doPrint(tmInst, true);
    }

    /**
     * doPrint Map Entry
     * @param tmInst targetMap
     * @param usedIter can used fail-fast iterator
     */
    private void doPrint(Map tmInst, boolean usedIter) {
        if (usedIter) {
            for (Iterator iterator = tmInst.keySet().iterator(); iterator.hasNext();) {
                Object key = iterator.next();
                System.out.println(key + ":" + tmInst.get(key));
            }
        } else {
            Object[] keys = tmInst.keySet().toArray();
            for (int index = 0; index < keys.length; index++) {
                Object value = tmInst.get(keys[index]);
                System.out.println(keys[index] + ":" + value);
            }
        }
        System.out.println();
    }

    /**
     * ֧��Key�����mapʵ�ֲ���
     * @author WangYanCheng
     * @version 2010-8-23
     */
    protected class TreeMapTest implements TestInterface {
        /**
         * {@inheritDoc}
         */
        public void doTest() {
            TreeMap sortedMap = new TreeMap();
            doFillMap(sortedMap);
            doPrint(sortedMap);
        }
    }

    /**
     * �е��������List,���Դ���һЩ����LRU(�������ʹ��)�㷨,��ʵ�ֶ�������Ԫ��
     * @author WangYanCheng
     * @version 2010-8-23
     */
    protected class LinkedHashMapTest implements TestInterface {
        /**
         * {@inheritDoc}
         */
        public void doTest() {
            LinkedHashMap lhmInst = new LinkedHashMap(16, 0.75f, true);
            doFillMap(lhmInst);
            System.out.println(lhmInst);
            for (int index = 0; index < 7; index++) {
                lhmInst.get(String.valueOf(index));
            }
            doPrint(lhmInst, false);
        }
    }

}

package org.ybygjy.basic.random;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;


/**
 * RandomTest
 * @author WangYanCheng
 * @version 2009-12-10
 */
public class RandomTest {
    /**
     * ������λ�����
     * @param bound �߽�
     */
    static void genral4BitNum(int bound) {
        Map<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> shiftMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < bound; i++) {
            int tmpI = (int) (Math.random() * 9000 + 1000);
            if (tmpMap.containsKey(tmpI)) {
                int count = 1;
                count = shiftMap.containsKey(tmpI) ? count + 1 : count;
                shiftMap.put(tmpI, count);
            }
            tmpMap.put(tmpI, tmpI);
        }
        for (Iterator iterator = tmpMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Entry) iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue() + "\t");
        }
        System.out.println("�ظ�");
        for (Iterator iterator = shiftMap.keySet().iterator(); iterator.hasNext();) {
            Integer key = (Integer) iterator.next();
            System.out.println(key + ":" + shiftMap.get(key) + " ��");
        }
    }
    /**
     * ����ָ����Χ�������
     * @param bound �߽�
     * @param rangeB ��ʼ
     * @param rangeE ����
     */
    static void generalAreaNum(int bound, int rangeB, int rangeE) {
        for (int i = 0; i < bound; i++) {
            System.out.println(((int) (Math.random() * rangeE + rangeB)));
        }
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        /*new Thread(new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("nextBoolean:" + new Random().nextBoolean());
                    System.out.println("nextInt:" + new Random().nextInt());
                    System.out.println("nextLong:" + new Random().nextLong());
                    System.out.println("nextFloat:" + new Random().nextFloat());
                    System.out.println("nextDouble:" + new Random().nextDouble());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
        //genral4BitNum(10);
        generalAreaNum(100, 0, 5);
    }
}

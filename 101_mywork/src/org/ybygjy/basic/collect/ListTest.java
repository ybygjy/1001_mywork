package org.ybygjy.basic.collect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ybygjy.test.TestUtils;

/**
 * List TestUser
 * @author WangYanCheng
 * @version 2009-12-13
 */
public class ListTest {
    /**
     * ����List Size�仯��remove
     */
    public void doTestListSize() {
        List<String> listInst = new ArrayList<String>();
        listInst.add("1");
        listInst.add("2");
        listInst.add("3");
        for (int index = 0, len = listInst.size(); index < len;) {
            System.out.println(index + ":" + listInst.get(index));
            listInst.remove(index);
            // index--;
            len--;
        }
    }

    /**
     * ����toArray(T[] tInst) ����
     * <p>1��toArray()�Ǹ���ʵ���ڴ����õ�ַ,�����ⲿ�����ݵ��޸ĻᷴӦ��List�ڵ�ʵ����</p>
     * <p>2��toArray(T[] a) ��ʵ�ֻ��ƣ���a����ĳ���С��ԴArray����ʱʹ��ԴArray����</p>
     */
    public void doTestToArray() {
        List<InnerEntity> tmpListChild = new ArrayList<InnerEntity>();
        InnerEntity rootEntity = new InnerEntity("A", null, null);
        tmpListChild.add(rootEntity);
        InnerEntity ieA = new InnerEntity("A.1", null, rootEntity);

        List<InnerEntity> tmpChildList = new ArrayList<InnerEntity>();
        tmpChildList.add(new InnerEntity("A.1.1", null, ieA));
        tmpChildList.add(new InnerEntity("A.1.2", null, ieA));
        tmpChildList.add(new InnerEntity("A.1.3", null, ieA));
        ieA.setItems(tmpChildList);
        tmpListChild.add(new InnerEntity("A.2", null, rootEntity));
        tmpListChild.add(new InnerEntity("A.3", null, rootEntity));
        InnerEntity[] ieArr = new InnerEntity[0];
        ieArr = tmpChildList.toArray(ieArr);
        System.out.println(ieArr[0].getName() + ":" + ieArr.length);
        ieArr[0].setName("HelloWorld");
        System.out.println(ieArr[0].getName());
    }

    /**
     * ��֤toArray��toArray(obj[] obj)����<br>
     * ���ۣ� <li>����ֱ��ʹ��((String[])arrayList.toArray())ǿ������ת��</li> <li>
     * toArrayִ�е���System.arrayCopy�ڴ�copy����</li> <li>
     * ����ʹ���������ķ�ʽ�����toArray(Object[] obj)����ת��</li>
     */
    public void doTestToArray2() {
        List<String> arrayList = new ArrayList<String>();
        arrayList.add("A");
        arrayList.add("B");
        Object[] strArr = arrayList.toArray();
        System.out.println(strArr.toString());
        String[] strArr2 = new String[arrayList.size()];
        arrayList.toArray(strArr2);
        TestUtils.doPrint(strArr2);
    }

    /**
     * ����List#sizeΪ��ʱget(0)״̬�����ۣ�<br>
     * NullPointException
     */
    public void doTestListSizeE() {
        List<String> rtnList = new ArrayList<String>();
        System.out.println(rtnList.get(0));
    }

    /**
     * ����binarySearch�����ۣ�<br>
     * �ǳ����ֶ����鲻���Ӷ��������ʵ��compareҲ��ֱ�Ӳ������
     */
    public void doTestBinarySearch() {
        String[] strArr = {"A", "B"};
        int flag = Arrays.binarySearch(strArr, "B");
        System.out.println(flag);
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
//        ListTest listTestInst = new ListTest();
        // listTestInst.doTestListSize();
        // listTestInst.doTestListSizeE();
        // listTestInst.doTestToArray();
        // listTestInst.doTestToArray2();
//      listTestInst.doTestBinarySearch();
        List<Integer> tmpList = new ArrayList<Integer>();
        tmpList.add(13);
        tmpList.add(23);
        Integer[] intArr = tmpList.toArray(new Integer[0]);
        System.out.println(intArr.length);
    }
}

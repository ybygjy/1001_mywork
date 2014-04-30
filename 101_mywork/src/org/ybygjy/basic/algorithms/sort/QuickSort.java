package org.ybygjy.basic.algorithms.sort;

/**
 * ��������
 * <p>1������˼��</p>
 * <p>2���ݹ�</p>
 * @author WangYanCheng
 * @version 2014-4-29
 */
public class QuickSort {
    private int[] dataArr;
    public QuickSort(int[] dataArr) {
        this.dataArr = dataArr;
    }
    public void doSort() {
        innerSort(0, dataArr.length - 1);
    }
    /**
     * ����
     * <p>�ݹ����</p>
     * @param left
     * @param right
     */
    private void innerSort(int left, int right) {
        if (right - left <=0) {
            return;
        } else {
            int pivot = dataArr[right];//ȡ���Ҷ�ֵ��Ϊ��Ŧ
            System.out.println("���֣�left:" + left + ",right:" + right + ",pivot:" + pivot);
            int partition = partitionIt(left, right, pivot);//����
            System.out.println("����������left:" + left + ",right:" + (partition - 1));
            innerSort(left, partition - 1);//�����������
            System.out.println("����������left:" + (partition + 1) + ",right:" + right );
            innerSort(partition + 1, right);//�ұ���������
        }
    }
    /**
     * ����
     * @param left
     * @param right
     * @param pivot ��Ŧ
     * @return
     */
    private int partitionIt(int left, int right, int pivot) {
        int leftPtr = left - 1;
        int rightPtr = right;
        while (true) {
            while (dataArr[++leftPtr] < pivot) {
                ;
            }
            while (rightPtr > 0 && dataArr[--rightPtr] > pivot) {
                ;
            }
            if (leftPtr >= rightPtr) {
                break;
            } else {
                swap(leftPtr, rightPtr);
            }
        }
        swap(leftPtr, right);
doPrint();
        return leftPtr;
    }
    /**
     * ����
     * @param leftPtr
     * @param rightPtr
     */
    private void swap(int leftPtr, int rightPtr) {
        int tmpValue = dataArr[leftPtr];
        dataArr[leftPtr] = dataArr[rightPtr];
        dataArr[rightPtr] = tmpValue;
    }
    /**
     * ��ӡ��ǰ����Ԫ��
     */
    public void doPrint() {
        System.out.println(java.util.Arrays.toString(dataArr));
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        int[] dataArr = new int[10];
        for (int i = 0; i < dataArr.length; i++) {
            dataArr[i] = (int) (Math.random() * 100);
        }
        dataArr = new int[]{2, 61, 27, 21, 4, 1, 74, 40, 90, 33};
        QuickSort qsInst = new QuickSort(dataArr);
        qsInst.doPrint();
        qsInst.doSort();
        qsInst.doPrint();
    }
}

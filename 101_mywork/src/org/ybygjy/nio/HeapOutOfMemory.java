package org.ybygjy.nio;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ����ByteArrayOutputStream����Java Heap Space Out of Memory
 * @author WangYanCheng
 * @version 2012-8-30
 */
public class HeapOutOfMemory {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        HeapOutOfMemory hofmInst = new HeapOutOfMemory();
        ByteArrayOutputStream baosInst = new ByteArrayOutputStream();
        while (true) {
            hofmInst.doWork(baosInst);
            System.out.println(baosInst.size()/(1024*1024) + "M");
        }
    }

    /**
     * ��ͣ�Ľ����ݴ�ŵ�Output�����У�ֱ�ӵ��¿ռ����
     * @param baosInst {@link ByteArrayOutputStream}
     */
    public void doWork(ByteArrayOutputStream baosInst) {
        BufferedInputStream bis = (BufferedInputStream) (this.getClass().getClassLoader().getResourceAsStream(this.getClass().getName().replaceAll("\\.", "/")+".class"));
        byte[] buff = new byte[1024 * 1024];
        try {
            while (bis.read(buff) != -1) {
                baosInst.write(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

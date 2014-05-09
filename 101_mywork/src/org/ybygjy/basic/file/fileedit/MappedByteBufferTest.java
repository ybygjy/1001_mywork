package org.ybygjy.basic.file.fileedit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * ѧϰMappedByteBuffer�������
 * @author WangYanCheng
 * @version 2011-12-27
 */
public class MappedByteBufferTest {
    //new File("F://tools//rar//loadrunner//LoadRunner.V8.1.iso");
    private File fileInst = new File("C://sqlnet.log");
    /** �ļ��ַ����� */
    private CharsetDecoder decoder;
    private String tokenStr = "error 12560";
    /**
     * ���췽��
     */
    public MappedByteBufferTest() {
        decoder = Charset.forName("UTF-8").newDecoder();
    }
    /**
     * ����MappedByteBufferֵ
     * @param mbbInst MappedByteBuffer����
     */
    public void showDetail(MappedByteBuffer mbbInst) {
        int position = mbbInst.position();
        int limit = mbbInst.limit();
        int capacity = mbbInst.capacity();
        System.out.println("P:" + position + "\tL:" + limit + "\tC:" + capacity);
        int index = -1;
        ByteBuffer byteBuff = ByteBuffer.wrap(tokenStr.getBytes());
        while ((index = mbbInst.compareTo(byteBuff))!= -1) {
            System.out.println("POSITION:" + index);
            mbbInst.position(index);
        }
    }

    /**
     * ���
     */
    public void doWork() {
        FileChannel channelInst = null;
        try {
            channelInst = new RandomAccessFile(fileInst, "rw").getChannel();
            MappedByteBuffer mbbInst = channelInst.map(MapMode.READ_ONLY, 0, fileInst.length());
            showDetail(mbbInst);
            mbbInst.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != channelInst) {
                try {
                    channelInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        new Thread(new Runnable(){
            /* (non-Javadoc)
             * @see java.lang.Runnable#run()
             */
            public void run() {
                new MappedByteBufferTest().doWork();
            }
        }).start();
        try {
            Thread.sleep(1000*60 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

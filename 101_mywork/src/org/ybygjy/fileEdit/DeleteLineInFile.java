package org.ybygjy.fileEdit;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ɾ���ļ���
 * @author WangYanCheng
 * @version 2011-12-29
 */
public class DeleteLineInFile {
    private File fileInst;
    private int[] delLines;
    private CharsetDecoder decoder;

    /**
     * Constructor
     * @param fileInst �ļ�ʵ��
     * @param delLines ɾ���ļ��У���0��ʼ������Χ���н�������
     */
    public DeleteLineInFile(File fileInst, int[] delLines) {
        this.fileInst = fileInst;
        Arrays.sort(delLines);
        this.delLines = delLines;
        this.decoder = Charset.forName("UTF-8").newDecoder();
    }

    /**
     * ʹ���ƶ��ļ���Ĳ���ɾ��
     */
    public void delete4Buffer() {
        ByteBuffer byteBuff = ByteBuffer.allocate(1024);
        RandomAccessFile rafInst = null;
        FileChannel fileChannel = null;
        try {
            rafInst = new RandomAccessFile(fileInst, "rw");
            fileChannel = rafInst.getChannel();
            readFileLine(fileChannel, byteBuff, fileInst.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fileChannel) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * �����Կ�ķ�ʽ�����ļ���
     * @param fileChannel ͨ��
     * @param byteBuff ������
     * @param fileSize �ļ�ʵ�ʴ�С
     * @throws IOException �쳣
     */
    public void readFileLine(FileChannel fileChannel, ByteBuffer byteBuff, long fileSize) throws IOException {
        long cntTotal = 0L;
        while ((fileChannel.read(byteBuff)) != -1) {
            byteBuff.flip();
            // ��֤�Ͷ�λ�зֶ�
            int position = calcPosition(decoder.decode(byteBuff));
            if (position != -1 && (position + cntTotal)< fileSize) {
                fileChannel = fileChannel.position(cntTotal + position);
                cntTotal += position;
            }
            byteBuff.clear();

        }
    }

    /**
     * ȡ�ַ������ƥ���λ�ã������ļ����position
     * @param cs {@link CharSequence}
     * @return rtnPosition {-1:δ�ҵ�ƥ��/position:����λ��ֵ}
     */
    private int calcPosition(CharSequence cs) {
        Matcher matcherInst = pattern.matcher(cs);
        MatchResult mr = null;
        while (matcherInst.find()) {
            mr = matcherInst.toMatchResult();
        }
        return mr == null ? -1 : mr.end();
    }

    private static Pattern pattern = Pattern.compile(".*\r?\n");

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        String tmpStr = "ABC";
        Matcher matcherInst = pattern.matcher(tmpStr);
        MatchResult mr = null;
        while (matcherInst.find()) {
            mr = (matcherInst.toMatchResult());
            System.out.println(mr.end());
        }
    }
}

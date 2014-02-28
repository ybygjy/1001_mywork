package org.ybygjy.basic.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����ʵ�ֻ���������ļ����ݲ���
 * <ol>
 * <li>����NIO���ƴ����ļ����Ķ�ȡ����</li>
 * <li>����Java����֧�ִ���ƥ��</li>
 * </ol>
 * @author WangYanCheng
 * @version 2011-11-1
 */
public class FileGrep {
    /** �ļ��ַ����� */
    private CharsetDecoder decoder;
    /** �ļ��� */
    private static Pattern pattern = Pattern.compile(".*\r?\n");

    /**
     * Constructor
     * @param decoder CharsetDecoder
     */
    public FileGrep(CharsetDecoder decoder) {
        this.decoder = decoder;
    }

    /**
     * ȡʵ��
     * @param charsetInst �ַ�������
     * @return fileGrepInst
     */
    public static FileGrep getInst(Charset charsetInst) {
        return new FileGrep(charsetInst.newDecoder());
    }

    /**
     * �ļ�����ƥ�����
     * @param pattern ƥ�䴮
     * @param fileInst �ļ�����
     * @return rtnStrArr/null �ļ�����ƥ����
     * @throws IOException �쳣��Ϣ
     */
    public String[] findResult4Regexp(final String pattern, File fileInst, final int groupIndex)
        throws IOException {
        final Pattern tmpPattern = Pattern.compile(pattern);
        final List<String> tmpArr = new ArrayList<String>();
        readFile(fileInst, new InnerCallback() {
            public void callBack(CharBuffer charBuff) {
                Matcher matcherInst = FileGrep.pattern.matcher(charBuff);
                while (matcherInst.find()) {
                    String tmpStrArr = matchesResult4Single(matcherInst.group(), tmpPattern, groupIndex);
                    if (null != tmpStrArr) {
                        tmpArr.add(tmpStrArr);
                    }
                }
            }
        });
        if (tmpArr.size() == 0) {
            return null;
        }
        String[] tmpStrArr = new String[tmpArr.size()];
        return tmpArr.toArray(tmpStrArr);
    }

    /**
     * ����ȡ�ļ�����
     * @param fileInst �ļ�
     * @return rtnArr/null
     * @throws IOException IOException
     */
    public String[] readFileContent(File fileInst) throws IOException {
        FileInputStream fins = null;
        try {
            fins = new FileInputStream(fileInst);
            FileChannel fileCInst = fins.getChannel();
            MappedByteBuffer mbbInst = fileCInst.map(MapMode.READ_ONLY, 0, fileInst.length());
            return (decoder.decode(mbbInst).toString().split("\\r\\n"));
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (null != fins) {
                fins.close();
            }
        }
    }

    /**
     * ����ƥ�䷵�ص�һ��ƥ����
     * <p>
     * ʾ��:
     * <pre>
     * Matcher matcherInst = Pattern.compile("@author\s+((\w+\.?)+\w+)\r?\n?").matcher("@author org.ybygjy.FileGrep");
     * </pre>
     * </p>
     * <p>
     * ע��ƥ������ǲ����������ǲ��������ƥ���������ʽ���
     * <ol>
     * <li>0,��ʾȡ��ǰƥ�䴮</li>
     * <li>1,��ʾȡ��ǰƥ�䴮�ĵ�һ����</li>
     * <li>2,��ʾȡ��ǰƥ�䴮�ĵڶ�����</li>
     * <li>3...��������</li>
     * </ol>
     * </p>
     * @param charBuff �ַ�����
     * @param pattern ƥ��ģʽ
     * @param groupIndex ��ƥ��ɹ�ʱ���ص�����ģʽƥ�������
     * @return rtnArr/null
     */
    private String matchesResult4Single(CharSequence charBuff, Pattern pattern, int groupIndex) {
        Matcher matcherInst = pattern.matcher(charBuff);
        if (matcherInst.find()) {
            return matcherInst.group(groupIndex);
        }
        return null;
    }

    /**
     * ���ļ�����
     * @param fileInst
     * @throws IOException
     */
    private void readFile(File fileInst, InnerCallback innerCallback) throws IOException {
        FileInputStream fins = new FileInputStream(fileInst);
        FileChannel fileChannel = fins.getChannel();
        MappedByteBuffer mbbInst = fileChannel.map(MapMode.READ_ONLY, 0, fileInst.length());
        innerCallback.callBack(decoder.decode(mbbInst));
        fileChannel.close();
    }

    /**
     * ����ص�����
     * @author WangYanCheng
     * @version 2011-11-1
     */
    interface InnerCallback {
        /**
         * �ص�����
         * @param charBuff �ַ����ж���
         */
        void callBack(CharBuffer charBuff);
    }

    /**
     * �������
     * @param args
     */
    public static void main(String[] args) {
        //String filePath = "D:\\work\\workspace\\mywork\\src\\org\\ybygjy\\basic\\file\\OpenFile4OS.java";
        String filePath = "J:\\office\\cn_office_professional_plus_2010_x86_515.exe";
        FileGrep fgInst = FileGrep.getInst(Charset.forName("ISO-8859-1"));
        try {
            String[] tmpArr = fgInst.readFileContent(new File(filePath));
            for (String str : tmpArr) {
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

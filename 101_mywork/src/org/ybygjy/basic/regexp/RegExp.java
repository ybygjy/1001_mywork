package org.ybygjy.basic.regexp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������ʽ
 * @author WangYanCheng
 * @version 2010-1-26
 */
public class RegExp {
    /**
     * ƥ���ļ���������
     * @param srcStr srcStr
     */
    public void doMatchFileContent() {
        String[] tmpStr = {"@depJs org.ybygjy\n@depJs org.ybygjy.j2ee\n@depJs org.ybygjy.j2se"};
        Pattern nsPattInst = Pattern.compile("@depJs\\s+((\\w+\\.*)+\\w+)\\r?\\n?");
        for (String srcStr : tmpStr) {
            Matcher matcherInst = nsPattInst.matcher(srcStr);
            while (matcherInst.find()) {
                System.out.println(matcherInst.group(1));
            }
        }
    }

    /**
     * ��ȡ���ƿռ�
     */
    public void doMatchNamespace() {
        String[] nsArr = new String[]{"Nstc.ibs.InnerQryRightAccountCombo", "Nstc.ibs.ChannelCombo", "Nstc.ibs.CustomerCombo", "Nstc.ibs.NetBankOrderDetailedConditionsPanel"};
        Pattern nsPattInst = Pattern.compile("(\\w+.*)\\.\\w+$");
        for (String tmpStr : nsArr) {
            System.out.print("��ȡ���ƿռ䣺".concat(tmpStr));
            Matcher matcherInst = nsPattInst.matcher(tmpStr);
            if (matcherInst.find()) {
                System.out.print("\tԭʼ��{" + tmpStr + "}���ƿռ�{" + matcherInst.group(1) + "}\n");
            }
        }
    }
    /**
     * ƥ���ļ���ַ
     * @param srcPath srcPath
     */
    public void doMatchFilePath(String srcPath) {
        String pattern = "(\\w+.*\\\\)+(\\w+[.]{1}\\w+)+";
        Matcher matcher = Pattern.compile(pattern).matcher(srcPath);
        int groupCount = matcher.groupCount();
        while (matcher.find()) {
            for (int index = 0; index <= groupCount; index++) {
                System.out.println(index + ":" + matcher.group(index));
            }
        }
    }
    /**
     * ���˵��۵��ʼ���ַ
     * @param emailStr �ʼ���ַ
     */
    public void doMatchEmail(String emailStr) {
        String regExp = "^(?=[^.])(?:\\w+[.]?)+@(([\\[](\\d+[.]?)+[\\]])|((\\w+[.]?)+[^.]){2,254})$";
        Matcher matcher = Pattern.compile(regExp).matcher(emailStr);
        if (matcher.matches()) {
            int groupCtn = matcher.groupCount();
            System.out.println(matcher.pattern().pattern() + "\t ƥ�䣺" + emailStr);
            for (int i = 0; i < groupCtn; i++) {
                System.out.println("��" + i + "�飺" + matcher.group(i));
            }
        }
    }
    /**
     * �ļ�����ƥ��
     * @param fileName fileName
     */
    public void doMatchFileName(String fileName) {
        System.out.println("�����ļ���չ��:".concat(fileName.replaceAll("[.]{1}.*", "")));
        System.out.println("�����ļ�����,ֻȡ��չ��:".concat(fileName.replaceAll(".*[.]{1}", ".")));
    }

    /**
     * includeChinese
     * @param str str
     * @return rtnFlag true/false
     */
    public boolean includeChinese(String str) {
        String pattern = ".*[\u4e00-\u9fa5]+.*";
        boolean rtnFlag = false;
        if (str.matches(pattern)) {
            rtnFlag = true;
        }
        return rtnFlag;
    }

    /**
     * isDigit Number
     * @param str str
     * @return rtnFlag true/false
     */
    public boolean isDigitNumber(String str) {
        String pattern = "^\\d+.?\\d+$";
        return str.matches(pattern);
    }

    /**
     * isSignDigit Number
     * @param str str
     * @return rtnFlag true/false
     */
    public boolean isSignDigitNumber(String str) {
        String pattern = "^[+-]?\\d+.?\\d+$";
        return str.matches(pattern);
    }

    /**
     * ����ƥ�������
     * @param pattInst ģʽ
     * @param srcStr Դƥ�䴮
     */
    public void regGroupTest(Pattern pattInst, String srcStr) {
        Matcher maInst = pattInst.matcher(srcStr);
        while (maInst.find()) {
            System.out.println(maInst.group());
            int groupCount = maInst.groupCount();
            for (int index = 1; index <= groupCount; index++) {
                System.out.println("Group:" + index + ";\tGroupContent:" + maInst.group(index));
            }
        }
    }

    /**
     * ����HTML�ַ�����ȡ�������ݣ���ʽ��<br>
     * &lt;div class="className"&gt;
     * @throws IOException IOException
     */
    public void doAnalyseHTML() throws IOException {
        String srcStr = doLoadFile();
        //String regStr = "(<div\\sclass=\"drag\"[^>]+>[^<>]*(((?open<div[^>]*>)[^<>]*)+((?-open</div>)[^<>]*)+)*(?(open)(?!))</div>)+";
        String regStr = "<div\\sclass=\"drag\"[^>]*>(<div[^>]*>(.*)</div>|.)*?</div>";
        Matcher matcher = Pattern.compile(regStr, Pattern.DOTALL).matcher(srcStr);
        System.out.println("isMatcher:" + matcher.matches() + ":" + matcher.groupCount());
        while (matcher.find()) {
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++) {
                System.out.println(i + ":" + matcher.group(i));
            }
        }
    }
    /**
     * ȡhtml��inputԪ�ص�ֵ����Ԫ�����ͷ�hidden
     * <p>1��Ŀǰ��֧��Ԫ��ֵ�а���'�ַ�����value='ab'c'</p>
     */
    public void doFetchInputValue() {
        String srcHTML = "<td><input type='text' value='12<3'/><input type='hidden' value='321'/><input type='text' value='456'/></td>";
        String regExpStr = "(<input\\s+)(type='[^h]+'\\s+)(value='([^']+)+')[^<]+";
        Matcher matcher = Pattern.compile(regExpStr).matcher(srcHTML);
        while (matcher.find()) {
            System.out.println("Դ�ַ���\t".concat(srcHTML));
            int count = matcher.groupCount();
            for (int i = 0; i <= count; i++) {
                System.out.println("��@T��ƥ��\t".replaceFirst("@T", "" + i).concat(matcher.group(i)));
            }
            System.out.println();
        }
    }
    /**
     * doLoadFile
     * @return fileContent fileContent
     * @throws IOException IOException
     */
    public String doLoadFile() throws IOException {
        InputStream ins = this.getClass().getResourceAsStream("htmlFile");
        String rtnStr = "";
        byte[] buff = new byte[1024];
        int count = -1;
        while ((count = ins.read(buff, 0, buff.length - 1)) != -1) {
            rtnStr += new String(buff, 0, count);
        }
        return rtnStr;
    }
    /**
     * ����Sql����
     * @param sql SQL
     */
    public void doAnalyseSql(String sql) {
        String andRegex = "([A|a][N|n][D|d])";
        String ignoreRegex = "(\\([^)]+\\))";
        String[] tmpStrArr = sql.split(andRegex);
        doPrint(tmpStrArr);
        Matcher matcher = Pattern.compile(ignoreRegex).matcher(sql);
        List<String> tmpList = new ArrayList<String>();
        while (matcher.find()) {
            tmpList.add(matcher.group(0));
        }
System.out.println(sql);
        sql = sql.replaceAll(ignoreRegex, "");
System.out.println(sql);
        sql = sql.replaceAll(andRegex, "");
System.out.println(sql);
    }
    /**
     * ȡ�ַ����е�\t��\r�ȱ��
     * @param str str
     */
    public void doAnalyseFlag(String str) {
        //��ͨ{��Ѱƥ��Ԫ��}
        String reg1 = "(\r|\b|\n|\t)";
        Matcher matcher = Pattern.compile(reg1).matcher(str);
        while (matcher.find()) {
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++) {
                str = matcher.replaceAll("\\\\" + matcher.group(i));
            }
        }
        System.out.println(str);
    }
    /**
     * ʹ�û��Ӳ���λ��
     * @param str str
     */
    public void doAnalyseFlag2(String str) {
        //����(��ѰԪ��λ��)
        String reg2 = "((?=\r))";
        str = Pattern.compile(reg2).matcher(str).replaceAll("����λ��");
        System.out.println(str);
    }
    /**
     * doPrint
     * @param tmpStrArr tmpStrArr
     */
    public void doPrint(String[] tmpStrArr) {
        for (String str : tmpStrArr) {
            System.out.println(str);
        }
    }
    /**
     * �������
     * @param args ����
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {
//        String str = "abc\rsdfg\ndef\bsdfg\t";
//        RegExp regExpInst = new RegExp();
//        regExpInst.doMatchNamespace();
//        regExpInst.doMatchFileContent();
//        regExpInst.doFetchInputValue();
//        regExpInst.doAnalyseFlag(str);
//        regExpInst.doAnalyseFlag2(str);
        // System.out.println(regExpInst.includeChinese("����"));
        // System.out.println("isNumber:".concat(String.valueOf(regExpInst.isDigitNumber("0.99"))));
        // System.out.println("isSignNumber:".concat(String.valueOf(regExpInst.isSignDigitNumber("-0.99"))));
        // System.out.println(Float.parseFloat("222"));
        // System.out.println("".equals(null));
        // regExpInst.doMatchFilePath("C:\\Program Files\\Adobe\\Flash.exe");
        // regExpInst.doMatchFileName("HelloWorld.txt");
        // regExpInst.regGroupTest(Pattern.compile("([A-Z]+)+(\\d+)+$"),
        // "D00101");
//         regExpInst.doAnalyseHTML();
//        regExpInst.doAnalyseHTML2();
//        String sql = "SELECT * FROM SY_TABLE_DEF WHERE 1=1 and 2=2 And 3=3 AND 4=4 AND (1=1 OR 2=2) OR (1=1 OR 1<2)";
//        regExpInst.doAnalyseSql(sql);
//        regExpInst.doMatchEmail("abc.abc.abc@abc.com");
//        regExpInst.doMatchEmail("abc.abc.abc@[172.16.0.15]");
    }
}

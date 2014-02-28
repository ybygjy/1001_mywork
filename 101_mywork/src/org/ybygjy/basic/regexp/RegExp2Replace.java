package org.ybygjy.basic.regexp;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �����ַ������滻��Ӧ����
 * @author WangYanCheng
 * @version 2010-4-25
 */
public class RegExp2Replace {
    /**
     * �����ļ���<br>
     * <pre><i>����</i><br>
     * String fileName = "HelloWorld.txt";
     * <li>������������κδ���</li>
     * String fileName = "HelloWorld";
     * <li>���������Ҫ���.txt</li>
     * </pre>
     */
    public void replaceFileName() {
        String fileName = "HelloWorld";
        fileName = fileName.matches("[.]{1}\\w+$") ? fileName : fileName.concat(".txt");
        System.out.println(fileName);
    }
    /**
     * LeopardAnalyse Replace used regular expression
     */
    public void doBasicReplace() {
        String s = "ja;d110jfa;<<123>>dafadj;lf<<456>><<789>>adljfa;";
        Map<String, String> replaceMap = new HashMap<String, String>() {
            { // ��Ĺ���
                put("123", "[china]");
                put("456", "[japan]");
                put("789", "[american]");
            }
        };
        Matcher m = Pattern.compile("<<(.+?)>>").matcher(s);
        while (m.find()) {
            if (replaceMap.containsKey(m.group(1))) {
                s = s.replace(m.group(), replaceMap.get(m.group(1)));
            } else {
                s = s.replace(m.group(), "");
            }
        }
        System.out.println(s);
    }
    /**
     * �������
     * @param args arguments
     */
    public static void main(String[] args) {
        new RegExp2Replace().replaceFileName();
    }
}

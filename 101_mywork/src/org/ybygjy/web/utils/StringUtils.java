package org.ybygjy.web.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���ַ����Ĺ�������
 * @author WangYanCheng
 * @version 2010-8-25
 */
public class StringUtils {
    /**singleton*/
    private static StringUtils suInst = new StringUtils();
    /**
     * singleton pattern
     */
    private StringUtils() {
    }
    /**
     * ȡ���ַ�������ʵ��
     * @return strUtil �ַ�����������ʵ��
     */
    public static final StringUtils getInstance() {
        return suInst;
    }
    /**
     * �����ַ���ռλ���ķ����滻
     * @param targetStr Դ�ַ���
     * @param paramArray �滻�����趨,��{{"@STR@", "�滻_1"}, {"@STR2@", "�滻_2"}}
     * @return rtnStr �滻��ɵ��ַ���
     */
    public String doReplace(String targetStr, Map<String, String> paramArray) {
        Pattern pattern = Pattern.compile("(@(\\w+)@)", Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(targetStr);
        String rtnStr = targetStr;
        String key = null;
        String value = null;
        while (matcher.find()) {
            key = matcher.group(2);
            value = paramArray.get(key);
            rtnStr = rtnStr.replace(matcher.group(1), value);
        }
        return rtnStr;
    }
}

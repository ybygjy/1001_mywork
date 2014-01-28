package org.ybygjy.sql.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ybygjy.meta.model.FieldMeta;
import org.ybygjy.meta.model.FieldType;
import org.ybygjy.meta.model.TableMeta;

/**
 * ��������SQL
 * @author WangYanCheng
 * @version 2012-4-10
 */
public class AnalyseSql {
    /**��ʵ��*/
    private TableMeta tableMeta;
    /**���ֶμ�*/
    private String[] columns;
    /**���ֶζ���*/
    private FieldMeta[] fieldMetaes;
    /**
     * ���캯��
     * @param tableMeta {@link TableMeta}
     */
    public AnalyseSql(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
        //ȡ�ֶζ���
        List<FieldMeta> fields = this.tableMeta.getFieldList();
        //�����ֶζ���ռ�
        fieldMetaes = fields.toArray(new FieldMeta[fields.size()]);
        int flag = 0;
        columns = new String[fields.size()];
        for (FieldMeta fmInst : fields) {
            columns[flag++] = fmInst.getFieldCode();
        }
    }

    /**
     * ����������
     * @return rtnSQL
     */
    public String analyseInsertSql() {
        StringBuffer tmpCol = new StringBuffer(), tmpPlaceholder = new StringBuffer();
        for (FieldMeta fieldMeta : fieldMetaes) {
            tmpCol.append(KeyWordMapp.mappingKeyWord(fieldMeta.getFieldCode())).append(",");
            tmpPlaceholder.append(KeyWordMapp.mappingTypeDefValue(fieldMeta.getFieldType(), "?").concat(","));
        }
        tmpCol.setLength(tmpCol.length() - 1);
        tmpPlaceholder.setLength(tmpPlaceholder.length() - 1);
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("INSERT INTO ").append(tableMeta.getTargetTableName()).append(" (")
            .append(tmpCol.toString()).append(") VALUES(").append(tmpPlaceholder.toString())
            .append(")");
        return sbuf.toString();
    }

    /**
     * �����ѯSQL
     * @return rtnSQL
     */
    public String analyseQuerySql() {
        StringBuffer rtnStr = new StringBuffer();
        rtnStr.append("SELECT ");
        for (FieldMeta fieldMeta : fieldMetaes) {
            if (fieldMeta.isSpecialType()) {
                continue;
            }
            rtnStr.append(KeyWordMapp.mappingKeyWord(fieldMeta.getFieldCode())).append(",");
        }
        rtnStr.setLength(rtnStr.length() - 1);
        rtnStr.append(" FROM ").append(tableMeta.getSrcTableName());
        return rtnStr.toString();
    }
    
    public FieldMeta[] getFieldMetaArr() {
        return this.fieldMetaes;
    }
    /**
     * �ؼ���ӳ��
     * @author WangYanCheng
     * @version 2012-4-24
     */
    static class KeyWordMapp {
        /**�ؼ���ӳ�伯*/
        private static Map<String, String> keyWordMapps = new HashMap<String, String>();
        /**��������ӳ�伯*/
        private static Map<FieldType, String> typeDefVMapp = new HashMap<FieldType, String>();
        static {
            keyWordMapps.put("ROW", "\"ROW\"");
            keyWordMapps.put("Row", "\"Row\"");
            keyWordMapps.put("LEVEL", "\"LEVEL\"");
        }
        static {
            typeDefVMapp.put(FieldType.CLOB, "EMPTY_CLOB()");
            typeDefVMapp.put(FieldType.NCLOB, "EMPTY_CLOB()");
        }
        /**
         * ƥ������ַ��Ĺؼ���
         * @param keyWorld �����ַ�
         * @return rtnKey �����ַ�/�ؼ���ӳ��
         */
        public static String mappingKeyWord(String keyWorld) {
            return keyWordMapps.containsKey(keyWorld) ? keyWordMapps.get(keyWorld) : keyWorld;
        }
        /**
         * ƥ���ض��ֶ����Ͷ�Ӧ��Ĭ��ֵ
         * @param key ����
         * @param defV Ĭ��ֵ
         * @return rtnV/defV
         */
        public static String mappingTypeDefValue(FieldType key, String defV) {
            return typeDefVMapp.containsKey(key) ? typeDefVMapp.get(key) : defV;
        }
    }
}
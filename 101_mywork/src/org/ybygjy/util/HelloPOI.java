package org.ybygjy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Hello POI World
 * @author WangYanCheng
 * @version 2013-1-11
 */
public class HelloPOI {
    private static int ROWNUM = 10;
    private static int COLNUM = 10;

    /**
     * �����߼�
     */
    public void doWork() {
        FileOutputStream fous = null;
        try {
            fous = createFileOutputStream("HelloPOI.xls");
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            HSSFRow r = null;
            HSSFCell c = null;
            wb.setSheetName(0, "HelloPOI");
            for (int rowNum = 0; rowNum < ROWNUM; rowNum++) {
                r = sheet.createRow(rowNum);
                for (int colNum = 0; colNum < COLNUM; colNum++) {
                    c = r.createCell(colNum);
                    c.setCellValue("[".concat(String.valueOf(rowNum)).concat(",").concat(String.valueOf(colNum)).concat("]"));
                    //width>>(256*10)/256=10>>����ʵֵΪ9.29
                    sheet.setColumnWidth(colNum, 256*10);
                }
            }
            wb.write(fous);
            fous.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fous != null) {
                try {
                    fous.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ���������
     * @param fileName �ļ�����
     * @return fous �ļ������
     */
    public FileOutputStream createFileOutputStream(String fileName) {
        FileOutputStream fous =null;
        File outputFile = new File(this.getClass().getResource(".").getFile(),fileName);
        System.out.println("�ļ�·��>>".concat(outputFile.getAbsolutePath()));
        try {
            fous = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fous;
    }

    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        new HelloPOI().doWork();
    }
}

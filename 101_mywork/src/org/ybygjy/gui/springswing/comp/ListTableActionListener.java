package org.ybygjy.gui.springswing.comp;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTable;
/**
 * ����ά��Table���¼�������ϵ
 * @author WangYanCheng
 * @version 2011-2-16
 */
public abstract class ListTableActionListener implements ActionListener {
    /**table���*/
    protected JTable table;
    /**table������ݴ洢*/
    protected List list;
    /**
     * setter table
     * @param table tableComponent
     */
    public void setTable(JTable table) {
        this.table = table;
    }
    /**
     * setter list
     * @param list list
     */
    public void setList(List list) {
        this.list = list;
    }
}

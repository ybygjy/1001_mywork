package org.ybygjy.pattern.command.mrsluo;
/**
 * ����ģʽ�������
 * @author WangYanCheng
 * @version 2011-8-1
 */
public class TestCommand {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        System.out.println("================����ͼ��================");
        Chart chart = new Chart();
        System.out.println("ִ����������");
        ICommand cmd = CommandFactory.getCommandInst("line");
        cmd.setObject("ֱ��:(3,3,8,10)");
        chart.execCmd(cmd);
        
        cmd = CommandFactory.getCommandInst("ellipse");
        cmd.setObject("��Բ:(10,20,8,10)");
        chart.execCmd(cmd);
        
        cmd = CommandFactory.getCommandInst("rect");
        cmd.setObject("����:(50,50,10,20)");
        chart.execCmd(cmd);
        
        System.out.println("======================�ع���������======================");
        chart.undo();
        chart.undo();
        System.out.println("======================������������======================");
        chart.redo();
        chart.redo();
    }
}

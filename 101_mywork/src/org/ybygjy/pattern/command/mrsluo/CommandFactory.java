package org.ybygjy.pattern.command.mrsluo;
/**
 * ���������
 * @author WangYanCheng
 * @version 2011-8-1
 */
public class CommandFactory {
    /**
     * ��ȡ����ʵ��
     * @param commType ��������
     * @return ����ʵ��
     */
    public static ICommand getCommandInst(String commType) {
        if ("ellipse".equals(commType)) {
            return new DrawEllipseCommand();
        } else if("line".equals(commType)) {
            return new DrawLineCommand();
        } else if ("rect".equals(commType)) {
            return new DrawRectCommand();
        } else {
            return null;
        }
    }
}

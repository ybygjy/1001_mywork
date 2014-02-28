package org.ybygjy.pattern.visitor;

import org.ybygjy.pattern.visitor.receiver.Pc;
import org.ybygjy.pattern.visitor.visitor.InventoryVisitor;
import org.ybygjy.pattern.visitor.visitor.PriceVisitor;

/**
 * �ͻ���
 * @author WangYanCheng
 * @version 2013-2-6
 */
public class Client {
    private PriceVisitor priceVisitor;
    private InventoryVisitor inventoryVisitor;
    private Equipment equipment;
    public Client() {
        //�豸
        equipment = new Pc();
        //�۸�Visitor
        priceVisitor = new PriceVisitor();
        //���Visitor
        inventoryVisitor = new InventoryVisitor();
    }
    public void doWork() {
        //�豸�ӿ�
        equipment.accept(priceVisitor);
        //�豸�ӿ�
        equipment.accept(inventoryVisitor);
        //ͳ�ƽ��
        System.out.println("Price: ".concat(String.valueOf(priceVisitor.value())));
        //ͳ�ƿ��
        System.out.println("Number of parts: ".concat(String.valueOf(inventoryVisitor.getSize())));
    }
    public static void main(String[] args) {
        new Client().doWork();
    }
}

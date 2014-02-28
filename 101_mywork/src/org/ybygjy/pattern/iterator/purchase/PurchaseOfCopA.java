package org.ybygjy.pattern.iterator.purchase;

import org.ybygjy.pattern.iterator.Iterator;
import org.ybygjy.pattern.iterator.Purchase;
import org.ybygjy.pattern.iterator.iter.ForwardIterator;

/**
 * ����A
 * @author WangYanCheng
 * @version 2012-11-26
 */
public class PurchaseOfCopA extends Purchase {
    /**
     * ���췽��
     */
    public PurchaseOfCopA() {
        append("Dish Washer");
        append("Hair Dresser");
        append("Microwave");
    }
    @Override
    public Iterator createIterator() {
        return new ForwardIterator(this);
    }
    
}

package org.ybygjy.basic.thinking.thread.atomic;

/**
 * �������к�����
 * @author WangYanCheng
 * @version 2010-9-30
 */
public class SerialNumberGenerator {
    /** serialNumber */
    private static volatile int serialNumber;

    /**
     * getter next serialNumber
     * @return serialNumber serialNumber
     */
    public static int nextSerialNumber() {
        return serialNumber++;
    }
}

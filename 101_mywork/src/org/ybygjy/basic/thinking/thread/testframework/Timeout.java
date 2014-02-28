package org.ybygjy.basic.thinking.thread.testframework;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ��ʱ��
 * @author WangYanCheng
 * @version 2010-9-30
 */
public class Timeout extends Timer {
    /**
     * Constructor
     * @param delay ��ʱ
     * @param msg ��Ϣ
     */
    public Timeout(int delay, final String msg) {
        super(true); // daemon thread
        schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(msg);
                System.exit(0);
            }
        }, delay);
    }
}

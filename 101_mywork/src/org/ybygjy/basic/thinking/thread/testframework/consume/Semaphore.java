package org.ybygjy.basic.thinking.thread.testframework.consume;

import org.ybygjy.basic.thinking.thread.testframework.Invariant;
import org.ybygjy.basic.thinking.thread.testframework.InvariantWatcher;
import org.ybygjy.basic.thinking.thread.testframework.state.InvariantFailure;
import org.ybygjy.basic.thinking.thread.testframework.state.InvariantOK;
import org.ybygjy.basic.thinking.thread.testframework.state.InvariantState;

/**
 * �߳�_�ź��� <li>����̹߳���SemaphoreInstance�������´���ģ���˶��̲߳����ź����������ݲ�һ�������</li>
 * ���߳��ź����Ĳ�һ�½�������ж��� <li>����ԭ�Ӳ�������</li> <li>����������</li>
 * @author WangYanCheng
 * @version 2010-9-30
 */
public class Semaphore {
    /**
     * SemaphoreTester
     * @author WangYanCheng
     * @version 2010-9-30
     */
    private class SemaphoreTester extends Thread {
        /** semaphore */
        private volatile SemaphoreInstance semaphore;

        /**
         * Constructor
         * @param semaphore semaphore
         */
        public SemaphoreTester(SemaphoreInstance semaphore) {
            this.semaphore = semaphore;
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            while (true) {
                if (semaphore.available()) {
                    yield();
                    semaphore.acquire();
                    yield();
                    semaphore.release();
                    yield();
                }
            }
        }
    }

    /**
     * Semaphore
     * @author WangYanCheng
     * @version 2010-9-30
     */
    private class SemaphoreInstance implements Invariant {
        /** semaphore */
        private volatile int semaphore = 0;

        /**
         * is available
         * @return true/false
         */
        public boolean available() {
            return semaphore == 0;
        }

        /**
         * self increment semaphore
         */
        public void acquire() {
            ++semaphore;
        }

        /**
         * decrease
         */
        public void release() {
            --semaphore;
        }

        /**
         * {@inheritDoc}
         */
        public InvariantState invariant() {
            int val = semaphore;
            // System.out.println(Thread.currentThread() + "-->" + val);
            if (val == 0 || val == 1) {
                return new InvariantOK();
            } else {
                return new InvariantFailure(val);
            }
        }
    }

    /**
     * ʧ�ܵİ���֮���������Ƶ��ź���
     * @author WangYanCheng
     * @version 2010-10-8
     */
    private class SyncSemaphoreInstance extends SemaphoreInstance {

        @Override
        public synchronized void acquire() {
            super.acquire();
        }

        @Override
        public synchronized boolean available() {
            return super.available();
        }

        @Override
        public synchronized void release() {
            super.release();
        }
    }

    /**
     * getSemaphoreInstance
     * @return {@link SemaphoreInstance}
     */
    public SemaphoreInstance getSemaphoreInst() {
        return new SemaphoreInstance();
    }

    /**
     * getSyncSemaphore
     * @return syncSemaphore syncSemaphore
     */
    public SemaphoreInstance getSyncSemaphoreInst() {
        return new SyncSemaphoreInstance();
    }

    /**
     * getSemaphoreTester
     * @param semaphoreInst {@link SemaphoreInstance}
     * @return {@link SemaphoreTester}
     */
    public SemaphoreTester getSemaphoreTester(SemaphoreInstance semaphoreInst) {
        return new SemaphoreTester(semaphoreInst);
    }

    /**
     * �������
     * @param args �����б�
     * @throws Exception Exception
     */
    public static void main(String[] args) throws Exception {
        // Semaphore semaphore = new Semaphore();
        // SemaphoreInstance semaphoreInst = semaphore.getSemaphoreInst();
        // semaphore.getSemaphoreTester(semaphoreInst);
        // semaphore.getSemaphoreTester(semaphoreInst);
        // new InvariantWatcher(semaphoreInst).join();
        Semaphore semaphore = new Semaphore();
        SemaphoreInstance syncSemaphore = semaphore.getSyncSemaphoreInst();
        semaphore.getSemaphoreTester(syncSemaphore);
        semaphore.getSemaphoreTester(syncSemaphore);
        new InvariantWatcher(syncSemaphore).join();
    }
}

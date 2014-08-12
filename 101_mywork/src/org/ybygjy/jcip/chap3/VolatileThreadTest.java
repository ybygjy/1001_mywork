package org.ybygjy.jcip.chap3;


/**
 * ������һ��volatile���߳���������
 * <p>1.1����д���</p>
 * <p>1.2��û����</p>
 * <p>1.3��volatileȷ�������ĸ��²����������߳̿ɼ�</p>
 * <p>1.4��volatile�������Ỻ���ڼĴ������������������ɼ��ĵط�</p>
 * <p>1.5�����˳�˼��һ�仰(volatile�����Կɼ��Ե�Ӱ���volatile���������Ϊ��Ҫ)</p>
 * ע��
 * <p>2.1�����̶߳������⣬volatileֻ��֤�����ĸ��²�������������ͬ��������¶������߳̿ɼ�</p>
 * @author WangYanCheng
 * @version 2014-7-17
 */
public class VolatileThreadTest {
    public void doWork() {
        final VolatileVO volatileVO = new VolatileVO();
        VolatileWriteThread writeThread = new VolatileWriteThread(volatileVO);
        VolatileReadThread readThread1 = new VolatileReadThread(volatileVO);
        VolatileReadThread readThread2 = new VolatileReadThread(volatileVO);
        writeThread.start();
        readThread1.start();
        readThread2.start();
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        VolatileThreadTest volatileThreadTest = new VolatileThreadTest();
        volatileThreadTest.doWork();
    }
    /**
     * ����{@link VolatileVO} д����
     * @author WangYanCheng
     * @version 2014-7-17
     */
    class VolatileWriteThread extends Thread {
        private VolatileVO volatileVo;
        public VolatileWriteThread(VolatileVO volatileVO) {
            this.volatileVo = volatileVO;
        }
        @Override
        public void run() {
            while (true) {
                int i = volatileVo.getStep() + 1;
                this.volatileVo.setStep(i);
                if (i%500 == 0) {
                    break;
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * ����{@link VolatileVO} ������
     * @author WangYanCheng
     * @version 2014-7-17
     */
    class VolatileReadThread extends Thread {
        private VolatileVO volatileVo;
        public VolatileReadThread(VolatileVO volatileVo) {
            this.volatileVo = volatileVo;
        }

        @Override
        public void run() {
            while (true) {
                String tmpValue = getName() + "#" + volatileVo.getStep();
                System.out.println(tmpValue);
                if (volatileVo.getStep()%500==0) {
                    break;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
/**
 * ��������ʵ��
 * @author WangYanCheng
 * @version 2014-7-17
 */
class VolatileVO {
	/** ʹ��volatileȷ������������ֵ�Ŀɼ���*/
    private volatile int step;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}

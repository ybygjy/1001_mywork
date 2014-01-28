package org.ybygjy.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Java�߳�Barrier����
 * @author WangYanCheng
 * @version 2012-5-7
 */
public class CycleBarrierExample {
    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 200;
        new HorseRace(nHorses, pause);
        System.out.println("Over");
    }
}

class Horse implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private CyclicBarrier barrier;
    /**
     * ���캯����ʼ
     * @param barrier {@link CyclicBarrier}
     */
    public Horse(CyclicBarrier barrier) {
        this.barrier = barrier;
    }
    /**
     * ȡ����
     * @return rtnStrides
     */
    public synchronized int getStrides() {
        return this.strides;
    }
    public String tracks() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }
    public String toString() {
        return "Horse ".concat(String.valueOf(id));
    }
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized(this) {
                    strides += rand.nextInt(3);
                }
                this.barrier.await();
            }
        } catch (InterruptedException e) {
            // A legitimate way to exit
            e.printStackTrace();
        } catch (BrokenBarrierException s) {
            s.printStackTrace();
        }
    }
}
class HorseRace {
    static final int FINISH_LINE = 75;
    private List<Horse> horses = new ArrayList<Horse>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    public HorseRace(int nHorses, final int pause) {
        barrier = new CyclicBarrier(nHorses, new Runnable() {
            public void run() {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < FINISH_LINE; i++) {
                    s.append("=");
                }
                System.out.println(s);
                for (Horse horse : horses) {
                    System.out.println(horse.tracks());
                }
                for (Horse horse : horses) {
                    if (horse.getStrides() >= FINISH_LINE) {
                        System.out.println(horse + " won!");
                        exec.shutdownNow();
                        return;
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                    System.out.println("barrier-action sleep interrupted.");
                }
            }
        });
        for (int i = 0; i < nHorses; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }
}
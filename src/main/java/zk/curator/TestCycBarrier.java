package zk.curator;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by boss on 2019/8/9 0:23
 */
public class TestCycBarrier {
    static class Cys extends Thread {

        CyclicBarrier barrier;

        public Cys(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(getName() + " 到达栅栏 A");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 A");

                Thread.sleep(2000);
                System.out.println(getName() + " 到达栅栏 B");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 B");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("任务已经完成");
        });
        for(int i = 0; i < 10; i++) {
            new Cys(cyclicBarrier).start();
        }
    }


}

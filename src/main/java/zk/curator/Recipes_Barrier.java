package zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Recipes_Barrier {
    static String barrier_path = "/curator_recipes_barrier_paths";
     static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory.builder()
                                .connectString("106.12.12.39:2181")
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
                        client.start();
                        barrier = new DistributedBarrier(client, barrier_path);
                        System.out.println(Thread.currentThread().getName() + "号barrier设置");
                        barrier.setBarrier();
                        barrier.waitOnBarrier();
                        System.err.println("启动...");
                    } catch (Exception e) {
                    }
                }
            }).start();
        }
        Thread.sleep(28000);
        barrier.removeBarrier();
    }
}






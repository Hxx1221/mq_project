package zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by boss on 2019/8/6 21:14
 */
public class Zookeeper_Create_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("106.12.12.39:2181", 5000, new Zookeeper_Create_API_Sync_Usage());
        connectedSemaphore.await();

        final String s = zooKeeper.create("/zk-test", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(s);


        final String ss = zooKeeper.create("/zk-test", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println(ss);
    }

    public void process(final WatchedEvent event) {

        System.out.println("Receive watched event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            System.out.println("Receive " + event);
            connectedSemaphore.countDown();
        }
    }
}

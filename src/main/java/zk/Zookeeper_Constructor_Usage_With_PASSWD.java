package zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Zookeeper_Constructor_Usage_With_PASSWD implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("106.12.12.39:2181", 5000, new Zookeeper_Constructor_Usage_With_PASSWD());
        connectedSemaphore.await();

        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
         zooKeeper = new ZooKeeper("106.12.12.39:2181", 5000,
                 new Zookeeper_Constructor_Usage_With_PASSWD(),1L,"test".getBytes());

        zooKeeper = new ZooKeeper("106.12.12.39:2181", 5000,
                new Zookeeper_Constructor_Usage_With_PASSWD(),sessionId,sessionPasswd);
        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent event) {
        System.out.println("Receive watched event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
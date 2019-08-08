package zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by boss on 2019/8/6 21:32
 */
public class Zookeeper_Create_API_Async_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("106.12.12.39:2181", 5000, new Zookeeper_Create_API_Async_Usage());
        connectedSemaphore.await();

        zooKeeper.create("/zk-tests", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallBack(),"I am");

        zooKeeper.create("/zk-tests", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallBack(),"I am");
        zooKeeper.create("/zk-tests", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallBack(),"I am ");
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void process(final WatchedEvent event) {

        System.out.println("Receive watched event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            System.out.println("Receive " + event);
            connectedSemaphore.countDown();
        }
    }


}
class IStringCallBack implements AsyncCallback.StringCallback{


    public void processResult(final int rc, final String path, final Object ctx, final String name) {
        System.out.println("Create path result : ["+rc+" ,path :"+path +", "+ctx+", name "+name+" ]");
    }
}
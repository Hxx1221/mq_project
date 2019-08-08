package zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import sun.reflect.generics.tree.Tree;

import java.util.TreeMap;

/**
 * Created by boss on 2019/8/8 23:07
 */
public class Create_Session_Sample {
    public static void main(String[] args) throws Exception {


        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("106.12.12.39:2181", 5000, 3000, retryPolicy);
        client.start();
        System.out.println("Zookeeper session1 established. ");
        CuratorFramework client1 = CuratorFrameworkFactory.builder().connectString("106.12.12.39:2181")
                .sessionTimeoutMs(5000).retryPolicy(retryPolicy).namespace("base").build();
        client1.start();
        client1.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("jahkfjh");
        System.out.println("Zookeeper session2 established. ");
        final TreeMap<Object, Object> objectObjectTreeMap = new TreeMap<Object, Object>();
        objectObjectTreeMap.put("","");


    }
}

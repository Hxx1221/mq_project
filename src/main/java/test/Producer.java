package test;

import com.rabbitmq.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String IP_ADDRESS = "106.12.33.235";
    private static final int PORT = 5672;
    private static final String EXCHANGE = "exchange_demo";

    private static final String ROUTINGKEY = "routingkey_demo";

    private static final String QUEUE = "queue_demo";

    private ConnectionFactory connectionFactory = null;
    private Connection connection = null;
    private Channel channel = null;

    @Before
    public void test() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {

        connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:root@" + IP_ADDRESS + ":" + PORT + "");
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        System.out.println("======建立连接======");

    }

    /**
     * @return ${return_type}    返回类型
     * @throws
     * @Author:He_xixiang
     * @Title: ${enclosing_method}
     * @Description: uri进行连接
     * @date: ${DATE}
     */
    @Test
    public void test1() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        channel.exchangeDeclare(EXCHANGE, "direct", true, false, null);
        channel.queueDeclare(QUEUE, true, false, false, null);
        channel.queueBind(QUEUE, EXCHANGE, ROUTINGKEY);
        String bb = "hello world!";

        for (int i = 0; i < 20; i++) {
            channel.basicPublish(EXCHANGE, "ssss", true, MessageProperties.PERSISTENT_TEXT_PLAIN, (bb + i).getBytes());

            channel.addReturnListener(new ReturnListener() {
                public void handleReturn(final int i, final String s, final String s1, final String s2, final AMQP.BasicProperties basicProperties, final byte[] bytes) throws IOException {
                    final String s3 = new String(bytes);
                    System.out.println(s3);

                }
            });

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * @return ${return_type}    返回类型
     * @throws
     * @Author:He_xixiang
     * @Title: ${enclosing_method}
     * @Description: 内置交换机  交换器与交换器进行绑定
     * @date: $DATE
     */
    @Test
    public void exchangeBindExchange() throws IOException {
        channel.exchangeDeclare("source", "direct", false, true, null);
        channel.exchangeDeclare("destination", "fanout", false, true, null);
        channel.exchangeBind("destination", "source", "exKey");
        channel.queueDeclare("queue", false, false, true, null);
        channel.queueBind("queue", "destination", "");
        channel.basicPublish("source", "exKey", new AMQP.BasicProperties().builder().expiration("10000").build(), "hxhxh".getBytes());

    }

    /**
     * @return ${return_type}    返回类型
     * @throws
     * @Author:He_xixiang
     * @Title: ${enclosing_method}
     * @Description: 给队列设置过期时间
     * @date: $DATE
     */
    @Test
    public void ttlQueue() throws IOException {
        final HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("x-message-ttl", 50000);
        channel.exchangeDeclare("ttlExchange", "direct", true, false, null);
        channel.queueDeclare("ttlQueue", true, false, false, stringObjectHashMap);
        channel.queueBind("ttlQueue", "ttlExchange", "normalKey");
        pushMessage("ttlExchange","normalKey","ttl");
        channel.basicPublish("ttlExchange", "normalKey", new AMQP.BasicProperties().builder().expiration("20000").build(), "hxhxh".getBytes());

    }

    /**
     * @return ${return_type}    返回类型
     * @throws
     * @Author:He_xixiang
     * @Title: ${enclosing_method}
     * @Description: 死信队列
     * @date: $DATE
     */
    @Test
    public void ttlDLX() throws IOException {
        final HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("x-message-ttl", 50000);
        final HashMap<String, Object> dxlMap = new HashMap<String, Object>();
        stringObjectHashMap.put("x-dead-letter-exchange", "dxlExchange");
        channel.exchangeDeclare("ttlExchange", "direct", true, false, null);
        channel.exchangeDeclare("dxlExchange", "fanout", true, false, null);
        channel.queueDeclare("ttlQueue", true, false, false, stringObjectHashMap);
        channel.queueBind("ttlQueue", "ttlExchange", "normalKey");
        channel.queueDeclare("dxlQueue", true, false, false, dxlMap);
        channel.queueBind("dxlQueue", "dxlExchange", "normalKey");
        pushMessage("ttlExchange","normalKey","ttl");
        channel.basicPublish("ttlExchange", "ss", new AMQP.BasicProperties().builder().expiration("20000").build(), "hxhxh".getBytes());

    }


    /**
     *  备份交换器
     */
    @Test
    public void alternate_exchange() throws IOException {

        final HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("alternate-exchange", "myAe");
        channel.exchangeDeclare("normalExchange", "direct", true, false, stringObjectHashMap);
        channel.exchangeDeclare("myAe", "fanout", true, false, null);
        channel.queueDeclare("normalQueue", true, false, false, null);
        channel.queueBind("normalQueue", "normalExchange", "normalKey");
        channel.queueDeclare("myAeQueue", true, false, false, null);
        channel.queueBind("myAeQueue", "myAe", "");
        String bb = "hello world!";
        pushMessage("normalExchange","ss",bb);
    }

    public void pushMessage(String exchangeName,String routingKey ,String message) throws IOException {
        for (int i = 0; i < 20; i++) {
            channel.basicPublish(exchangeName, routingKey, true, MessageProperties.PERSISTENT_TEXT_PLAIN, (message + i).getBytes());

            channel.addReturnListener(new ReturnListener() {
                public void handleReturn(final int i, final String s, final String s1, final String s2, final AMQP.BasicProperties basicProperties, final byte[] bytes) throws IOException {
                    final String s3 = new String(bytes);
                    System.out.println(s3);

                }
            });

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    @Test
    public void getQueue() throws IOException {
        final AMQP.Exchange.DeclareOk direct = channel.exchangeDeclare(EXCHANGE, "direct", true);//非持久化 排他的 自动删除的
        System.out.println(direct);
        final String queue = channel.queueDeclare().getQueue();
        System.out.println(queue);

    }

    @After
    public void close() throws IOException, TimeoutException {
        System.out.println("======close======");
        channel.close();
        connection.close();

    }

}

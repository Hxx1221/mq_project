import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author:He_xixiang
 * @Title: ${enclosing_method}
 * @Description: ${todo}(这里用一句话描述这个方法的作用)
 * @return ${return_type}    返回类型
 * @throws
 */
public class produce {

    private static final String a = "exchange_demo";

    private static final String b = "routingkey_demo";

    private static final String c = "queue_demo";

    private static final String IP_ADDRESS = "106.12.33.235";
    private static final int PORT = 5672;


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(a, "direct", true, false, null);
        channel.queueDeclare(c, true, false, false, null);
        channel.queueBind(c, a, b);
        String bb = "hello world";

        for (int i = 0; i < 20; i++) {
            channel.basicPublish(a, b, MessageProperties.PERSISTENT_TEXT_PLAIN, (bb+i).getBytes());
            TimeUnit.MILLISECONDS.sleep(500);
        }
        channel.close();
        connection.close();
    }
}

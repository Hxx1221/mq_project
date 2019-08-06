import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ExchangeDeclare {

    private static final String a = "exchange_demo";

    private static final String b = "routingkey_demo";

    private static final String c = "queue_demo";

    private static final String IP_ADDRESS = "106.12.33.235";
    private static final int PORT = 5672;
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare("exchangeName","direct",true);
/**
 * *          Exchange.DeclareOk exchangeDeclare(String exchange,       交换器
 *                                               String type, 交换器类型 dircet fanout  topic headers
 *                                               boolean durable,     是否设置持久化
 *                                               boolean autoDelete,  是否自动删除
 *                                               boolean internal,    是否内置交换器
 *                                               Map<String, Object> arguments
 *                                               ) throws IOException;
 * */

    }


}
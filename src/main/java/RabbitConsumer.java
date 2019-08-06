import com.rabbitmq.client.*;

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
public class RabbitConsumer {


    private static final String QUEUE_NAME = "queue_name";

    private static final String IP_ADDRESS = "106.12.33.235";
    private static final int PORT = 5672;


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Address[] addresses = new Address[]{
                new Address(IP_ADDRESS, PORT)};
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("root");
        factory.setPassword("root");
        final Connection connection = factory.newConnection(addresses);

        final Channel channel = connection.createChannel();

        channel.basicQos(64);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(final String consumerTag, final Envelope envelope, final AMQP.BasicProperties properties, final byte[] body) throws IOException {
                System.out.println(" recv  message:  " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }

        };
        channel.basicConsume(QUEUE_NAME,true, consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }

}


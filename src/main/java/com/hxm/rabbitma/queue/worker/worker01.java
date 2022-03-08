package com.hxm.rabbitma.queue.worker;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hxmao
 * @date 2022/2/28 14:25
 */
public class worker01 {
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2 消费者启动等待消费......");


        boolean autoAck = false;
        channel.basicConsume(RabbitMqUtils.QUEUE_NAME, autoAck, ((s, delivery) -> {
            String receivedMessage = new String(delivery.getBody());
            System.out.println("接收到消息:"+receivedMessage);

            // 应答
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }), (s -> {
            System.out.println(s+"消费者取消消费接口回调逻辑");

        }));
    }
}

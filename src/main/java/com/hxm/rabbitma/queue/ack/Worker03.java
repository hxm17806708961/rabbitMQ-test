package com.hxm.rabbitma.queue.ack;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.hxm.rabbitma.utils.SleepUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息手动应答，不丢失，放回队列重新消费
 * @author hxmao
 * @date 2022/2/28 17:04
 */
public class Worker03 {

    private final static String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            System.out.println("C2 消费者启动等待消费......");

            Boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, ((consumerTag, message) -> {

                SleepUtils.sleep(1000);

                System.out.println("接收消息 " + new String(message.getBody()));

                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

            }), (consumerTag -> {

                System.out.println(consumerTag + "消费者标记");

            }));
        }
    }
}

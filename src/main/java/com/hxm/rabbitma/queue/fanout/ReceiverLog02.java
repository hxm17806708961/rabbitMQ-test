package com.hxm.rabbitma.queue.fanout;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅 模式
 *
 * 生产者，发送给交换机， 交换机发给队列，队列发给消费者
 *
 * @author hxmao
 * @date 2022/3/9 15:48
 */
@Slf4j
public class ReceiverLog02 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 声明临时队列
        String queue = channel.queueDeclare().getQueue();

        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");

        System.out.println("等待接收消息。。。");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {

            String s = new String(message.getBody());
            System.out.println("ReceiverLog02接收到的消息，" + s);

        };

        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});


    }
}

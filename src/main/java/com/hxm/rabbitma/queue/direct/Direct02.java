package com.hxm.rabbitma.queue.direct;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hxmao
 * @date 2022/3/10 18:27
 */
public class Direct02 {

    private final static String EXCHANGE_NAME = "direct_log";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 声明队列
        channel.queueDeclare("disk", false, false, true, null);

        //
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Direct02 " + new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume("disk", true, deliverCallback, consumerTag -> {});

    }
}

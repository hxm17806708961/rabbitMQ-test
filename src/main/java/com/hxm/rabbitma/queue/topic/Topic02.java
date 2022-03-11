package com.hxm.rabbitma.queue.topic;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 主题模式，
 * @author hxmao
 * @date 2022/3/11 10:38
 */
public class Topic02 {

    public static final String EXCHANGE_NAME = "topic";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 接收消息
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("主题02 "+ new String(message.getBody(), "UTF-8") + " 接收的键" + message.getEnvelope().getRoutingKey());
        };

        // 接收消息
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}

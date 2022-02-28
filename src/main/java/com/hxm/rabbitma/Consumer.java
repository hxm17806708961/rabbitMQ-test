package com.hxm.rabbitma;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 * @author hxmao
 * @date 2022/2/28 14:12
 */
public class Consumer {
    public final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.217.128");
        factory.setUsername("admin");
        factory.setPassword("123");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.basicConsume(
                QUEUE_NAME,
                (s, delivery) -> {
            String s1 = new String(delivery.getBody());
            System.out.println(s1);
        },
                s -> System.out.println("消息消费被中断"));
    }
}

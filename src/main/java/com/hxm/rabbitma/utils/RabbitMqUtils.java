package com.hxm.rabbitma.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hxmao
 * @date 2022/2/28 14:20
 */
public class RabbitMqUtils {

    public final static String QUEUE_NAME = "hello";

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.217.128");
        factory.setUsername("admin");
        factory.setPassword("123");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        return channel;
    }
}

package com.hxm.rabbitma;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * @author hxmao
 * @date 2022/2/28 13:58
 */
public class Producer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.217.128");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();
        String message = "hello word";
        channel.queueDeclare(QUEUE_NAME,false,false, false,null);

        channel.basicPublish("",QUEUE_NAME,null, message.getBytes());

        System.out.println("发送信息");

    }
}

package com.hxm.rabbitma.queue.ack;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author hxmao
 * @date 2022/2/28 16:56
 */
public class Task {

    private final static String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Channel channel = RabbitMqUtils.getChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("生产者 发出 " + message);
        }


    }
}

package com.hxm.rabbitma.queue.direct;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 多重绑定
 * 只让相同的routingkey接收
 * @author hxmao
 * @date 2022/3/11 10:00
 */
public class DirectLog {

    public static final String exchangeName = "direct_log";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String message = scanner.next();

            System.out.println("生产者发出 " + message);
            channel.basicPublish(exchangeName, "error", null, message.getBytes());
        }

    }
}

package com.hxm.rabbitma.queue.dealqueue;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列实战，正常队列
 *
 * 死信队列
 * @author hxmao
 * @date 2022/3/11 11:19
 */
public class Consumer02 {
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        // 绑定队列与交换机
        System.out.println("等待接收消息。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("死信队列 " + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback,consumerTag -> {});
    }

}

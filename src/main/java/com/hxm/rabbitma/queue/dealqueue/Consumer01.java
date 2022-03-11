package com.hxm.rabbitma.queue.dealqueue;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列实战，正常队列
 * @author hxmao
 * @date 2022/3/11 11:19
 */
public class Consumer01 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        // 声明队列
        Map<String, Object> arguments = new HashMap<>();
        // 过期时间  10秒
//        arguments.put("x-message-ttl", 10000);
        // 死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 限制队列的消息数
//        arguments.put("x-max-length", 6);

        // 声明队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");

        // 绑定队列与交换机
        System.out.println("等待接收消息。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            if ("info5".equals(msg)){
                System.out.println("消息拒绝" + msg);
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                System.out.println("正常队列 " + msg);
            }
        };
        // 开启手动应答，手动拒绝
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback,consumerTag -> {});
    }

}

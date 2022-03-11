package com.hxm.rabbitma.queue.dealqueue;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * 死信队列，生产者
 *
 * 生成死信队列的三大来源
 *
 * 信息拒绝，开启手动应答
 * 超时拒绝ttl
 * 信息太多，队列已满
 *
 * @author hxmao
 * @date 2022/3/11 11:41
 */
public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            //设置消息的 TTL 时间
            AMQP.BasicProperties properties = new
                    AMQP.BasicProperties().builder().expiration("10000").build();
            //该信息是用作演示队列个数限制
            for (int i = 1; i <11 ; i++) {
                String message="info"+i;
                channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null,
                        message.getBytes());
                System.out.println("生产者发送消息:"+message);
            }
        }
    }

}

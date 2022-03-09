package com.hxm.rabbitma.queue.ack;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.hxm.rabbitma.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息手动应答，不丢失，放回队列重新消费
 * @author hxmao
 * @date 2022/2/28 17:04
 */
public class Worker04 {

    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2 等待接收消息处理时间较长");
        //消息消费的时候如何处理消息
        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            String message= new String(delivery.getBody());
            SleepUtils.sleep(30);
            System.out.println("接收到消息:"+message);
            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        //采用手动应答
        boolean autoAck=false;
        channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,(consumerTag)->{
            System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        });
    }
}

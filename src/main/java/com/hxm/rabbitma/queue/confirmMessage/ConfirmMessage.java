package com.hxm.rabbitma.queue.confirmMessage;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认模式
 *      单个确认
 *      批量确认
 *      异步批量确认
 *
 * @author hxmao
 * @date 2022/3/9 13:50
 */
public class ConfirmMessage {

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {

        // 1. 单个确认  耗时703ms
//        publishMessageSingle();

        // 2. 批量确认  耗时85ms
//        publishMessageAll();

        // 3. 异步批量确认    耗时32ms
        publishMessageAsync();
    }

    private static void publishMessageAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        /**
         * 开启线程，适用于高并发
         */
        ConcurrentSkipListMap<Long, String> outStandConfirm = new ConcurrentSkipListMap<>();

        long start = System.currentTimeMillis();

        // 使用监听器，监听成功的消息
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            // 删除已经确认的消息
            if (multiple){
                ConcurrentNavigableMap<Long, String> confirmed =
                        outStandConfirm.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outStandConfirm.remove(deliveryTag);
            }

            System.out.println("确认的消息 "+ deliveryTag);
        };

        ConfirmCallback nackCallback = (deliveryTag, multiple)-> {
            // 重新发布
            System.out.println("未确认的消息 "+ deliveryTag);
        };
        channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < 1000; i++) {
            String message = i + "消息";
            channel.basicPublish("", queueName, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发

            outStandConfirm.put(channel.getNextPublishSeqNo(), message);
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + 1000 + "个异步发布确认消息,耗时" + (end - start) +
                "ms");

    }

    private static void publishMessageAll() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();

        long start = System.currentTimeMillis();

        // 批量确认数量 100
        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            if (i%100 == 0){
                channel.waitForConfirms();
            }

        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + 1000 + "个批量确认消息,耗时" + (end - start) +
                "ms");


    }

    public static void publishMessageSingle() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);

        channel.confirmSelect();

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + 1000 + "个单独确认消息,耗时" + (end - start) +
                "ms");
    }

}














package com.hxm.rabbitma.queue.worker;

import com.hxm.rabbitma.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 轮训分发消息
 * @author hxmao
 * @date 2022/2/28 14:29
 */
public class Task01 {

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.queueDeclare(RabbitMqUtils.QUEUE_NAME,false,false,false,null);
            //从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.next();
                channel.basicPublish("",RabbitMqUtils.QUEUE_NAME,null,message.getBytes());
                System.out.println("发送消息完成:"+message);
            }
        }

    }
}

package api;

/**
 * @author derek zhan
 * @date 2023/7/19 10:03:34
 */

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;


@Configuration
public class PushConsumerExample {
    private static final Logger logger = LoggerFactory.getLogger(PushConsumerExample.class);


    @Bean
    public void register() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delay-test-0317", false);
        //设置nameserver
        consumer.setNamesrvAddr("localhost:9876");
        //设置topic,subExpression即设置订阅的tag，*表示所有
        consumer.subscribe("delay-topic", "*");
        //从最新的offset拉取
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //注册监听
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            for (MessageExt msg : msgs) {
                logger.info("接收时间：{},msg:{}", LocalDateTime.now(), formatter.format(Long.valueOf(new String(msg.getBody()))));
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        logger.info("Consumer Started");
    }

}

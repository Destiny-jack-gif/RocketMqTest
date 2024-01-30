package api;

/**
 * @author derek zhan
 * @date 2023/7/19 10:03:15
 */

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;


@Component
public class ProducerExample2 {
    private static final Logger logger = LoggerFactory.getLogger(ProducerExample2.class);

    ProducerExample2() {
        new Thread(() -> {
            try {
                this.send();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void send() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("test-producer", false);
        producer.setNamesrvAddr("localhost:9876");

        producer.start();
        while (true) {
            long currentTimeMillis = System.currentTimeMillis();
            Message msg = new Message();
            msg.setTopic("delay-topic");
            msg.setBody((currentTimeMillis + "").getBytes(RemotingHelper.DEFAULT_CHARSET));
            Duration messageDelayTime = Duration.ofSeconds(10);

            long delayTimestamp = currentTimeMillis + messageDelayTime.toMillis();
            // 绝对时间：定时消息
            msg.setDeliverTimeMs(delayTimestamp);
            // 相对时间：延时消息
//            msg.setDelayTimeSec(1000 * 5);
            SendResult sendResult = producer.send(msg, 10000);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            logger.info("发送时间：{}, 延时：{}", LocalDateTime.now(), formatter.format(delayTimestamp));
            Thread.sleep(1000);
        }
    }

}

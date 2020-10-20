package cn.xzxy.lewy.framework.kafka.domain.sender.service;

import cn.xzxy.lewy.framework.kafka.domain.log.model.core.RawMessage;
import cn.xzxy.lewy.framework.kafka.domain.sender.dao.SendLogDAO;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.SenderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author lewy95
 */
@Service
@Slf4j
public class KafkaSender {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    SendLogDAO sendLogDAO;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void send(final MessageSenderLog messageSenderLog) {
        RawMessage message = messageSenderLog.getMessage();
        ProducerRecord producerRecord = new ProducerRecord(messageSenderLog.getMessageMetadata().getTopic(), messageSenderLog.getMessageMetadata().getKey(), message);
        this.kafkaTemplate.send(producerRecord).addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            public void onFailure(Throwable throwable) {
                KafkaSender.log.debug("收到kafka失败回调");
                KafkaSender.this.applicationEventPublisher.publishEvent(SenderEvent.fail(throwable, new MessageSenderLog[]{messageSenderLog}));
            }

            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                KafkaSender.log.debug("收到kafka成功回调");
                RecordMetadata recordMetadata = integerStringSendResult.getRecordMetadata();
                int partition = recordMetadata.partition();
                long offset = recordMetadata.offset();
                KafkaSender.this.applicationEventPublisher.publishEvent(SenderEvent.success(partition, offset, new MessageSenderLog[]{messageSenderLog}));
            }
        });
    }
}

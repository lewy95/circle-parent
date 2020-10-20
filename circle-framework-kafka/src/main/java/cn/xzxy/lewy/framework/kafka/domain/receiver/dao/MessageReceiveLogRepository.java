package cn.xzxy.lewy.framework.kafka.domain.receiver.dao;

import cn.xzxy.lewy.framework.kafka.domain.receiver.model.MessageReceiveLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author lewy95
 */
public interface MessageReceiveLogRepository extends MongoRepository<MessageReceiveLog, String> {
}
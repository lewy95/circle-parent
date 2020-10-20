package cn.xzxy.lewy.framework.kafka.domain.sender.dao;

import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageSenderLog;
import cn.xzxy.lewy.framework.kafka.domain.sender.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author lewy95
 */
public interface MessageSenderLogRepository extends MongoRepository<MessageSenderLog, String> {
    List<MessageSenderLog> findByMessageStatusOrderBySendTimeAsc(MessageStatus var1);
}

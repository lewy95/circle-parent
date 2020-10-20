package cn.xzxy.lewy.framework.kafka.domain.sender.service;

import cn.xzxy.lewy.framework.kafka.domain.log.model.core.RawMessage;
import cn.xzxy.lewy.framework.kafka.feature.common.CommonException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * @author lewy95
 */
@Slf4j
public class AveragePartitioner implements Partitioner {

    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        try {
            if (value instanceof RawMessage) {
                RawMessage message = (RawMessage) value;
                String messageId = message.getMessageId();
                Integer hashCode = messageId.hashCode();
                hashCode = Math.abs(hashCode);
                List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
                int numPartitions = partitions.size();
                Integer partition = hashCode % numPartitions;
                log.debug("消息{}发送到{}分区", messageId, partition);
                return partition;
            } else {
                log.warn("发送的消息体无法转换{},默认发送0分区", RawMessage.class);
                return 0;
            }
        } catch (Exception exception) {
            log.error("消息分配分区失败,{}", JSON.toJSONString(value));
            throw new CommonException(27888, "计算分区失败", exception);
        }
    }

    public void close() {
    }

    public void configure(Map<String, ?> configs) {
    }
}
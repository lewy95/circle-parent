package cn.xzxy.lewy.framework.kafka.domain.log.model.core;

import lombok.Data;

/**
 * @author lewy95
 */
@Data
public class MessageMetadata {

    /**
     * 主题
     */
    String topic;
    /**
     * key
     */
    String key;
    /**
     * 分区
     */
    Integer partition;
    /**
     * 偏移量（序号）
     */
    Long offset;

    public MessageMetadata(String topic, String key) {
        this.topic = topic;
        this.key = key;
    }
}

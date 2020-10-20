package cn.xzxy.lewy.framework.kafka.domain.log.model.core;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.UUID;

/**
 * @author lewy95
 */
@Data
public class RawMessage {

    /**
     * 消息ID
     */
    String messageId;
    /**
     * 业务追踪id
     */
    String traceId;
    /**
     * 消息
     */
    JSONObject value;

    public RawMessage(String traceId, JSONObject value) {
        this.traceId = traceId;
        this.value = value;
        this.messageId = UUID.randomUUID().toString();
    }

    public RawMessage(String messageId, String traceId, JSONObject value) {
        this.messageId = messageId;
        this.traceId = traceId;
        this.value = value;
    }

    public RawMessage() {
    }
}

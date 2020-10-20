package cn.xzxy.lewy.framework.kafka.domain.sender.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author lewy95
 */
@Data
public class MessageVO {

    /** topic */
    private String topic;
    /** title */
    private String key;
    /** trace id */
    private String traceId;
    /** value */
    private JSONObject value;

    public MessageVO(String topic, String key, String traceId, JSONObject value) {
        this.topic = topic;
        this.key = key;
        this.traceId = traceId;
        this.value = value;
    }

    public MessageVO(String topic, String key, JSONObject value) {
        this.topic = topic;
        this.key = key;
        this.value = value;
    }
}

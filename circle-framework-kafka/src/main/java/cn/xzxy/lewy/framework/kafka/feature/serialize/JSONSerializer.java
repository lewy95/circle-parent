package cn.xzxy.lewy.framework.kafka.feature.serialize;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author lewy95
 */
public class JSONSerializer implements Serializer {
    private static final Logger log = LoggerFactory.getLogger(JSONSerializer.class);

    public JSONSerializer() {
    }

    public byte[] serialize(String s, Object o) {
        return JSON.toJSONBytes(o);
    }

    public void configure(Map configs, boolean isKey) {
    }

    public byte[] serialize(String topic, Headers headers, Object data) {
        return JSON.toJSONBytes(data);
    }

    public void close() {
    }
}

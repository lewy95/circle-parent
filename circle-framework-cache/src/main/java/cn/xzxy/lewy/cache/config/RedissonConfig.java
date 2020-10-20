package cn.xzxy.lewy.cache.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author lewy95
 */
@Configuration
@EnableConfigurationProperties(RedissonConfigProperties.class)
@Slf4j
public class RedissonConfig {

    @Autowired
    RedissonConfigProperties redissonConfigProperties;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        Config config = Config.fromYAML(new ClassPathResource(redissonConfigProperties.getConfigPath()).getInputStream());
        config.setCodec(getBaseCodec());
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    BaseCodec getBaseCodec() {
        ParserConfig.getGlobalInstance().addAccept(redissonConfigProperties.getFastJsonAcceptPackage());
        return new BaseCodec() {
            private final Encoder encoder = in -> {
                ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
                try {
                    ByteBufOutputStream os = new ByteBufOutputStream(out);
                    JSON.writeJSONString(os, in, SerializerFeature.WriteClassName);
                    return os.buffer();
                } catch (IOException e) {
                    out.release();
                    throw e;
                } catch (Exception e) {
                    out.release();
                    throw new IOException(e);
                }
            };
            private final Decoder<Object> decoder = (buf, state) ->
                    JSON.parseObject(new ByteBufInputStream(buf), Object.class);

            @Override
            public Decoder<Object> getValueDecoder() {
                return decoder;
            }

            @Override
            public Encoder getValueEncoder() {
                return encoder;
            }
        };
    }
}
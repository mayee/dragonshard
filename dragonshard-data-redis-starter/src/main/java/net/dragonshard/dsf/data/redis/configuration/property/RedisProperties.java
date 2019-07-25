package net.dragonshard.dsf.data.redis.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis配置
 *
 * @author mayee
 * @date 2019-06-28
 *
 * @version v1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "dragonshard.redis")
public class RedisProperties {

    /**
     * 是否开启 true/false
     */
    private boolean enabled = true;

    /**
     * 业务标识前缀
     */
    private String bizPrefix;

}

/*
 *   Copyright 1999-2018 dragonshard.net.
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package net.dragonshard.dsf.data.redis.configuration;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.data.redis.configuration.property.RedisProperties;
import net.dragonshard.dsf.data.redis.serializer.StringRedisSerializer;
import net.dragonshard.dsf.data.redis.service.CacheService;
import net.dragonshard.dsf.data.redis.service.ICacheService;
import net.dragonshard.dsf.data.redis.service.RedisCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

import static net.dragonshard.dsf.data.redis.common.RedisConstants.BIZ_PREFIX_SPLIT;


/**
 * RedisAutoConfiguration
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-28
 **/
@Slf4j
@Configuration
@Import({RedisProperties.class})
@ConditionalOnProperty(prefix = "dragonshard.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;
    @Value("${spring.application.name:}")
    private String applicationName;

    @Bean
    @ConditionalOnMissingBean
    public ICacheService redisCache() {
        return new RedisCacheService();
    }

    @Bean
    @ConditionalOnBean(ICacheService.class)
    public CacheService initCache(ICacheService cacheService) {
        return CacheService.initCache(cacheService);
    }

    /**
     * 验证规则 <br>
     * 默认加载 dragonshard.redis.biz-prefix <br>
     * 如果该参数为空则加载 spring.application.name <br>
     * 如果二者都为空，抛出异常启动终止
     */
    @PostConstruct
    public void validRequiredParams() {
        if (StringUtils.isEmpty(applicationName)) {
            Preconditions.checkArgument(StringUtils.isNotEmpty(redisProperties.getBizPrefix()), "Missing required configuration items: dragonshard.redis.biz-prefix");
        } else {
            if (StringUtils.isEmpty(redisProperties.getBizPrefix())) {
                redisProperties.setBizPrefix(applicationName);
                log.info("The configuration item (dragonshard.redis.biz-prefix) is missing, using the default value > {} (spring.application.name)", applicationName);
            }
        }
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        String prefix = StringUtils.isBlank(redisProperties.getBizPrefix()) ? "" : redisProperties.getBizPrefix() + BIZ_PREFIX_SPLIT;
        log.info("biz-prefix > {}", prefix);

        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer(prefix));
        return template;
    }
}

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

package net.dragonshard.dsf.data.redis.serializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 在序列化之前加入业务属性key
 *
 * @author mayee
 * @date 2019-07-25
 *
 * @version v1.0
 **/
@Slf4j
public class StringRedisSerializer implements RedisSerializer<String> {

    private final Charset charset;

    private final String bizPrefix;

    public StringRedisSerializer(String bizPrefix) {
        this(StandardCharsets.UTF_8, bizPrefix);
    }

    public StringRedisSerializer(Charset charset, String bizPrefix) {

        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
        this.bizPrefix = bizPrefix;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(@Nullable String string) {
        return (string == null ? null : (this.bizPrefix + string).getBytes(charset));
    }

}

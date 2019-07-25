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

package net.dragonshard.dsf.data.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static net.dragonshard.dsf.core.toolkit.StringPool.ASTERISK;

/**
 * 缓存的Redis实现
 *
 * @author mayee
 * @date 2019-06-28
 *
 * @version v1.0
 **/
public class RedisCacheService implements ICacheService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void save(String key, String value, long exp) {
        if (exp > -1) {
            redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    @Override
    public String delete(String key) {
        String value = get(key);
        if (value != null) {
            redisTemplate.opsForValue().getOperations().delete(key);
        }
        return value;
    }


    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void clear(String cachePrefix) {
        Set<String> keys = redisTemplate.keys(cachePrefix + ASTERISK);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

}

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

import static net.dragonshard.dsf.core.toolkit.StringPool.ASTERISK;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * 缓存的Redis实现
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-28
 **/
@Slf4j
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

  @SuppressWarnings("unchecked")
  @Override
  public void delete(String... key) {
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        redisTemplate.delete(key[0]);
      } else {
        redisTemplate.delete(CollectionUtils.arrayToList(key));
      }
    }
  }


  @Override
  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public Boolean hasKey(String key) {
    try {
      return redisTemplate.hasKey(key);
    } catch (Exception e) {
      log.error(key, e);
      return false;
    }
  }

  @Override
  public void clear(String cachePrefix) {
    Set<String> keys = redisTemplate.keys(cachePrefix + ASTERISK);
    if (!CollectionUtils.isEmpty(keys)) {
      redisTemplate.delete(keys);
    }
  }

  @Override
  public Boolean expire(String key, long time) {
    try {
      if (time > 0) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
      }
      return true;
    } catch (Exception e) {
      log.error(key, e);
      return false;
    }
  }

  @Override
  public Long getExpire(String key) {
    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }
}

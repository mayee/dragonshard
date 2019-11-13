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

package net.dragonshard.dsf.limit.local.impl;

import com.google.common.util.concurrent.RateLimiter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.KeyUtil;
import net.dragonshard.dsf.limit.aop.LimitExecutor;
import net.dragonshard.dsf.limit.configuration.property.LimitProperties;
import org.springframework.util.Assert;

/**
 * Guava限流实现类
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-17
 **/
@Slf4j
public class GuavaLocalLimitExecutorImpl implements LimitExecutor {

  private LimitProperties limitProperties;

  public GuavaLocalLimitExecutorImpl(LimitProperties limitProperties) {
    this.limitProperties = limitProperties;
  }

  private volatile Map<String, RateLimiterEntity> rateLimiterEntityMap = new ConcurrentHashMap<String, RateLimiterEntity>();

  @Override
  public boolean tryAccess(String name, String key, int limitPeriod, int limitCount) {
    Assert.hasText(name, "@Limit parameter [ name ] is null or empty");
    Assert.hasText(key, "@Limit parameter [ key ] is null or empty");
    String compositeKey = KeyUtil.getCompositeKey(limitProperties.getPrefix(), name, key);
    return tryAccess(compositeKey, limitPeriod, limitCount);
  }

  @Override
  public boolean tryAccess(String compositeKey, int limitPeriod, int limitCount) {
    Assert.hasText(compositeKey, "@Limit parameter [ composite key ] is null or empty");
    Assert.isTrue(limitPeriod == 1,
      "@Limit parameter [ limitPeriod ] must be 1 second for Guava rate limiter");
    RateLimiterEntity rateLimiterEntity = getRateLimiterEntity(compositeKey, limitCount);
    return rateLimiterEntity.getRateLimiter().tryAcquire();
  }

  private RateLimiterEntity getRateLimiterEntity(String compositeKey, double rate) {
    RateLimiterEntity rateLimiterEntity = rateLimiterEntityMap.get(compositeKey);
    if (rateLimiterEntity == null) {
      RateLimiter newRateLimiter = RateLimiter.create(rate);

      RateLimiterEntity newRateLimiterEntity = new RateLimiterEntity();
      newRateLimiterEntity.setRateLimiter(newRateLimiter);
      newRateLimiterEntity.setRate(rate);

      rateLimiterEntity = rateLimiterEntityMap.putIfAbsent(compositeKey, newRateLimiterEntity);
      if (rateLimiterEntity == null) {
        rateLimiterEntity = newRateLimiterEntity;
      }
    } else {
      if (rateLimiterEntity.getRate() != rate) {
        rateLimiterEntity.getRateLimiter().setRate(rate);
        rateLimiterEntity.setRate(rate);
      }
    }

    return rateLimiterEntity;
  }

  /**
   * 因为 rateLimiter.setRate(permitsPerSecond)会执行一次synchronized 为避免不必要的同步，故通过RateLimiterEntity去封装，做一定的冗余设计
   */
  @Setter
  @Getter
  private class RateLimiterEntity {

    private RateLimiter rateLimiter;
    private double rate;
  }
}

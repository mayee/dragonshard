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

package net.dragonshard.dsf.limit.redis.impl;

import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.limit.aop.LimitDelegate;
import net.dragonshard.dsf.limit.aop.LimitExecutor;
import net.dragonshard.dsf.limit.configuration.property.LimitProperties;
import net.dragonshard.dsf.limit.exception.RequestReachMaxLimitException;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Redis限流实现类
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-17
 **/
@Slf4j
public class RedisLimitDelegateImpl implements LimitDelegate {

  @Autowired
  private LimitExecutor limitExecutor;
  private LimitProperties limitProperties;

  public RedisLimitDelegateImpl(LimitProperties limitProperties) {
    this.limitProperties = limitProperties;
  }

  @Override
  public Object invoke(MethodInvocation invocation, String key, int limitPeriod, int limitCount)
    throws Throwable {
    boolean status = true;
    try {
      status = limitExecutor.tryAccess(key, limitPeriod, limitCount);
    } catch (Exception e) {
      if (limitProperties.getAop().getExceptionIgnore()) {
        log.error("Redis exception occurs while Limit", e);
        return invocation.proceed();
      } else {
        throw e;
      }
    }

    if (status) {
      return invocation.proceed();
    } else {
      throw new RequestReachMaxLimitException();
    }
  }
}

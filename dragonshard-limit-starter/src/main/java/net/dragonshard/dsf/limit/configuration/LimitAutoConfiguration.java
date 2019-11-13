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

package net.dragonshard.dsf.limit.configuration;

import net.dragonshard.dsf.limit.aop.LimitAutoScanProxy;
import net.dragonshard.dsf.limit.aop.LimitDelegate;
import net.dragonshard.dsf.limit.aop.LimitExecutor;
import net.dragonshard.dsf.limit.aop.LimitInterceptor;
import net.dragonshard.dsf.limit.configuration.property.LimitProperties;
import net.dragonshard.dsf.limit.local.condition.LocalLimitCondition;
import net.dragonshard.dsf.limit.local.impl.GuavaLocalLimitExecutorImpl;
import net.dragonshard.dsf.limit.local.impl.LocalLimitDelegateImpl;
import net.dragonshard.dsf.limit.redis.condition.RedisLimitCondition;
import net.dragonshard.dsf.limit.redis.handler.RedisHandler;
import net.dragonshard.dsf.limit.redis.handler.RedisHandlerImpl;
import net.dragonshard.dsf.limit.redis.impl.RedisLimitDelegateImpl;
import net.dragonshard.dsf.limit.redis.impl.RedisLimitExecutorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 启动类
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-17
 **/
@Configuration
@Import({
  LimitProperties.class
})
public class LimitAutoConfiguration {

  private final LimitProperties limitProperties;

  @Autowired
  public LimitAutoConfiguration(LimitProperties limitProperties) {
    this.limitProperties = limitProperties;
  }

  @Bean
  public LimitAutoScanProxy limitAutoScanProxy() {
    return new LimitAutoScanProxy(limitProperties.getAop().getScanPackages());
  }

  @Bean
  public LimitInterceptor limitInterceptor() {
    return new LimitInterceptor();
  }

  @Bean
  @Conditional(LocalLimitCondition.class)
  public LimitDelegate localLimitDelegate() {
    return new LocalLimitDelegateImpl(limitProperties);
  }

  @Bean
  @Conditional(LocalLimitCondition.class)
  public LimitExecutor localLimitExecutor() {
    return new GuavaLocalLimitExecutorImpl(limitProperties);
  }

  @Bean
  @Conditional(RedisLimitCondition.class)
  public LimitDelegate redisLimitDelegate() {
    return new RedisLimitDelegateImpl(limitProperties);
  }

  @Bean
  @Conditional(RedisLimitCondition.class)
  public LimitExecutor redisLimitExecutor() {
    return new RedisLimitExecutorImpl(limitProperties);
  }

  @Bean
  @Conditional(RedisLimitCondition.class)
  @ConditionalOnMissingBean
  public RedisHandler redisHandler() {
    return new RedisHandlerImpl();
  }
}

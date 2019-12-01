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

package net.dragonshard.dsf.tinypng.configuration;

import com.google.common.base.Preconditions;
import com.tinify.Tinify;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.ExceptionUtils;
import net.dragonshard.dsf.tinypng.configuration.property.TinypngProperties;
import net.dragonshard.dsf.tinypng.framework.service.ITinypngService;
import net.dragonshard.dsf.tinypng.framework.service.impl.TinypngServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 启动类
 *
 * @author mayee
 **/
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@Import({
  TinypngProperties.class
})
@AllArgsConstructor
@ConditionalOnProperty(prefix = "dragonshard.tinypng", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TinypngAutoConfiguration {

  private TinypngProperties tinypngConfig;

  @PostConstruct
  public void init() {
    validApiKey();
    initTinify();
  }

  @Bean
  public ITinypngService tinypngService(){
    return new TinypngServiceImpl();
  }

  /**
   * 初始化 api-key
   */
  private void initTinify(){
    Tinify.setKey(tinypngConfig.getApiKey());
    if(StringUtils.isNotBlank(tinypngConfig.getProxy())){
      Tinify.setProxy(tinypngConfig.getProxy());
    }
  }

  /**
   * <code>dragonshard.tinypng.api-key</code> 为必填参数。
   * <p>
   * 如果启用了严格模式（dragonshard.tinypng.strict = true）, 则会立即联网校验api-key是否有效。
   */
  private void validApiKey() {
    Preconditions.checkArgument(StringUtils.isNotBlank(tinypngConfig.getApiKey()),
      "[DSE-TP-1]Missing required configuration items: dragonshard.tinypng.api-key");
    if(tinypngConfig.getStrict()){
      log.info("Strict mode is turned on, will perform [ api-key ] check.");
      Tinify.setKey(tinypngConfig.getApiKey());
      if(Tinify.validate()){
        log.info("Tinify api-key verification passed.");
      }else{
        throw ExceptionUtils.get("[DSE-TP-2]Tinify api-key verification failed...");
      }
    }
  }

}

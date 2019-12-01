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

package net.dragonshard.dsf.upload.local.configuration;

import static net.dragonshard.dsf.upload.local.constant.UploadLocalConstant.COMPRESS_TYPE_TINYPNG;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.ClassUtils;
import net.dragonshard.dsf.core.toolkit.ExceptionUtils;
import net.dragonshard.dsf.upload.local.common.ContextUtil;
import net.dragonshard.dsf.upload.local.configuration.properties.UploadLocalProperties;
import net.dragonshard.dsf.upload.local.framework.service.IUploadLocalService;
import net.dragonshard.dsf.upload.local.framework.service.impl.SampleUploadServiceImpl;
import net.dragonshard.dsf.upload.local.tinypng.condition.TinypngCondition;
import net.dragonshard.dsf.upload.local.tinypng.service.IAsyncCompressService;
import net.dragonshard.dsf.upload.local.tinypng.service.impl.TinypngAsyncCompressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 本地上传自动配置类
 *
 * @author mayee
 **/
@Slf4j
@Configuration
@Import({
  UploadLocalProperties.class
})
@ConditionalOnProperty(prefix = "dragonshard.upload.local", name = "enabled", havingValue = "true", matchIfMissing = true)
public class UploadLocalAutoConfiguration {

  @Value("${dragonshard.tinypng.enabled:}")
  private Boolean tinypngEnabled;

  @Autowired
  private UploadLocalProperties uploadLocalConfig;
  // 如果未启用压缩，这里不会被注入
  @Autowired(required = false)
  private IAsyncCompressService asyncCompressService;

  @Bean
  public IUploadLocalService localUpload() {
    return new SampleUploadServiceImpl(uploadLocalConfig, asyncCompressService);
  }

  @Bean
  @Conditional(TinypngCondition.class)
  public IAsyncCompressService tinypngAsyncCompressService() {
    return new TinypngAsyncCompressServiceImpl();
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public ContextUtil getContextUtil() {
    return new ContextUtil();
  }

  @PostConstruct
  public void init() {
    if (uploadLocalConfig.getCompress().isEnabled()) {
      // 使用 tinypng 进行压缩
      if(COMPRESS_TYPE_TINYPNG.equalsIgnoreCase(uploadLocalConfig.getCompress().getType())){
        validTinypng();
      }else{
        throw ExceptionUtils.get("[DSE-UL-2]Only supports tinypng compression.");
      }
    }

  }

  /**
   * 如果开启了图片压缩 <code>dragonshard.upload.local.compress.enabled = true</code>， 并且将压缩实现类型指定为
   * <code>dragonshard.upload.local.compress.type = tinypng</code>， 则校验是否已正确引入依赖（dragonshard-tinypng-starter）并启用
   * <code>dragonshard.tinypng.enabled = true</code>
   */
  private void validTinypng() {
    if (!ClassUtils
      .isPresent("net.dragonshard.dsf.tinypng.configuration.TinypngAutoConfiguration")) {
      throw ExceptionUtils.get("[DSE-UL-3]Could not find dragonshard-tinypng-starter in your project...");
    }
    if (tinypngEnabled != null && !tinypngEnabled) {
      throw ExceptionUtils
        .get("[DSE-UL-4][dragonshard.tinypng.enabled] must be set to 'true', now it is 'false'");
    }
  }



}

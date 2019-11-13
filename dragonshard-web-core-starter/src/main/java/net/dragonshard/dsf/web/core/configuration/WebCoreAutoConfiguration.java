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

package net.dragonshard.dsf.web.core.configuration;

import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.web.core.configuration.property.WebCoreProperties;
import net.dragonshard.dsf.web.core.filter.RequestConvertFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 启动类
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-18
 **/
@Slf4j
@Configuration
@Import({
  JacksonAutoConfiguration.class,
  WebCoreProperties.class
})
@ConditionalOnProperty(prefix = "dragonshard.web-core", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebCoreAutoConfiguration {

  @Value("${server.servlet.context-path:/}")
  private String contextPath;

  private final WebCoreProperties webCoreProperties;

  @Autowired
  public WebCoreAutoConfiguration(WebCoreProperties webCoreProperties) {
    this.webCoreProperties = webCoreProperties;
  }

  @Bean
  @ConditionalOnMissingBean
  public WebCoreMvcAutoConfiguration webMvcConfiguration() {
    return new WebCoreMvcAutoConfiguration(webCoreProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public RequestConvertFilter requestConvertFilter() {
    return new RequestConvertFilter();
  }


}

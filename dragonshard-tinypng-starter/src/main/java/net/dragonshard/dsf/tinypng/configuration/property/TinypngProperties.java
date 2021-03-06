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

package net.dragonshard.dsf.tinypng.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置文件
 *
 * @author mayee
 * @version v1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "dragonshard.tinypng")
public class TinypngProperties {

  /**
   * 是否开启
   */
  private Boolean enabled = true;
  /**
   * 用于认证
   */
  private String apiKey;
  /**
   * 严格模式，默认false
   */
  private Boolean strict = false;
  /**
   * 代理
   */
  private String proxy;

}

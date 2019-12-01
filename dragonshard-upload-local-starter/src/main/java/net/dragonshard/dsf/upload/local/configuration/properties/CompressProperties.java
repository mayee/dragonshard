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

package net.dragonshard.dsf.upload.local.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 压缩配置
 *
 * @author mayee
 **/
@Component
@ConfigurationProperties(prefix = "dragonshard.upload.local.compress")
@Data
public class CompressProperties {

  /**
   * 是否开启，默认 false
   */
  private boolean enabled = false;

  /**
   * 压缩实现类型, 当前仅支持 tinypng
   */
  private String type = "tinypng";

}

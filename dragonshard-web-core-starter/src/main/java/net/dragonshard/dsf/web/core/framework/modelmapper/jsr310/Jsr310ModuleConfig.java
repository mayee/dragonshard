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
package net.dragonshard.dsf.web.core.framework.modelmapper.jsr310;

import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Config for {@link Jsr310Module}
 *
 * @author Chun Han Hsiao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jsr310ModuleConfig {

  private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
  private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
  private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

  @Builder.Default
  private String datePattern = DEFAULT_DATE_PATTERN;
  @Builder.Default
  private String dateTimePattern = DEFAULT_DATE_TIME_PATTERN;
  @Builder.Default
  private String timePattern = DEFAULT_TIME_PATTERN;
  @Builder.Default
  private ZoneId zoneId = ZoneId.systemDefault();
}

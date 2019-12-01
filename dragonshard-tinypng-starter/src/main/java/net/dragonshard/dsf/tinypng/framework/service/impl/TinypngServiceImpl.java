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

package net.dragonshard.dsf.tinypng.framework.service.impl;

import com.tinify.Source;
import com.tinify.Tinify;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.tinypng.framework.service.ITinypngService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

/**
 * 压缩服务实现类
 *
 * @author mayee
 **/
@Slf4j
@NoArgsConstructor
public class TinypngServiceImpl implements ITinypngService {

  @Async
  @Override
  public void compressAndSave(String fromFilePath, String toFilePath) {
    if (StringUtils.isBlank(fromFilePath)) {
      return;
    }
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Source source;
    try {
      source = Tinify.fromFile(fromFilePath);
      source.toFile(toFilePath);
    } catch (Exception e) {
      log.warn("[DSE-TP-3]Compressing error. file > {}, msg > {}", fromFilePath, e.getMessage());
    } finally {
      stopWatch.stop();
      if (log.isDebugEnabled()) {
        log.debug("Compression completed({}), total > {}s [{}]", Tinify.compressionCount(),
          stopWatch.getTotalTimeSeconds(), fromFilePath);
      }
    }
  }

  @Override
  public int getCompressionCount() {
    return Tinify.compressionCount();
  }


}

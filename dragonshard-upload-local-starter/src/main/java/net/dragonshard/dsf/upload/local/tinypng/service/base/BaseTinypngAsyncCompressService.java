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

package net.dragonshard.dsf.upload.local.tinypng.service.base;

import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.upload.local.strategy.context.TinypngContext;
import net.dragonshard.dsf.upload.local.tinypng.service.IAsyncCompressService;
import org.springframework.scheduling.annotation.Async;

/**
 * 抽象骨架
 *
 * @author mayee
 **/
@Slf4j
public abstract class BaseTinypngAsyncCompressService implements IAsyncCompressService {

  @Async
  @Override
  public void compress(String filePath) {
    new TinypngContext().process(filePath);
  }

}

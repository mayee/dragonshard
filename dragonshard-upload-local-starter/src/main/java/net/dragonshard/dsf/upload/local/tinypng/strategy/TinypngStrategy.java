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

package net.dragonshard.dsf.upload.local.tinypng.strategy;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.ClassUtils;
import net.dragonshard.dsf.upload.local.common.UploadLocalUtils;
import net.dragonshard.dsf.upload.local.strategy.ICompressStrategy;

/**
 * tinypng 的压缩策略实现
 *
 * @author mayee
 **/
@Slf4j
@SuppressWarnings("all")
public class TinypngStrategy implements ICompressStrategy {

  @Override
  public void compress(String filePath) {
    if (UploadLocalUtils.validCompressWithTinypng(filePath)) {
      Class clazz = ClassUtils
        .toClassConfident("net.dragonshard.dsf.tinypng.framework.service.impl.TinypngServiceImpl");
      if (clazz != null) {
        try {
          Object object = clazz.newInstance();
          Method method = clazz.getMethod("compressAndSave", String.class, String.class);
          method.invoke(object, filePath, filePath);
        } catch (Exception e) {
          log.warn("Method call failed -> {}", e.getMessage());
        }
      }
    }
  }


}

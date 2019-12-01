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

package net.dragonshard.dsf.upload.local.strategy.context;

import java.util.ArrayList;
import java.util.List;
import net.dragonshard.dsf.upload.local.strategy.ICompressStrategy;

/**
 * 上下文抽象类
 *
 * @author mayee
 **/
public abstract class BaseContext {

  private List<ICompressStrategy> strategyList = new ArrayList<>();

  public void process(String filePath) {
    initBuildStrategy(strategyList);
    for (int i = 0, n = strategyList.size(); i < n; i++) {
      strategyList.get(i).compress(filePath);
    }
  }

  /**
   * 设置参与压缩的策略。
   *
   * @param strategyList 策略集合
   */
  protected abstract void initBuildStrategy(List<ICompressStrategy> strategyList);
}

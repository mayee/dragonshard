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

package net.dragonshard.dsf.upload.local.strategy;

/**
 * 压缩策略
 *
 * @author mayee
 **/
@FunctionalInterface
public interface ICompressStrategy {

  /**
   * 压缩处理
   *
   * @param filePath 待压缩的文件路径
   */
  void compress(String filePath);

}

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

package net.dragonshard.dsf.tinypng.framework.service;

/**
 * 顶层接口
 *
 * @author mayee
 **/
public interface ITinypngService {

  /**
   * 压缩并保存
   *
   * @param fromFilePath 输入文件
   * @param toFilePath 输出文件
   */
  void compressAndSave(String fromFilePath, String toFilePath);

  /**
   * 获取本月的压缩计数
   *
   * @return 压缩数
   */
  int getCompressionCount();

}

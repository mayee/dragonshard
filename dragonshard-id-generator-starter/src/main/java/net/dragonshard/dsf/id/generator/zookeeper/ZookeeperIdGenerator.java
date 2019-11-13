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

package net.dragonshard.dsf.id.generator.zookeeper;

public interface ZookeeperIdGenerator {

  /**
   * 获取全局唯一序号
   *
   * @param name 资源名字
   * @param key 资源Key
   * @return String
   * @throws Exception 异常
   */
  String nextSequenceId(String name, String key) throws Exception;

  String nextSequenceId(String compositeKey) throws Exception;

  String[] nextSequenceIds(String name, String key, int count) throws Exception;

  String[] nextSequenceIds(String compositeKey, int count) throws Exception;
}

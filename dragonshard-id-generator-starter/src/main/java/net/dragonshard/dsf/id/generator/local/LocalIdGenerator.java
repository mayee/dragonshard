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

package net.dragonshard.dsf.id.generator.local;

/**
 * 本地ID生成器接口
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-14
 **/
public interface LocalIdGenerator {

  /**
   * 获取全局唯一ID，根据Twitter雪花ID算法。为兼顾到前端失精问题，把返回值long改成String SnowFlake算法用来生成64位的ID，刚好可以用long整型存储，能够用于分布式系统中生产唯一的ID，
   * 并且生成的ID有大致的顺序。 在这次实现中，生成的64位ID可以分成5个部分： 0 - 41位时间戳 - 5位数据中心标识 - 5位机器标识 - 12位序列号
   *
   * @param dataCenterId 数据中心标识ID
   * @param machineId 机器标识ID
   * @return String
   */
  String nextUniqueId(long dataCenterId, long machineId) throws Exception;

  String nextUniqueId() throws Exception;

  String[] nextUniqueIds(long dataCenterId, long machineId, int count) throws Exception;

  String[] nextUniqueIds(int count) throws Exception;

}

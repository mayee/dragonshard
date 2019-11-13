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

package net.dragonshard.dsf.id.generator.local.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.id.generator.configuration.property.LocalIdGeneratorProperties;
import net.dragonshard.dsf.id.generator.local.LocalIdGenerator;

/**
 * 自增ID实现（local）
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-14
 **/
@Slf4j
public class LocalIdGeneratorImpl implements LocalIdGenerator {

  // 2017-01-01 00:00:00:000
  private static final long DEFAULT_START_TIMESTAMP = 1483200000000L;
  private volatile Map<String, SnowflakeIdGenerator> idGeneratorMap = new ConcurrentHashMap<String, SnowflakeIdGenerator>();
  private LocalIdGeneratorProperties localIdGeneratorProperties;

  public LocalIdGeneratorImpl(LocalIdGeneratorProperties localIdGeneratorProperties) {
    this.localIdGeneratorProperties = localIdGeneratorProperties;
  }

  @Override
  public String nextUniqueId() throws Exception {
    return nextUniqueId(localIdGeneratorProperties.getDataCenter(),
      localIdGeneratorProperties.getMachine());
  }

  @Override
  public String nextUniqueId(long dataCenterId, long machineId) throws Exception {
    return getIdGenerator(dataCenterId, machineId).nextId();
  }

  @Override
  public String[] nextUniqueIds(int count) throws Exception {
    return nextUniqueIds(localIdGeneratorProperties.getDataCenter(),
      localIdGeneratorProperties.getMachine(), count);
  }

  @Override
  public String[] nextUniqueIds(long dataCenterId, long machineId, int count) throws Exception {
    return getIdGenerator(dataCenterId, machineId).nextIds(count);
  }

  private SnowflakeIdGenerator getIdGenerator(long dataCenterId, long machineId) {
    String key = dataCenterId + "-" + machineId;

    SnowflakeIdGenerator idGenerator = idGeneratorMap.get(key);
    if (idGenerator == null) {
      SnowflakeIdGenerator newIdGnerator = new SnowflakeIdGenerator(DEFAULT_START_TIMESTAMP,
        dataCenterId, machineId);
      idGenerator = idGeneratorMap.putIfAbsent(key, newIdGnerator);
      if (idGenerator == null) {
        idGenerator = newIdGnerator;
      }
    }

    return idGenerator;
  }
}

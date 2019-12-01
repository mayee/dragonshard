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

package net.dragonshard.dsf.id.generator.redis.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.DateUtil;
import net.dragonshard.dsf.core.toolkit.ExceptionUtils;
import net.dragonshard.dsf.core.toolkit.KeyUtil;
import net.dragonshard.dsf.id.generator.configuration.property.RedisIdGeneratorProperties;
import net.dragonshard.dsf.id.generator.redis.RedisIdGenerator;
import net.dragonshard.dsf.id.generator.redis.handler.RedisHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.Assert;

@Slf4j
public class RedisIdGeneratorImpl implements RedisIdGenerator {

  private static final String DATE_FORMAT = "yyyyMMddHHmmssSSS";
  private static final String DECIMAL_FORMAT = "00000000";
  private static final int MAX_BATCH_COUNT = 1000;

  @Autowired
  private RedisHandler redisHandler;
  private RedisIdGeneratorProperties redisIdGeneratorProperties;
  private RedisScript<List<Object>> redisScript;

  public RedisIdGeneratorImpl(RedisIdGeneratorProperties redisIdGeneratorProperties) {
    this.redisIdGeneratorProperties = redisIdGeneratorProperties;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @PostConstruct
  public void init() {
    String luaScript = buildLuaScript();
    redisScript = new DefaultRedisScript(luaScript, List.class);
  }

  private String buildLuaScript() {
    StringBuilder lua = new StringBuilder();
    lua.append("local incrKey = KEYS[1];");
    lua.append("\nlocal step = ARGV[1];");
    lua.append("\nlocal count;");
    lua.append("\ncount = tonumber(redis.call('incrby', incrKey, step));");
    lua.append("\nlocal now = redis.call('time');");
    lua.append("\nreturn {now[1], now[2], count}");

    return lua.toString();
  }

  @Override
  public String nextUniqueId(String name, String key, int step, int length) throws Exception {
    Assert.hasText(name, "Parameter [ name ] is null or empty");
    Assert.hasText(key, "Parameter [ key ] is null or empty");
    String compositeKey = KeyUtil
      .getCompositeKey(redisIdGeneratorProperties.getPrefix(), name, key);
    return nextUniqueId(compositeKey, step, length);
  }

  @Override
  public String nextUniqueId(String compositeKey, int step, int length) throws Exception {
    Assert.hasText(compositeKey, "Parameter [ composite key ] is null or empty");
    List<String> keys = new ArrayList<String>();
    keys.add(compositeKey);

    StringRedisTemplate redisTemplate = redisHandler.getRedisTemplate();
    List<Object> result = redisTemplate.execute(redisScript, keys, step + "");
    Object value1 = result.get(0);
    Object value2 = result.get(1);
    Object value3 = result.get(2);

    long mill =
      Long.parseLong(String.valueOf(value1)) * 1000 + Long.parseLong(String.valueOf(value2)) / 1000;
    Date date = new Date(mill);

    return DateUtil.formatDate(date, DATE_FORMAT) +
      formatString((long) value3, length);
  }

  @Override
  public String[] nextUniqueIds(String name, String key, int step, int length, int count)
    throws Exception {
    if (count <= 0 || count > MAX_BATCH_COUNT) {
      throw ExceptionUtils
        .get(String.format("Count can't be greater than %d or less than 0", MAX_BATCH_COUNT));
    }

    String[] nextUniqueIds = new String[count];
    for (int i = 0; i < count; i++) {
      nextUniqueIds[i] = nextUniqueId(name, key, step, length);
    }

    return nextUniqueIds;
  }

  @Override
  public String[] nextUniqueIds(String compositeKey, int step, int length, int count)
    throws Exception {
    if (count <= 0 || count > MAX_BATCH_COUNT) {
      throw ExceptionUtils
        .get(String.format("Count can't be greater than %d or less than 0", MAX_BATCH_COUNT));
    }

    String[] nextUniqueIds = new String[count];
    for (int i = 0; i < count; i++) {
      nextUniqueIds[i] = nextUniqueId(compositeKey, step, length);
    }

    return nextUniqueIds;
  }

  private static String formatString(long key, int length) {
    String value = String.valueOf(key);
    if (value.length() < length) {
      DecimalFormat format = getDecimalFormat();
      return format.format(key);
    } else {
      return value.substring(value.length() - length);
    }
  }

  private static volatile Map<String, DecimalFormat> decimalFormatMap = new ConcurrentHashMap<String, DecimalFormat>();

  private static DecimalFormat getDecimalFormat() {
    DecimalFormat decimalFormat = decimalFormatMap.get(RedisIdGeneratorImpl.DECIMAL_FORMAT);
    if (decimalFormat == null) {
      DecimalFormat newDecimalFormat = new DecimalFormat();
      newDecimalFormat.applyPattern(RedisIdGeneratorImpl.DECIMAL_FORMAT);
      decimalFormat = decimalFormatMap
        .putIfAbsent(RedisIdGeneratorImpl.DECIMAL_FORMAT, newDecimalFormat);
      if (decimalFormat == null) {
        decimalFormat = newDecimalFormat;
      }
    }

    return decimalFormat;
  }
}

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

package net.dragonshard.dsf.core.toolkit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期转换工具类
 *
 * @author mayee
 * @version v1.0
 **/
public class DateUtil {

  private static volatile Map<String, DateTimeFormatter> dateFormatMap = new ConcurrentHashMap<String, DateTimeFormatter>();

  public static String formatDate(Date date, String pattern) {
    DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);

    ZoneId zoneId = ZoneId.systemDefault();
    Instant instant = date.toInstant();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

    return localDateTime.format(dateTimeFormatter);
  }

  public static Date parseDate(String date, String pattern) {
    DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);

    LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);

    ZoneId zoneId = ZoneId.systemDefault();
    Instant instant = localDateTime.atZone(zoneId).toInstant();

    return Date.from(instant);
  }

  private static DateTimeFormatter getDateTimeFormatter(String pattern) {
    DateTimeFormatter dateTimeFormatter = dateFormatMap.get(pattern);
    if (dateTimeFormatter == null) {
      DateTimeFormatter newDateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
      dateTimeFormatter = dateFormatMap.putIfAbsent(pattern, newDateTimeFormatter);
      if (dateTimeFormatter == null) {
        dateTimeFormatter = newDateTimeFormatter;
      }
    }

    return dateTimeFormatter;
  }
}

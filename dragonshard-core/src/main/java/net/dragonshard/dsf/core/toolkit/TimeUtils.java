/*
 *   Copyright 1999-2018 zhangchi.dev Holding Ltd.
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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Time utils
 *
 * @author mayee
 * @version v1.0
 **/
public class TimeUtils {
    private static final long MINUTE = 60 * 1000L;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long MONTH = 31 * DAY;
    private static final long YEAR = 12 * MONTH;

    /**
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;
    /**
     * 获取默认日期格式: yyyy-MM-dd
     */
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = TimeFormat.SHORT_DATE_PATTERN_LINE.formatter;
    /**
     * 获取默认日期格式: HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = TimeFormat.SHORT_TIME_PATTERN_LINE.formatter;


    TimeUtils() {
        throw new AssertionError();
    }

    /**
     * 默认格式的字符串转时间对象（yyyy-MM-dd HH:mm:ss）
     *
     * @param timeStr 时间字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parseDefaultDateTime(String timeStr) {
        return LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 自定义格式的字符串转时间对象
     *
     * @param timeStr 时间字符串
     * @param format  时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String timeStr, TimeFormat format) {
        return LocalDateTime.parse(timeStr, format.formatter);
    }

    /**
     * 默认格式的字符串转日期对象（yyyy-MM-dd）
     *
     * @param timeStr 日期字符串
     * @return LocalDate
     */
    public static LocalDate parseDefaultDate(String timeStr) {
        return LocalDate.parse(timeStr, DEFAULT_DATE_FORMATTER);
    }

    /**
     * 自定义格式的字符串转日期对象
     *
     * @param timeStr 日期字符串
     * @param format  时间格式
     * @return LocalDate
     */
    public static LocalDate parseDate(String timeStr, TimeFormat format) {
        return LocalDate.parse(timeStr, format.formatter);
    }

    /**
     * 默认格式的字符串转时分秒对象（HH:mm:ss）
     *
     * @param timeStr 日期字符串
     * @return LocalTime
     */
    public static LocalTime parseDefaultTime(String timeStr) {
        return LocalTime.parse(timeStr, DEFAULT_DATE_FORMATTER);
    }

    /**
     * 自定义格式的字符串转时分秒对象
     *
     * @param timeStr 日期字符串
     * @param format  时间格式
     * @return LocalTime
     */
    public static LocalTime parseTime(String timeStr, TimeFormat format) {
        return LocalTime.parse(timeStr, format.formatter);
    }

    /**
     * 时间对象转默认格式的字符串（yyyy-MM-dd HH:mm:ss）
     *
     * @param time LocalDateTime
     * @return 日期字符串
     */
    public static String parseDefaultDateTime(LocalDateTime time) {
        return DEFAULT_DATETIME_FORMATTER.format(time);
    }

    /**
     * 时间对象转自定义格式的字符串
     *
     * @param time   LocalDateTime
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String parseDateTime(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * 日期对象转默认格式的字符串（yyyy-MM-dd）
     *
     * @param date LocalDate
     * @return 日期字符串
     */
    public static String parseDefaultDate(LocalDate date) {
        return DEFAULT_DATE_FORMATTER.format(date);
    }

    /**
     * 日期对象转自定义格式的字符串
     *
     * @param date   LocalDate
     * @param format 日期格式
     * @return 日期字符串
     */
    public static String parseDate(LocalDate date, TimeFormat format) {
        return format.formatter.format(date);
    }

    /**
     * 时分秒对象转默认格式的字符串（HH:mm:ss）
     *
     * @param time LocalTime
     * @return 时分秒字符串
     */
    public static String parseDefaultTime(LocalTime time) {
        return DEFAULT_TIME_FORMATTER.format(time);
    }

    /**
     * 时分秒对象转自定义格式的字符串
     *
     * @param time   LocalTime
     * @param format 时分秒格式
     * @return 时分秒字符串
     */
    public static String parseTime(LocalTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * 获取默认格式的当前时间（yyyy-MM-dd HH:mm:ss）
     *
     * @return 时间字符串
     */
    public static String getDefaultCurrentDatetime() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取自定义格式的当前时间
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getCurrentDatetime(TimeFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间毫秒
     *
     * @return 毫秒值
     */
    public static long getCurrentMillisecond() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 时间对象转毫秒
     *
     * @param localDateTime 时间对象
     * @return 毫秒值
     */
    public static long getMillisecondForLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 毫秒转时间
     *
     * @param millisecond 毫秒
     * @return 时间对象
     */
    public static LocalDateTime getDateTimeByMillisecond(long millisecond) {
        return LocalDateTime.ofEpochSecond(millisecond, 0, ZoneOffset.ofHours(8));
    }

    /**
     * 毫秒转时间字符串(自定义格式)
     *
     * @param epochSecond 毫秒值
     * @param format      时间格式
     * @return 时间字符串
     */
    public static String getDateTimeStringByMillisecond(long epochSecond, TimeFormat format) {
        return parseDateTime(LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.ofHours(8)), format);
    }

    /**
     * 返回本月第一天
     *
     * @return 日期对象
     */
    public static LocalDate getFirstDayOfThisMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 返回本月第n天
     *
     * @param num 天数
     * @return 日期对象
     */
    public static LocalDate getSomeDayOfThisMonth(int num) {
        return LocalDate.now().withDayOfMonth(num);
    }

    /**
     * 返回本月最后一天
     *
     * @return 日期对象
     */
    public static LocalDate getLastDayOfThisMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 以字符串格式返回某个时间与当前时间的差值
     *
     * @param localDateTime 时间对象
     * @return 字符串格式的差值
     */
    public static String getTimeFormatText(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        long diff = getCurrentMillisecond() - getMillisecondForLocalDateTime(localDateTime);
        long r = 0;
        if (diff > YEAR) {
            r = (diff / YEAR);
            return r + "年前";
        }
        if (diff > MONTH) {
            r = (diff / MONTH);
            return r + "个月前";
        }
        if (diff > DAY) {
            r = (diff / DAY);
            return r + "天前";
        }
        if (diff > HOUR) {
            r = (diff / HOUR);
            return r + "小时前";
        }
        if (diff > MINUTE) {
            r = (diff / MINUTE);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 是否为同一天
     *
     * @param d1 对比的日期
     * @param d2 对比的日期
     * @return 相同true，不同false
     */
    public static boolean sameDate(Date d1, Date d2) {
        LocalDate localDate1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    /**
     * Date to LocalDate
     *
     * @param date Date对象
     * @return LocalDate对象
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date to LocalDateTime
     *
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Date to LocalTime
     *
     * @param date Date对象
     * @return LocalTime对象
     */
    public static LocalTime toLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * LocalDate to Date
     *
     * @param localDate LocalDate对象
     * @return Date对象
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime to Date
     *
     * @param localDateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 时间格式
     */
    public enum TimeFormat {

        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

        /**
         * 仅时间格式
         */
        SHORT_TIME_PATTERN_LINE("HH:mm:ss"),
        SHORT_TIME_PATTERN_WITH_MILSEC_LINE("HH:mm:ss.SSS"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),

        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NUMBER("yyyyMMddHHmmssSSS");

        String text;

        transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            this.text = pattern;
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        public String getText() {
            return text;
        }

        /**
         * 根据枚举码获取枚举
         *
         * @param text 枚举码
         * @return 枚举
         */
        public static TimeFormat getByText(String text) {
            for (TimeFormat timeFormat : values()) {
                if (text.equals(timeFormat.getText())) {
                    return timeFormat;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {

    }

}

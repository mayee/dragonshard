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

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 拼音工具类
 *
 * @author mayee
 * @version v1.0
 **/
public class PinyinUtils {

    /**
     * 音标
     *
     * @param text 文字
     * @return 音标
     */
    public static String toPinyinMark(String text) throws PinyinException {
        return text == null ? null : PinyinHelper.convertToPinyinString(text, "", PinyinFormat.WITH_TONE_MARK);
    }

    public static String toPinyinMark(String text, String separator) throws PinyinException {
        return text == null ? null : PinyinHelper.convertToPinyinString(text, separator, PinyinFormat.WITH_TONE_MARK);
    }

    /**
     * 数字标记
     *
     * @param text 文字
     * @return 数字标记
     */
    public static String toPinyinNumber(String text) throws PinyinException {
        return text == null ? null : PinyinHelper.convertToPinyinString(text, "", PinyinFormat.WITH_TONE_NUMBER);
    }

    public static String toPinyinNumber(String text, String separator) throws PinyinException {
        return text == null ? null : PinyinHelper.convertToPinyinString(text, separator, PinyinFormat.WITH_TONE_NUMBER);
    }

    /**
     * 默认
     *
     * @param text 文字
     * @return 拼音
     */
    public static String toPinyin(String text) throws PinyinException {
        return text == null ? null : PinyinHelper.convertToPinyinString(text, "", PinyinFormat.WITHOUT_TONE);
    }

    public static String toPinyin(String text, String separator) throws PinyinException {
        return text == null ? null : PinyinHelper.convertToPinyinString(text, separator, PinyinFormat.WITHOUT_TONE);
    }

    /**
     * 首字母缩写
     *
     * @param text 文字
     * @return 首字母缩写
     */
    public static String toShortPinyin(String text) throws PinyinException {
        return text == null ? null : PinyinHelper.getShortPinyin(text);
    }

}

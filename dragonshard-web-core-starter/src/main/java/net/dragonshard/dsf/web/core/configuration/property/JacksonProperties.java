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

package net.dragonshard.dsf.web.core.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jackson配置
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-18
 **/
@Data
@Component
@ConfigurationProperties(prefix = "dragonshard.web-core.jackson")
public class JacksonProperties {

    /**
     * 是否开启
     */
    private boolean enabled = true;

    String timeFormatSerializer = "HH:mm:ss";
    String dateFormatSerializer = "yyyy-MM-dd";
    String dateTimeFormatSerializer = "yyyy-MM-dd HH:mm:ss";

    /**
     * 序列化设置
     * <p>
     * ALWAYS       默认
     * NON_DEFAULT  属性为默认值不序列化
     * NON_EMPTY    属性为 空（""） 或者为 NULL 不序列化
     * NON_NULL     属性为NULL 不序列化
     */
    String defaultPropertyInclusion = "NON_NULL";

    /**
     * 给所有数字加上引号
     */
    boolean writeNumbersAsStrings = true;

    /**
     * 是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
     */
    boolean allowUnquotedControlChars = true;

    /**
     * 反序列化时, 遇到未知属性时是否引起结果失败( 抛 JsonMappingException 异常 )
     * false: 忽略
     * true:  抛异常
     */
    boolean failOnUnknownProperties = false;

    /**
     * 配置为true表示mapper接受只有一个元素的数组的反序列化
     */
    boolean acceptSingleValueAsArray = true;

    /**
     * 配置为true表示mapper允许对null进行转换
     */
    boolean acceptEmptyStringAsNullObject = true;

}

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

package net.dragonshard.dsf.web.core.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import net.dragonshard.dsf.web.core.common.WebCoreConstants;
import net.dragonshard.dsf.web.core.configuration.property.WebCoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson配置类
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-18
 **/
@Slf4j
@ConditionalOnProperty(prefix = "dragonshard.web-core.jackson", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JacksonAutoConfiguration {

    private final WebCoreProperties webCoreProperties;

    @Autowired
    public JacksonAutoConfiguration(WebCoreProperties webCoreProperties) {
        this.webCoreProperties = webCoreProperties;
    }

    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(webCoreProperties.getJackson().getDateFormatSerializer())));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(webCoreProperties.getJackson().getDateFormatSerializer())));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(webCoreProperties.getJackson().getTimeFormatSerializer())));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer((DateTimeFormatter.ofPattern(webCoreProperties.getJackson().getTimeFormatSerializer()))));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(webCoreProperties.getJackson().getDateTimeFormatSerializer())));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(webCoreProperties.getJackson().getDateTimeFormatSerializer())));
        mapper.registerModule(module);

        /*
         * 是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
         */
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, webCoreProperties.getJackson().isAllowUnquotedControlChars());
        /*
         * 给所有数字加上引号
         */
        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, webCoreProperties.getJackson().isWriteNumbersAsStrings());
        /*
         * 反序列化时, 遇到未知属性时是否引起结果失败( 抛 JsonMappingException 异常 )
         * false: 忽略
         * true:  抛异常
         */
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, webCoreProperties.getJackson().isFailOnUnknownProperties());
        /*
         * 配置为true表示mapper接受只有一个元素的数组的反序列化
         */
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, webCoreProperties.getJackson().isAcceptSingleValueAsArray());
        /*
         * 配置为true表示mapper允许对null进行转换
         */
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, webCoreProperties.getJackson().isAcceptEmptyStringAsNullObject());


        /*
         * 序列化设置
         *
         * ALWAYS       默认
         * NON_DEFAULT  属性为默认值不序列化
         * NON_EMPTY    属性为 空（""） 或者为 NULL 不序列化
         * NON_NULL     属性为NULL 不序列化
         */
        if (WebCoreConstants.JACKSON.NON_DEFAULT.equalsIgnoreCase(webCoreProperties.getJackson().getDefaultPropertyInclusion())) {
            mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        } else if (WebCoreConstants.JACKSON.NON_EMPTY.equalsIgnoreCase(webCoreProperties.getJackson().getDefaultPropertyInclusion())) {
            mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
        } else if (WebCoreConstants.JACKSON.NON_NULL.equalsIgnoreCase(webCoreProperties.getJackson().getDefaultPropertyInclusion())) {
            mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        } else {
            mapper.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        }
        return mapper;
    }

}

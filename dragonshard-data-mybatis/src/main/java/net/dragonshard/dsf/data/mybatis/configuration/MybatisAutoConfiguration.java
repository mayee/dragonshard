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
package net.dragonshard.dsf.data.mybatis.configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import net.dragonshard.dsf.data.mybatis.plus.CommonMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis 配置
 *
 * @author mayee
 */
@Configuration
public class MybatisAutoConfiguration {

    /**
     * 分页
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 自动填充
     */
    @Bean
    public CommonMetaObjectHandler commonMetaObjectHandler() {
        return new CommonMetaObjectHandler();
    }

}

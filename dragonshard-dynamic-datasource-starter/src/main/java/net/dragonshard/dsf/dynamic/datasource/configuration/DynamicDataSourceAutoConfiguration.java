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
package net.dragonshard.dsf.dynamic.datasource.configuration;

import net.dragonshard.dsf.dynamic.datasource.DynamicDataSourceConfigure;
import net.dragonshard.dsf.dynamic.datasource.DynamicDataSourceCreator;
import net.dragonshard.dsf.dynamic.datasource.DynamicRoutingDataSource;
import net.dragonshard.dsf.dynamic.datasource.aop.DynamicDataSourceAdvisor;
import net.dragonshard.dsf.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import net.dragonshard.dsf.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import net.dragonshard.dsf.dynamic.datasource.configuration.druid.DruidDynamicDataSourceConfiguration;
import net.dragonshard.dsf.dynamic.datasource.processor.DsHeaderProcessor;
import net.dragonshard.dsf.dynamic.datasource.processor.DsProcessor;
import net.dragonshard.dsf.dynamic.datasource.processor.DsSessionProcessor;
import net.dragonshard.dsf.dynamic.datasource.processor.DsSpelExpressionProcessor;
import net.dragonshard.dsf.dynamic.datasource.provider.DynamicDataSourceProvider;
import net.dragonshard.dsf.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

/**
 * 动态数据源核心自动配置类
 *
 * @author TaoYu Kanyuxia
 * @see DynamicDataSourceProvider
 * @see DynamicRoutingDataSource
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import({DruidDynamicDataSourceConfiguration.class})
public class DynamicDataSourceAutoConfiguration {

    @Autowired
    private DynamicDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider(DynamicDataSourceCreator dynamicDataSourceCreator) {
        return new YmlDynamicDataSourceProvider(properties, dynamicDataSourceCreator);
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceCreator dynamicDataSourceCreator(@Autowired(required = false) WebApplicationContext webApplicationContext) {
        DynamicDataSourceCreator dynamicDataSourceCreator = new DynamicDataSourceCreator();
        dynamicDataSourceCreator.setApplicationContext(webApplicationContext);
        dynamicDataSourceCreator.setDruidGlobalConfig(properties.getGlobalDruid());
        dynamicDataSourceCreator.setHikariGlobalConfig(properties.getGlobalHikari());
        dynamicDataSourceCreator.setGlobalPublicKey(properties.getPublicKey());
        return dynamicDataSourceCreator;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setStrict(properties.getStrict());
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor();
        interceptor.setDsProcessor(dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
        DsSessionProcessor sessionProcessor = new DsSessionProcessor();
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(spelExpressionProcessor);
        return headerProcessor;
    }

    @Bean
    @ConditionalOnBean(DynamicDataSourceConfigure.class)
    public DynamicDataSourceAdvisor dynamicAdvisor(DynamicDataSourceConfigure dynamicDataSourceConfigure, DsProcessor dsProcessor) {
        DynamicDataSourceAdvisor advisor = new DynamicDataSourceAdvisor(dynamicDataSourceConfigure.getMatchers());
        advisor.setDsProcessor(dsProcessor);
        return advisor;
    }

}

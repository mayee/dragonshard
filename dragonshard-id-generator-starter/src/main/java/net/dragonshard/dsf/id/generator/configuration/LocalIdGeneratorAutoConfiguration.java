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

package net.dragonshard.dsf.id.generator.configuration;

import net.dragonshard.dsf.id.generator.configuration.property.LocalIdGeneratorProperties;
import net.dragonshard.dsf.id.generator.local.LocalIdGenerator;
import net.dragonshard.dsf.id.generator.local.impl.LocalIdGeneratorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自增ID配置（local）
 *
 * @author mayee
 * @date 2019-07-14
 *
 * @version v1.0
 **/
@Configuration
@Import({ LocalIdGeneratorProperties.class })
@ConditionalOnProperty(prefix = "dragonshard.id-generator.local", name = "enabled", matchIfMissing = true)
public class LocalIdGeneratorAutoConfiguration {

    private final LocalIdGeneratorProperties localIdGeneratorProperties;

    @Autowired
    public LocalIdGeneratorAutoConfiguration(LocalIdGeneratorProperties localIdGeneratorProperties) {
        this.localIdGeneratorProperties = localIdGeneratorProperties;
    }

    @Bean
    public LocalIdGenerator localIdGenerator() {
        return new LocalIdGeneratorImpl(localIdGeneratorProperties);
    }

}

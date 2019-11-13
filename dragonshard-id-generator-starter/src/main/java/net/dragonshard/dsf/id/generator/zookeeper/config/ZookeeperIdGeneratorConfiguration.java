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

package net.dragonshard.dsf.id.generator.zookeeper.config;

import net.dragonshard.dsf.id.generator.configuration.property.ZookeeperIdGeneratorProperties;
import net.dragonshard.dsf.id.generator.zookeeper.ZookeeperIdGenerator;
import net.dragonshard.dsf.id.generator.zookeeper.handler.CuratorHandler;
import net.dragonshard.dsf.id.generator.zookeeper.handler.CuratorHandlerImpl;
import net.dragonshard.dsf.id.generator.zookeeper.impl.ZookeeperIdGeneratorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ZookeeperIdGeneratorConfiguration
 *
 * @author mayee
 * @version v1.0
 **/
@Configuration
@Import({
  ZookeeperIdGeneratorProperties.class
})
public class ZookeeperIdGeneratorConfiguration {

  @Bean
  public ZookeeperIdGenerator zookeeperIdGenerator() {
    return new ZookeeperIdGeneratorImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public CuratorHandler curatorHandler() {
    return new CuratorHandlerImpl();
  }
}

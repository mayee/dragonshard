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

package net.dragonshard.dsf.data.secret.configuration;

import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.data.secret.advice.SecretRequestAdvice;
import net.dragonshard.dsf.data.secret.configuration.property.AESProperties;
import net.dragonshard.dsf.data.secret.configuration.property.RSAProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * SecretAutoConfiguration
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-28
 **/
@Slf4j
@Configuration
@Import({SecretRequestAdvice.class, AESProperties.class, RSAProperties.class})
@ConditionalOnProperty(prefix = "dragonshard.secret", name = "enabled", matchIfMissing = true)
public class SecretAutoConfiguration {

}

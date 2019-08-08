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

package net.dragonshard.dsf.data.secret.algorithm.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.security.Provider;

/**
 * RSA密钥
 *
 * @author mayee
 * @date 2019-07-08
 *
 * @version v1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RSAKey extends SecretKey {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 系数
     */
    private String modulus;

    /**
     * 实现厂商类名
     */
    private Class<? extends Provider> providerClass;

}

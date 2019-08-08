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

package net.dragonshard.dsf.data.secret.algorithm.decrypt;


import net.dragonshard.dsf.data.secret.algorithm.key.SecretKey;

/**
 * 消息体解密接口
 *
 * @author mayee
 * @date 2019-07-08
 *
 * @version v1.0
 **/
public interface BodyDecrypt {

    /**
     * 解密
     *
     * @param input 密文
     * @param key   密钥
     * @return      明文
     */
    String decryptBody(String input, SecretKey key);

}

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


import static net.dragonshard.dsf.web.core.common.WebCoreConstants.SECRET.CIPHERTEXT_TYPE_BASE64;
import static net.dragonshard.dsf.web.core.common.WebCoreConstants.SECRET.CIPHERTEXT_TYPE_HEX;

import net.dragonshard.dsf.core.toolkit.EncryptUtils;
import net.dragonshard.dsf.data.secret.algorithm.key.AESKey;
import net.dragonshard.dsf.data.secret.algorithm.key.SecretKey;

/**
 * 消息体解密
 *
 * @author mayee
 * @version v1.0
 * @date 2019-05-03
 **/
public class AESBodyDecrypt implements BodyDecrypt {

  private String ciphertextType;

  public AESBodyDecrypt(String ciphertextType) {
    this.ciphertextType = ciphertextType;
  }

  @Override
  public String decryptBody(String input, SecretKey secretKey) {
    AESKey aesKey = (AESKey) secretKey;
    if (CIPHERTEXT_TYPE_BASE64.equalsIgnoreCase(ciphertextType)) {
      return EncryptUtils.aesDecryptBase64(input, aesKey.getKey());
    } else if (CIPHERTEXT_TYPE_HEX.equalsIgnoreCase(ciphertextType)) {
      return EncryptUtils.aesDecryptHex(input, aesKey.getKey());
    }
    return null;
  }

}

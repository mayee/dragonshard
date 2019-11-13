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
import static net.dragonshard.dsf.web.core.enums.DsfErrorCodeEnum.SECRET_PROVIDER_FAIL;
import static net.dragonshard.dsf.web.core.enums.DsfErrorCodeEnum.SECRET_PROVIDER_NOT_SUPPORT_BASE64;

import net.dragonshard.dsf.core.toolkit.EncryptUtils;
import net.dragonshard.dsf.data.secret.algorithm.key.RSAKey;
import net.dragonshard.dsf.data.secret.algorithm.key.SecretKey;
import net.dragonshard.dsf.data.secret.exception.SecretRequestDecryptException;
import org.apache.commons.lang3.StringUtils;

/**
 * 消息体解密(RSA)
 *
 * @author mayee
 * @version v1.0
 * @date 2019-05-03
 **/
public class RSABodyDecrypt implements BodyDecrypt {

  private String ciphertextType;

  public RSABodyDecrypt(String ciphertextType) {
    this.ciphertextType = ciphertextType;
  }

  @Override
  public String decryptBody(String input, SecretKey secretKey) {
    RSAKey rsaKey = (RSAKey) secretKey;
    if (CIPHERTEXT_TYPE_BASE64.equalsIgnoreCase(ciphertextType)) {
      if (rsaKey.getProviderClass() == null) {
        return new String(
          EncryptUtils.rsaDecryptBase64ByPirvateKey(
            input, rsaKey.getPrivateKey()
          )
        );
      } else {
        throw new SecretRequestDecryptException(SECRET_PROVIDER_NOT_SUPPORT_BASE64.msg());
      }
    } else if (CIPHERTEXT_TYPE_HEX.equalsIgnoreCase(ciphertextType)) {
      if (rsaKey.getProviderClass() == null) {
        return new String(
          EncryptUtils.rsaDecryptHexByPirvateKey(
            input, rsaKey.getPrivateKey()
          )
        );
      } else {
        try {
          return StringUtils.reverse(new String(
            EncryptUtils.rsaDecryptHexByProviderPirvateKey(
              input,
              rsaKey.getModulus(),
              rsaKey.getPrivateKey(),
              rsaKey.getProviderClass().newInstance()
            )
          ));
        } catch (InstantiationException | IllegalAccessException e) {
          throw new SecretRequestDecryptException(SECRET_PROVIDER_FAIL.msg());
        }
      }
    }
    return null;
  }

}

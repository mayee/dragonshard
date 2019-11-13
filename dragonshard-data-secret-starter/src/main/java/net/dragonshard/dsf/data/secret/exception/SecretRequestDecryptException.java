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

package net.dragonshard.dsf.data.secret.exception;

/**
 * 请求消息解密异常
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-25
 **/
public class SecretRequestDecryptException extends RuntimeException {

  public SecretRequestDecryptException() {
    super();
  }

  public SecretRequestDecryptException(String message) {
    super(message);
  }

  public SecretRequestDecryptException(String message, Throwable cause) {
    super(message, cause);
  }

  public SecretRequestDecryptException(Throwable cause) {
    super(cause);
  }
}

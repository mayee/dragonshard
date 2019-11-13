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

package net.dragonshard.dsf.web.core.common;

/**
 * 常量
 *
 * @author mayee
 * @version v1.0
 * @date 2018/11/15
 **/
public class WebCoreConstants {

  /**
   * Jackson
   */
  public static final class JACKSON {

    public static final String ALWAYS = "ALWAYS";
    public static final String NON_DEFAULT = "NON_DEFAULT";
    public static final String NON_EMPTY = "NON_EMPTY";
    public static final String NON_NULL = "NON_NULL";

  }

  public static final class MAPPING {

    public static final String X_PATH_VARIABLE_MAPPING_NAME = "X-DSF-MappingName";
  }

  public static final class LOG {

    public static String DSF_API_REQURL = "DSF_API_REQURL";
    public static String DSF_API_MAPPING = "DSF_API_MAPPING";
    public static String DSF_API_METHOD = "DSF_API_METHOD";
    public static String DSF_API_BEGIN_TIME = "DSF_API_BEGIN_TIME";
    public static String DSF_API_UID = "DSF_API_UID";
    public static String DSF_API_TRACE_ID = "DSF_API_TRACE_ID";
  }

  public static final class SECRET {

    public static String ALGORITHM_AES = "AES";
    public static String ALGORITHM_RSA = "RSA";

    public static String CIPHERTEXT_TYPE_BASE64 = "BASE64";
    public static String CIPHERTEXT_TYPE_HEX = "HEX";
  }

}

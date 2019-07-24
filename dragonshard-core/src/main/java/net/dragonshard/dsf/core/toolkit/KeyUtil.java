/*
 *   Copyright 1999-2018 zhangchi.dev Holding Ltd.
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

package net.dragonshard.dsf.core.toolkit;

import net.dragonshard.dsf.core.common.CoreConstants;

/**
 * key工具类
 *
 * @author mayee
 * @date 2019-07-15
 *
 * @version v1.0
 **/
public class KeyUtil {
    public static String getCompositeKey(String prefix, String name, String key) {
        return prefix + CoreConstants.REDIS_ID_SPLIT + name + CoreConstants.REDIS_ID_SPLIT + key;
    }

    public static String getCompositeWildcardKey(String prefix, String name) {
        return prefix + CoreConstants.REDIS_ID_SPLIT + name + "*";
    }

    public static String getCompositeWildcardKey(String key) {
        return key + "*";
    }
}

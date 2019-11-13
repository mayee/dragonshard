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
package net.dragonshard.dsf.core.toolkit;

import java.util.Collection;
import java.util.Map;

/**
 * Collection工具类
 *
 * @author Caratacus
 */
public class CollectionUtils {

  /**
   * 校验集合是否为空
   *
   * @param coll 入参
   * @return boolean
   */
  public static boolean isEmpty(Collection<?> coll) {
    return (coll == null || coll.isEmpty());
  }

  /**
   * 校验集合是否不为空
   *
   * @param coll 入参
   * @return boolean
   */
  public static boolean isNotEmpty(Collection<?> coll) {
    return !isEmpty(coll);
  }

  /**
   * 判断Map是否为空
   *
   * @param map 入参
   * @return boolean
   */
  public static boolean isEmpty(Map<?, ?> map) {
    return (map == null || map.isEmpty());
  }

  /**
   * 判断Map是否不为空
   *
   * @param map 入参
   * @return boolean
   */
  public static boolean isNotEmpty(Map<?, ?> map) {
    return !isEmpty(map);
  }
}

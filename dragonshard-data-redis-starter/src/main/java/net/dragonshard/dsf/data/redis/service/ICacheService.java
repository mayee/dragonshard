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

package net.dragonshard.dsf.data.redis.service;

/**
 * 缓存顶层接口
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-28
 **/
public interface ICacheService {
    /**
     * 设置缓存
     *
     * @param key    缓存键
     * @param value  缓存值
     * @param expSec 失效时间(秒)
     */
    void save(String key, String value, long expSec);

    /**
     * 删除缓存数据
     *
     * @param key 缓存键
     * @return V 泛型
     */
    String delete(String key);

    /**
     * 获取缓存数据,如果关键字不存在返回null
     *
     * @param key 缓存键
     * @return 缓存实体
     */
    String get(String key);

    /**
     * 清空以cachePrefix开头的缓存
     *
     * @param cachePrefix 缓存前缀
     */
    void clear(String cachePrefix);

}

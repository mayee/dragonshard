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
     * @param key 缓存键, 一个值或多个
     */
    void delete(String... key);

    /**
     * 获取缓存数据,如果关键字不存在返回null
     *
     * @param key 缓存键
     * @return 缓存实体
     */
    String get(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    Boolean hasKey(String key);

    /**
     * 清空以cachePrefix开头的缓存
     *
     * @param cachePrefix 缓存前缀
     */
    void clear(String cachePrefix);

    /**
     * 指定缓存失效时间
     *
     * @param key
     *            键
     * @param time
     *            时间(秒)
     * @return true成功 false失败
     */
    Boolean expire(String key, long time);

    /**
     * 根据key 获取过期时间
     *
     * @param key
     *            键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    Long getExpire(String key);
}

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


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.StringUtils;

/**
 * 缓存实现的门面
 *
 * @author mayee
 * @date 2019-06-28
 *
 * @version v1.0
 **/
public class CacheService {
    private static ICacheService cacheService;

    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存value
     * @param expSec   失效时间(秒)
     * @param <V>   泛型
     */
    public static <V> void save(String key, V value, long expSec) {
        cacheService.save(key, JSON.toJSONString(value), expSec);
    }

    /**
     * 删除缓存数据
     *
     * @param key 缓存键
     * @return V 泛型
     */
    public static String delete(String key) {
        String value = cacheService.delete(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, String.class);
    }

    /**
     * 删除缓存数据
     *
     * @param <V>           泛型
     * @param key           缓存键
     * @param typeReference 类型
     * @return V 泛型
     */
    public static <V> V delete(String key, TypeReference typeReference) {
        String value = cacheService.delete(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, typeReference.getType());
    }

    /**
     * 获取缓存对象,返回string
     *
     * @param key 缓存键
     * @return 返回缓存实体
     */
    public static String get(String key) {
        String value = cacheService.get(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, String.class);
    }


    /**
     * 获取缓存对象
     *
     * @param <V>           泛型
     * @param key           缓存键
     * @param typeReference 类型
     * @return 返回缓存实体
     */
    public static <V> V get(String key, TypeReference typeReference) {
        String value = cacheService.get(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, typeReference.getType());
    }

    public static CacheService initCache(ICacheService cacheService) {
        CacheService.cacheService = cacheService;
        return new CacheService();
    }

    /**
     * 清空以cachePrefix开头的缓存
     *
     * @param cachePrefix 缓存前缀
     */
    public static void clear(String cachePrefix) {
        cacheService.clear(cachePrefix);
    }
}

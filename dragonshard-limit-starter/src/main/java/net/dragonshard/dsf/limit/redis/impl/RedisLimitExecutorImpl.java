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

package net.dragonshard.dsf.limit.redis.impl;

import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.KeyUtil;
import net.dragonshard.dsf.limit.aop.LimitExecutor;
import net.dragonshard.dsf.limit.configuration.property.LimitProperties;
import net.dragonshard.dsf.limit.redis.handler.RedisHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RedisLimitExecutorImpl implements LimitExecutor {

    @Autowired
    private RedisHandler redisHandler;
    private LimitProperties limitProperties;
    private RedisScript<Number> redisScript;

    public RedisLimitExecutorImpl(LimitProperties limitProperties) {
        this.limitProperties = limitProperties;
    }

    @PostConstruct
    public void initialize() {
        String luaScript = buildLuaScript();
        redisScript = new DefaultRedisScript<Number>(luaScript, Number.class);
    }

    private String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        // 调用不超过最大值，则直接返回
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        // 执行计算器自加
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");

        return lua.toString();
    }

    @Override
    public boolean tryAccess(String name, String key, int limitPeriod, int limitCount) {
        Assert.hasText(name, "@Limit parameter [ name ] is null or empty");
        Assert.hasText(key, "@Limit parameter [ key ] is null or empty");
        String compositeKey = KeyUtil.getCompositeKey(limitProperties.getPrefix(), name, key);
        return tryAccess(compositeKey, limitPeriod, limitCount);
    }

    @Override
    public boolean tryAccess(String compositeKey, int limitPeriod, int limitCount) {
        Assert.hasText(compositeKey, "@Limit parameter [ composite key ] is null or empty");
        List<String> keys = new ArrayList<String>();
        keys.add(compositeKey);
        StringRedisTemplate redisTemplate = redisHandler.getRedisTemplate();
        Number count = redisTemplate.execute(redisScript, keys, limitCount + "", limitPeriod + "");
        return count.intValue() <= limitCount;
    }
}

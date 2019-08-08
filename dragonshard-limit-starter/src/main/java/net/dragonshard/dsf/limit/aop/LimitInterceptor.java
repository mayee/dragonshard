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

package net.dragonshard.dsf.limit.aop;

import com.nepxion.matrix.proxy.aop.AbstractInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.ExceptionUtils;
import net.dragonshard.dsf.core.toolkit.KeyUtil;
import net.dragonshard.dsf.limit.annotation.Limit;
import net.dragonshard.dsf.limit.configuration.property.LimitProperties;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 拦截器
 *
 * @author mayee
 * @date 2019-07-16
 *
 * @version v1.0
 **/
@Slf4j
public class LimitInterceptor extends AbstractInterceptor {

    @Autowired
    private LimitProperties limitProperties;
    @Resource
    private LimitDelegate limitDelegate;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Limit limitAnnotation = getLimitAnnotation(invocation);
        if (limitAnnotation != null) {
            String name = limitAnnotation.name();
            String key = limitAnnotation.key();
            int limitPeriod = limitAnnotation.limitPeriod();
            int limitCount = limitAnnotation.limitCount();

            return invoke(invocation, limitAnnotation, name, key, limitPeriod, limitCount);
        }

        return invocation.proceed();
    }

    private Limit getLimitAnnotation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(Limit.class)) {
            return method.getAnnotation(Limit.class);
        }

        return null;
    }

    private Object invoke(MethodInvocation invocation, Annotation annotation, String name, String key, int limitPeriod, int limitCount) throws Throwable {
        if (StringUtils.isEmpty(name)) {
            throw ExceptionUtils.get("Annotation [Limit]'s name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw ExceptionUtils.get("Annotation [Limit]'s key is null or empty");
        }

        String spelKey = null;
        try {
            spelKey = getSpelKey(invocation, key);
        } catch (Exception e) {
            spelKey = key;
        }
        String compositeKey = KeyUtil.getCompositeKey(limitProperties.getPrefix(), name, spelKey);

        if (log.isDebugEnabled()) {
            String proxyType = getProxyType(invocation);
            String proxiedClassName = getProxiedClassName(invocation);
            String methodName = getMethodName(invocation);
            log.debug("Intercepted for annotation - Limit [key={}, limitPeriod={}, limitCount={}, proxyType={}, proxiedClass={}, method={}]", compositeKey, limitPeriod, limitCount, proxyType, proxiedClassName, methodName);
        }

        return limitDelegate.invoke(invocation, compositeKey, limitPeriod, limitCount);
    }
}

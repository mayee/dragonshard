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

import com.nepxion.matrix.proxy.aop.DefaultAutoScanProxy;
import com.nepxion.matrix.proxy.mode.ProxyMode;
import com.nepxion.matrix.proxy.mode.ScanMode;
import net.dragonshard.dsf.limit.annotation.Limit;

import java.lang.annotation.Annotation;

/**
 * 包扫描代理
 *
 * @author mayee
 * @date 2019-07-16
 *
 * @version v1.0
 **/
public class LimitAutoScanProxy extends DefaultAutoScanProxy {

    private static final long serialVersionUID = 3354937345406209378L;
    private String[] commonInterceptorNames;

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    public LimitAutoScanProxy(String scanPackages) {
        super(scanPackages, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    @Override
    protected String[] getCommonInterceptorNames() {
        if (commonInterceptorNames == null) {
            commonInterceptorNames = new String[] { "limitInterceptor" };
        }

        return commonInterceptorNames;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        if (methodAnnotations == null) {
            methodAnnotations = new Class[] { Limit.class };
        }

        return methodAnnotations;
    }
}

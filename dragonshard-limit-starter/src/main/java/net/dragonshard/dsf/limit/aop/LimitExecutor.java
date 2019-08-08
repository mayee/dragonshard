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

public interface LimitExecutor {

    /**
     * 在给定的时间段里最多的访问限制次数(超出次数返回false)；等下个时间段开始，才允许再次被访问(返回true)，周而复始
     * @param name 资源名字
     * @param key 资源Key
     * @param limitPeriod 给定的时间段(单位秒)
     * @param limitCount 最多的访问限制次数
     * @return boolean
     */
    boolean tryAccess(String name, String key, int limitPeriod, int limitCount) throws Exception;

    boolean tryAccess(String compositeKey, int limitPeriod, int limitCount) throws Exception;
}

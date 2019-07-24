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


import net.dragonshard.dsf.core.exception.DragonshardException;

/**
 * 异常辅助工具类
 *
 * @author mayee
 * @date 2019-06-01
 *
 * @version v1.0
 **/
public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg 消息
     * @param t   异常信息
     * @return 返回异常
     */
    public static DragonshardException get(String msg, Throwable t, Object... params) {
        return new DragonshardException(StringUtils.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     * @return 返回异常
     */
    public static DragonshardException get(String msg, Object... params) {
        return new DragonshardException(StringUtils.format(msg, params));
    }

    /**
     * 重载的方法
     *
     * @param t 异常
     * @return 返回异常
     */
    public static DragonshardException get(Throwable t) {
        return new DragonshardException(t);
    }

}

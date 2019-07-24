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
package net.dragonshard.dsf.web.core.framework.exception;

import net.dragonshard.dsf.web.core.framework.model.BizErrorCode;

/**
 * 业务异常类
 *
 * @author mayee
 * @date 2019-06-09
 *
 * @version v1.0
 **/
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 6797769538354461365L;
    /**
     * 错误码
     */
    private final BizErrorCode errorCode;

    public BizException(BizErrorCode errorCode) {
        super(errorCode.getError());
        this.errorCode = errorCode;

    }

    public BizErrorCode getErrorCode() {
        return errorCode;
    }

}

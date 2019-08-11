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
package net.dragonshard.dsf.web.core.enums;

import net.dragonshard.dsf.core.exception.UnknownEnumException;
import net.dragonshard.dsf.web.core.framework.model.BizErrorCode;

import javax.servlet.http.HttpServletResponse;

/**
 * 业务异常枚举
 *
 * @author mayee
 * @date 2019-06-06
 *
 * @version v1.0
 **/
public enum DsfErrorCodeEnum {

    /**
     * 400
     */
    BAD_REQUEST(HttpServletResponse.SC_BAD_REQUEST, true, "请求参数错误或不完整"),
    /**
     * JSON格式错误
     */
    JSON_FORMAT_ERROR(HttpServletResponse.SC_BAD_REQUEST, true, "JSON格式错误"),
    /**
     * 401
     */
    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, true, "请先进行认证"),
    /**
     * 403
     */
    FORBIDDEN(HttpServletResponse.SC_FORBIDDEN, true, "无权查看"),
    /**
     * 404
     */
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, true, "未找到该路径"),
    /**
     * 405
     */
    METHOD_NOT_ALLOWED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, true, "请求方式不支持"),
    /**
     * 406
     */
    NOT_ACCEPTABLE(HttpServletResponse.SC_NOT_ACCEPTABLE, true, "不可接受该请求"),
    /**
     * 411
     */
    LENGTH_REQUIRED(HttpServletResponse.SC_LENGTH_REQUIRED, true, "长度受限制"),
    /**
     * 415
     */
    UNSUPPORTED_MEDIA_TYPE(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, true, "不支持的媒体类型"),
    /**
     * 416
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE, true, "不能满足请求的范围"),
    /**
     * 500
     */
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "服务器异常，请稍后再试"),
    /**
     * 503
     */
    SERVICE_UNAVAILABLE(HttpServletResponse.SC_SERVICE_UNAVAILABLE, true, "服务器无法使用，请耐心等待"),


    // --------------- Dragonshard ---------------
    SECRET_DECRYPT_BODY_FAIL(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "请求报文解密失败"),
    SECRET_PROVIDER_FAIL(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "Provider加载失败"),
    SECRET_PROVIDER_NOT_SUPPORT_BASE64(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "RSA的Provider模式加密不支持BASE64格式"),
    UPLOAD_SIGN_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "签名错误"),
    UPLOAD_SIGN_TIME_OUT(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "签名超时"),
    REQUEST_REACH_MAX_LIMIT(HttpServletResponse.SC_SERVICE_UNAVAILABLE, true, "请求达到上限，稍后再试"),
    ;

    private final int httpCode;
    private final boolean show;
    private final String msg;

    DsfErrorCodeEnum(int httpCode, boolean show, String msg) {
        this.httpCode = httpCode;
        this.msg = msg;
        this.show = show;
    }

    /**
     * 转换为ErrorCode(自定义返回消息)
     *
     * @param msg
     * @return
     */
    public BizErrorCode convert(String msg) {
        return BizErrorCode.builder().httpCode(httpCode()).show(show()).error(name()).msg(msg).build();
    }

    /**
     * 转换为ErrorCode
     *
     * @return
     */
    public BizErrorCode convert() {
        return BizErrorCode.builder().httpCode(httpCode()).show(show()).error(name()).msg(msg()).build();
    }

    public static DsfErrorCodeEnum getErrorCode(String errorCode) {
        DsfErrorCodeEnum[] enums = DsfErrorCodeEnum.values();
        for (DsfErrorCodeEnum errorCodeEnum : enums) {
            if (errorCodeEnum.name().equalsIgnoreCase(errorCode)) {
                return errorCodeEnum;
            }
        }
        throw new UnknownEnumException("Error: Unknown errorCode, or do not support changing errorCode!\n");
    }

    public int httpCode() {
        return this.httpCode;
    }

    public String msg() {
        return this.msg;
    }

    public boolean show() {
        return this.show;
    }

}

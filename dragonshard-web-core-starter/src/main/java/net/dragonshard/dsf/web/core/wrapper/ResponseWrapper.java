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
package net.dragonshard.dsf.web.core.wrapper;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import net.dragonshard.dsf.web.core.bean.Result;
import net.dragonshard.dsf.web.core.framework.model.BizErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * response包装类
 *
 * @author Caratacus
 */
@Slf4j
public class ResponseWrapper extends HttpServletResponseWrapper {

    private BizErrorCode errorcode;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public ResponseWrapper(HttpServletResponse response, BizErrorCode errorcode) {
        super(response);
        setErrorCode(errorcode);
    }

    /**
     * 获取ErrorCode
     *
     * @return
     */
    public BizErrorCode getErrorCode() {
        return errorcode;
    }

    /**
     * 设置ErrorCode
     *
     * @param errorCode
     */
    public void setErrorCode(BizErrorCode errorCode) {
        if (Objects.nonNull(errorCode)) {
            this.errorcode = errorCode;
            super.setStatus(this.errorcode.getHttpCode());
        }
    }

    /**
     * 向外输出ApiResponses
     *
     * @param dsfResult
     */
    public void printWriterApiResponses(Result dsfResult) {
        writeValueAsJson(dsfResult);
    }

    /**
     * 向外输出json对象
     *
     * @param obj
     */
    public void writeValueAsJson(Object obj) {
        if (super.isCommitted()) {
            log.warn("Warn: Response isCommitted, Skip the implementation of the method.");
            return;
        }
        super.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        super.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = super.getWriter()) {
            writer.print(JSONObject.toJSONString(obj));
            writer.flush();
        } catch (IOException e) {
            log.warn("Error: Response printJson faild, stackTrace: {}", Throwables.getStackTraceAsString(e));
        }
    }

}

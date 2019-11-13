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
package net.dragonshard.dsf.web.core.bean;

import static net.dragonshard.dsf.web.core.common.WebCoreConstants.LOG.DSF_API_TRACE_ID;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import net.dragonshard.dsf.web.core.framework.model.BizErrorCode;
import net.dragonshard.dsf.web.core.framework.util.ResponseUtils;
import org.springframework.http.HttpStatus;

/**
 * GET: 200 OK POST: 201 Created PUT: 200 OK PATCH: 200 OK DELETE: 204 No Content 接口返回(多态)
 *
 * @author Caratacus
 * @author Mayee
 */
public class Result<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  public static Result<Void> success(HttpStatus status) {
    return SuccessResult.<Void>builder().status(status.value()).build();

  }

  public static <T> Result<T> success(T object) {
    return success(object, HttpStatus.OK);

  }

  public static <T> Result<T> success(T object, HttpStatus status) {
    return SuccessResult.<T>builder().status(status.value()).data(object).build();

  }

  public static Result failure(HttpServletRequest request, BizErrorCode errorCode,
    Exception exception) {
    return ResponseUtils.exceptionMsg(FailedResult.builder().msg(errorCode.getMsg()), exception)
      .traceId((String) request.getAttribute(DSF_API_TRACE_ID))
      .code(errorCode.getError())
      .show(errorCode.isShow())
      .time(LocalDateTime.now())
      .status(errorCode.getHttpCode())
      .build();
  }

}

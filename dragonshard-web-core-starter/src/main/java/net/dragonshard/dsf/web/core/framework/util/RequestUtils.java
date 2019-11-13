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
package net.dragonshard.dsf.web.core.framework.util;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.web.core.enums.HTTPMethod;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

/**
 * Request 请求工具类
 *
 * @author Caratacus
 */
@SuppressWarnings("ALL")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RequestUtils {

  /**
   * 判断请求方式GET
   */
  public static boolean isGet(HttpServletRequest request) {
    return HTTPMethod.GET.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式POST
   */
  public static boolean isPost(HttpServletRequest request) {
    return HTTPMethod.POST.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式PUT
   */
  public static boolean isPut(HttpServletRequest request) {
    return HTTPMethod.PUT.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式DELETE
   */
  public static boolean isDelete(HttpServletRequest request) {
    return HTTPMethod.DELETE.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式PATCH
   */
  public static boolean isPatch(HttpServletRequest request) {
    return HTTPMethod.PATCH.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式TRACE
   */
  public static boolean isTrace(HttpServletRequest request) {
    return HTTPMethod.TRACE.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式HEAD
   */
  public static boolean isHead(HttpServletRequest request) {
    return HTTPMethod.HEAD.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式OPTIONS
   */
  public static boolean isOptions(HttpServletRequest request) {
    return HTTPMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 获取请求
   */
  public static String getRequestBody(HttpServletRequest request) {
    String requestBody = null;
    if (isContainBody(request)) {
      try {
        ServletInputStream inputStream = null;
//                if (request instanceof ShiroHttpServletRequest) {
//                    ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
//                    inputStream = shiroRequest.getRequest().getInputStream();
//                } else {
        inputStream = request.getInputStream();
//                }
        if (Objects.nonNull(inputStream)) {
          StringWriter writer = new StringWriter();
          IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8.name());
          requestBody = writer.toString();
        }
      } catch (IOException ignored) {
      }
    }
    return requestBody;
  }

  /**
   * 获取请求
   */
  public static byte[] getByteBody(HttpServletRequest request) {
    byte[] body = new byte[0];
    try {
      body = StreamUtils.copyToByteArray(request.getInputStream());
    } catch (IOException e) {
      log.error("Error: Get RequestBody byte[] fail," + e);
    }
    return body;
  }

  /**
   * 是否包含请求体
   */
  public static boolean isContainBody(HttpServletRequest request) {
    return isPost(request) || isPut(request) || isPatch(request);
  }

}

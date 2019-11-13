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

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.web.core.framework.log.Log;
import net.dragonshard.dsf.web.core.spring.ApplicationUtils;

/**
 * 请求日志工具类
 *
 * @author Caratacus
 */
@Slf4j
public class LogUtils {

  /**
   * 获取日志对象
   */
  public static void printLog(Long beiginTime,
    String uid,
    Map<String, String[]> parameterMap,
    String requestBody,
    String url,
    String mapping,
    String method,
    String ip,
    String traceId,
    Object object) {
    Log logInfo = Log.builder()
      //查询参数
      .parameterMap(parameterMap)
      .uid(uid)
      //请求体
      .requestBody(parseBody(requestBody))
      //请求路径
      .url(url)
      //请求mapping
      .mapping(mapping)
      //请求方法
      .method(method)
      .runTime((beiginTime != null ? System.currentTimeMillis() - beiginTime : 0) + "ms")
      .result(object)
      .ip(ip)
      .traceId(traceId)
      .build();
    log.info(JSONObject.toJSONString(logInfo));
  }

  public static void doAfterReturning(Object ret) {
    ResponseUtils.writeValAsJson(ApplicationUtils.getRequest(), ret);
  }

  private static Object parseBody(String requestBody) {
    try {
      return Optional.ofNullable(JSONObject.parse(requestBody)).orElse(requestBody);
    } catch (Exception e) {
      return requestBody;
    }
  }
}

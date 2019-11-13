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
package net.dragonshard.dsf.web.core.spring;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Spring Application 工具类
 *
 * @author Caratacus
 */
public class ApplicationUtils {

  /**
   * 全局的ApplicationContext
   */
  private final static ApplicationContext APPLICATION_CONTEXT = ApplicationContextRegister
    .getApplicationContext();

  /**
   * 获取ApplicationContext
   */
  public static ApplicationContext getApplicationContext() {
    return APPLICATION_CONTEXT;
  }

  /**
   * 获取springbean
   */
  public static <T> T getBean(String beanName, Class<T> requiredType) {
    if (containsBean(beanName)) {
      return APPLICATION_CONTEXT.getBean(beanName, requiredType);
    }
    return null;
  }

  /**
   * 获取springbean
   */
  public static <T> T getBean(Class<T> requiredType) {
    return APPLICATION_CONTEXT.getBean(requiredType);
  }

  /**
   * 获取springbean
   */
  public static <T> T getBean(String beanName) {
    if (containsBean(beanName)) {
      Class<T> type = getType(beanName);
      return APPLICATION_CONTEXT.getBean(beanName, type);
    }
    return null;
  }

  /**
   * 依赖spring框架获取HttpServletRequest
   *
   * @return HttpServletRequest
   */
  public static HttpServletRequest getRequest() {
    HttpServletRequest request = null;
    try {
      ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
      if (Objects.nonNull(requestAttributes)) {
        request = requestAttributes.getRequest();
      }
    } catch (Exception ignored) {
    }
    return request;
  }

  /**
   * ApplicationContext是否包含该Bean
   */
  public static boolean containsBean(String name) {
    return APPLICATION_CONTEXT.containsBean(name);
  }

  /**
   * ApplicationContext该Bean是否为单例
   */
  public static boolean isSingleton(String name) {
    return APPLICATION_CONTEXT.isSingleton(name);
  }

  /**
   * 获取该Bean的Class
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getType(String name) {
    return (Class<T>) APPLICATION_CONTEXT.getType(name);
  }

}

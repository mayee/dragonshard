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

package net.dragonshard.dsf.upload.local.common;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * context 工具类
 */
@Component
public class ContextUtil implements ApplicationContextAware {

  private static ApplicationContext APPLICATION_CONTEXT;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    APPLICATION_CONTEXT = applicationContext;
  }

  /**
   * 获取 bean 的类型
   */
  public static <T> List<T> getBeansOfType(Class<T> clazz) {
    Map<String, T> map;
    try {
      map = APPLICATION_CONTEXT.getBeansOfType(clazz);
    } catch (Exception e) {
      map = null;
    }
    return map == null ? null : new ArrayList<>(map.values());
  }


  /**
   * 获取所有被注解的 bean
   */
  public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> anno) {
    Map<String, Object> map;
    try {
      map = APPLICATION_CONTEXT.getBeansWithAnnotation(anno);
    } catch (Exception e) {
      map = null;
    }
    return map;
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

  public static boolean containsBean(String name) {
    return APPLICATION_CONTEXT.containsBean(name);
  }

  /**
   * 获取该Bean的Class
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getType(String name) {
    return (Class<T>) APPLICATION_CONTEXT.getType(name);
  }
}

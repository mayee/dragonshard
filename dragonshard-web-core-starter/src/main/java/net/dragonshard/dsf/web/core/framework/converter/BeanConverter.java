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
package net.dragonshard.dsf.web.core.framework.converter;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.dragonshard.dsf.core.toolkit.CollectionUtils;
import net.dragonshard.dsf.web.core.framework.modelmapper.jdk8.Jdk8Module;
import net.dragonshard.dsf.web.core.framework.modelmapper.jsr310.Jsr310Module;
import net.dragonshard.dsf.web.core.framework.modelmapper.jsr310.Jsr310ModuleConfig;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.objenesis.instantiator.util.ClassUtils;

/**
 * 转换工具类
 *
 * @author Caratacus
 */
public class BeanConverter {

  private static final ModelMapper MODEL_MAPPER;

  static {
    MODEL_MAPPER = new ModelMapper();
    Jsr310ModuleConfig config = Jsr310ModuleConfig.builder()
      .dateTimePattern("yyyy-MM-dd HH:mm:ss.SSS")
      .datePattern("yyyy-MM-dd")
      .zoneId(ZoneOffset.UTC)
      .build();
    MODEL_MAPPER.registerModule(new Jsr310Module(config)).registerModule(new Jdk8Module());
    MODEL_MAPPER.getConfiguration().setFullTypeMatchingRequired(true);
    MODEL_MAPPER.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
  }

  /**
   * 获取 modelMapper
   */
  public static ModelMapper getModelMapper() {
    return MODEL_MAPPER;
  }

  /**
   * Bean转换为Map
   */
  public static <T> Map<String, Object> beanToMap(T bean) {
    Map<String, Object> map = Collections.emptyMap();
    if (null != bean) {
      BeanMap beanMap = BeanMap.create(bean);
      map = new HashMap<>(beanMap.keySet().size());
      for (Object key : beanMap.keySet()) {
        map.put(String.valueOf(key), beanMap.get(key));
      }
    }
    return map;
  }

  /**
   * List<E>转换为List<Map<String, Object>>
   */
  public static <T> List<Map<String, Object>> beansToMap(List<T> objList) {
    List<Map<String, Object>> list = Collections.emptyList();
    if (CollectionUtils.isNotEmpty(objList)) {
      list = new ArrayList<>(objList.size());
      Map<String, Object> map;
      T bean;
      for (T anObjList : objList) {
        bean = anObjList;
        map = beanToMap(bean);
        list.add(map);
      }
    }
    return list;
  }

  /**
   * map转为bean
   *
   * @param <T> the type parameter
   * @param mapList the map list
   * @param beanClass the bean class
   * @return t list
   */
  public static <T> List<T> mapToBean(List<Map<String, Object>> mapList, Class<T> beanClass) {
    List<T> list = Collections.emptyList();
    if (CollectionUtils.isNotEmpty(mapList)) {
      list = new ArrayList<>(mapList.size());
      Map<String, Object> map;
      T bean;
      for (Map<String, Object> map1 : mapList) {
        map = map1;
        bean = mapToBean(map, beanClass);
        list.add(bean);
      }
    }
    return list;
  }

  /**
   * map转为bean
   *
   * @param map the map
   * @param beanClass the bean class
   * @return t
   */
  public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
    T entity = ClassUtils.newInstance(beanClass);
    BeanMap beanMap = BeanMap.create(entity);
    beanMap.putAll(map);
    return entity;
  }

  /**
   * 列表转换
   *
   * @param clazz the clazz
   * @param list the list
   */
  public static <T> List<T> convert(Class<T> clazz, List<?> list) {
    return CollectionUtils.isEmpty(list) ? Collections.emptyList()
      : list.stream().map(e -> convert(clazz, e)).collect(Collectors.toList());
  }

  /**
   * 单个对象转换
   *
   * @param targetClass 目标对象
   * @param source 源对象
   * @return 转换后的目标对象
   */
  public static <T> T convert(Class<T> targetClass, Object source) {
    return getModelMapper().map(source, targetClass);
  }

}

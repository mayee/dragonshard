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

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.BeanUtils;
import net.dragonshard.dsf.core.toolkit.StringPool;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

/**
 * 工具类
 *
 * @author mayee
 **/
@Slf4j
public class UploadLocalUtils {

  public static final int MILLIS = 1000;
  private static final String IMAGE_FORMAT_JPG = "jpg";
  private static final String IMAGE_FORMAT_JPEG = "jpeg";
  private static final String IMAGE_FORMAT_PNG = "png";

  /**
   * 生成md5
   *
   * @param str 要加密的内容
   * @return 加密后内容
   */
  public static String md5(String str) {
    return DigestUtils.md5DigestAsHex(str.getBytes());
  }

  /**
   * 排序签名
   *
   * @param o 要签名的对象
   * @param <T> 泛型
   * @param secret 秘钥
   * @return 加密后字符串
   */
  public static <T> String signWithSort(T o, String secret) {
    Map<String, Object> map = BeanUtils.beanToMap(o);
    String signStr = map.keySet().stream().sorted()
      .map(key -> {
        Object value = map.get(key);
        if (value == null) {
          return null;
        }
        return key.concat(StringPool.EQUALS).concat(String.valueOf(value));
      }).filter(Objects::nonNull)
      .collect(Collectors.joining(StringPool.AMPERSAND));
    log.debug("SignWithSort > {}", signStr);
    return md5(signStr + secret);
  }

  /**
   * 获取文件名后缀，携带小数点
   *
   * @param fileName 文件名称
   * @return 后缀名称
   */
  public static String fileSuffixWithPoint(String fileName) {
    if (StringUtils.isEmpty(fileName)) {
      return "";
    }
    int suffixIndex = fileName.lastIndexOf(StringPool.DOT);
    if (suffixIndex > -1) {
      return fileName.substring(suffixIndex);
    }
    return "";
  }

  /**
   * 获取文件名后缀，不携带小数点
   *
   * @param fileName 文件名称
   * @return 后缀名称
   */
  public static String fileSuffix(String fileName) {
    if (StringUtils.isEmpty(fileName)) {
      return "";
    }
    int suffixIndex = fileName.lastIndexOf(StringPool.DOT);
    if (suffixIndex > -1) {
      return fileName.substring(suffixIndex + 1);
    }
    return "";
  }

  /**
   * 判断图片格式是否可以被 tinypng 进行压缩
   *
   * @param fileName 文件名
   * @return true 可以，false 不可以
   */
  public static boolean validCompressWithTinypng(String fileName){
    String fileFormatLowerCase = fileSuffix(fileName).toLowerCase();
    return IMAGE_FORMAT_JPG.equals(fileFormatLowerCase)
      || IMAGE_FORMAT_JPEG.equals(fileFormatLowerCase)
      || IMAGE_FORMAT_PNG.equals(fileFormatLowerCase);
  }

}

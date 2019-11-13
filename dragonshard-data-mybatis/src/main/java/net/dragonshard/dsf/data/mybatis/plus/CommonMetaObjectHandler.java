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
package net.dragonshard.dsf.data.mybatis.plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 通用填充类
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-06
 **/
public class CommonMetaObjectHandler implements MetaObjectHandler {

  /**
   * 创建时间
   */
  private final String createTime = "createTime";
  /**
   * 修改时间
   */
  private final String modifiedTime = "modifiedTime";

  @Override
  public void insertFill(MetaObject metaObject) {
    setInsertFieldValByName(createTime, LocalDateTime.now(), metaObject);
    setInsertFieldValByName(modifiedTime, LocalDateTime.now(), metaObject);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    setUpdateFieldValByName(modifiedTime, LocalDateTime.now(), metaObject);
  }


}

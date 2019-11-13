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
package net.dragonshard.dsf.data.mybatis.common.cons;

/**
 * PAGE 常量
 *
 * @author mayee
 * @version v1.0
 * @date 2019-07-19
 **/
public interface PageCons {

  /**
   * 页数
   */
  String PAGE_PAGE_NUM = "pageNum";
  /**
   * 分页大小
   */
  String PAGE_PAGE_SIZE = "pageSize";
  /**
   * 排序字段 ASC
   */
  String PAGE_ASCS = "ascs";
  /**
   * 排序字段 DESC
   */
  String PAGE_DESCS = "descs";
  /**
   * 是否开启查询分页
   */
  String OPEN_PAGING = "openPaging";
  /**
   * 默认每页条目10,最大条目数50
   */
  int DEFAULT_PAGE_SIZE = 10;
  int MAX_PAGE_SIZE = 50;

}

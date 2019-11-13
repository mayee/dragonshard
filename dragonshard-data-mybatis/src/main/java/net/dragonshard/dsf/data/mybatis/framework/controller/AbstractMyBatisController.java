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
package net.dragonshard.dsf.data.mybatis.framework.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.dragonshard.dsf.web.core.framework.controller.WebController;

/**
 * 扩展自 WebController
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-07
 **/
public abstract class AbstractMyBatisController extends WebController {

  public abstract <T> Page<T> getPage();

  public abstract <T> Page<T> getPage(boolean openSort);

}

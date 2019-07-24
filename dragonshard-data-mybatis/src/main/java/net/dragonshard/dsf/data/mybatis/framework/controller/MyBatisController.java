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

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.dragonshard.dsf.core.toolkit.TypeUtils;
import net.dragonshard.dsf.data.mybatis.common.AntiSQLFilter;
import net.dragonshard.dsf.data.mybatis.common.cons.PageCons;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用的 MyBatisController
 *
 * @author mayee
 * @date 2019-06-07
 *
 * @version v1.0
 **/
public class MyBatisController extends AbstractMyBatisController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    /**
     * 获取分页对象
     *
     * @return
     */
    @Override
    public <T> Page<T> getPage() {
        return getPage(false);
    }

    /**
     * 获取分页对象
     *
     * @param openSort
     * @return
     */
    @Override
    public <T> Page<T> getPage(boolean openSort) {
        int index = 1;
        // 页数
        Integer pageNum = TypeUtils.castToInt(request.getParameter(PageCons.PAGE_PAGE_NUM), index);
        // 分页大小
        Integer pageSize = TypeUtils.castToInt(request.getParameter(PageCons.PAGE_PAGE_SIZE), PageCons.DEFAULT_PAGE_SIZE);
        // 是否查询分页
        Boolean openPaging = TypeUtils.castToBoolean(request.getParameter(PageCons.OPEN_PAGING), true);
        pageSize = (pageSize > PageCons.MAX_PAGE_SIZE) ? PageCons.MAX_PAGE_SIZE : pageSize;
        Page<T> page = new Page<>(pageNum, pageSize, openPaging);
        if (openSort) {
            String[] ascs = getParameterSafeValues(PageCons.PAGE_ASCS);
            for(String name : ascs){
                page.addOrder(OrderItem.asc(name));
            }
            String[] descs = getParameterSafeValues(PageCons.PAGE_DESCS);
            for(String name : descs){
                page.addOrder(OrderItem.desc(name));
            }
        }
        return page;
    }

    /**
     * 获取安全参数(SQL ORDER BY 过滤)
     *
     * @param parameter
     * @return
     */
    private String[] getParameterSafeValues(String parameter) {
        return AntiSQLFilter.getSafeValues(request.getParameterValues(parameter));
    }
}

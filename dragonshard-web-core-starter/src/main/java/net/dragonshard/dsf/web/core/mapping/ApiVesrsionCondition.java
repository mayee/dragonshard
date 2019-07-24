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

package net.dragonshard.dsf.web.core.mapping;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本条件
 *
 * @author chi.zhang
 * @author 2018-06-11 上午10:44
 * @version v1.0
 **/
public class ApiVesrsionCondition implements RequestCondition<ApiVesrsionCondition> {

    private int apiVersion;

    /**
     * 路径中版本的前缀， 这里用 /v[1-9]/的形式
     */
    private static final Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d+)/");

    private int getApiVersion() {
        return this.apiVersion;
    }

    public ApiVesrsionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVesrsionCondition combine(@NonNull ApiVesrsionCondition other) {
        return new ApiVesrsionCondition(other.getApiVersion());
    }

    @Override
    public ApiVesrsionCondition getMatchingCondition(@NonNull HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getServletPath());
        if (m.find()) {
            Integer version = Integer.valueOf(m.group(1));
            if (version < this.apiVersion) {
                return null;
            }
        }
        return this;
    }

    @Override
    public int compareTo(@NonNull ApiVesrsionCondition other, @NonNull HttpServletRequest request) {
        int i = other.getApiVersion() - this.apiVersion;
        if (i == 0) {
            return 0;
        } else {
            return i > 0 ? 1 : -1;
        }
    }

}

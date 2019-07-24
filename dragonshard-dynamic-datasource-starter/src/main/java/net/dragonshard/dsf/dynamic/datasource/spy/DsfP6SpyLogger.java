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

package net.dragonshard.dsf.dynamic.datasource.spy;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import net.dragonshard.dsf.core.toolkit.StringUtils;

/**
 * P6spy SQL 打印策略
 *
 * @author mayee
 * @version v1.0
 **/
public class DsfP6SpyLogger implements MessageFormattingStrategy {

    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, final String sql, final String url) {
        return StringUtils.isNotEmpty(sql) ? "P6SPY Consume Time > " + elapsed + " ms " +
                "\nP6SPY Execute SQL  > " + P6Util.singleLine(sql) + "\n" : null;
    }


}

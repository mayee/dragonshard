/*
 *   Copyright 1999-2018 zhangchi.dev Holding Ltd.
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

package net.dragonshard.dsf.core.exception;

/**
 * 异常
 *
 * @author mayee
 * @version v1.0
 * @date 2018/11/15
 **/
public class DragonshardException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DragonshardException(String message) {
        super(message);
    }

    public DragonshardException(Throwable throwable) {
        super(throwable);
    }

    public DragonshardException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

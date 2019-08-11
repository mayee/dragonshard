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
package net.dragonshard.dsf.web.core.framework.controller;

import net.dragonshard.dsf.web.core.bean.Result;
import net.dragonshard.dsf.web.core.bean.SuccessResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 基础web controller的实现
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-07
 **/
public class WebController extends AbstractWebController {

    @Override
    public <T> ResponseEntity<Result<T>> success(T object) {
        return success(object, null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity success() {
        return success(HttpStatus.OK);
    }

    @Override
    public <T> ResponseEntity<Result<T>> success(T object, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<Result<T>>(
                SuccessResult.<T>builder().data(object).status(status.value()).build(), headers, status);
    }

    @Override
    public <T> ResponseEntity<Result<T>> success(HttpStatus status) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<Result<T>>(
                SuccessResult.<T>builder().status(status.value()).build(), httpHeaders, status);
    }

}

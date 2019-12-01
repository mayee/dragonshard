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

package net.dragonshard.dsf.upload.local.framework.service.impl;

import net.dragonshard.dsf.upload.local.configuration.properties.UploadLocalProperties;
import net.dragonshard.dsf.upload.local.framework.service.base.AbstractUploadLocalService;
import net.dragonshard.dsf.upload.local.tinypng.service.IAsyncCompressService;

/**
 * 文件上传实现
 *
 * @author mayee
 **/
public class SampleUploadServiceImpl extends AbstractUploadLocalService {

  public SampleUploadServiceImpl(
    UploadLocalProperties uploadLocalProperties, IAsyncCompressService asyncCompressService) {
    super(uploadLocalProperties, asyncCompressService);
  }

}

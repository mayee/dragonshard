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

package net.dragonshard.dsf.upload.local.configuration.framework.service;

import java.io.IOException;
import java.io.InputStream;
import net.dragonshard.dsf.upload.local.configuration.common.model.UploadRequest;
import net.dragonshard.dsf.upload.local.configuration.common.model.UploadResult;
import net.dragonshard.dsf.upload.local.configuration.common.model.UploadToken;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传顶层接口
 *
 * @author mayee
 * @version v1.0
 **/
public interface IUploadLocalService {

  /**
   * 预上传，获取token
   *
   * @param uploadRequest 上传请求实体
   * @return 签名
   */
  UploadToken preload(UploadRequest uploadRequest);

  /**
   * 通过multipartFile上传文件，有token
   *
   * @param uploadFile 文件
   * @param uploadRequest 请求实体
   * @param token 签名字符串
   * @return 上传成功实体
   * @throws IOException ioexception
   */
  UploadResult upload(MultipartFile uploadFile, UploadRequest uploadRequest, String token)
    throws IOException;

  /**
   * 通过stream上传文件，有token
   *
   * @param uploadStream 上传的stream流
   * @param uploadRequest 请求实体
   * @param token 签名字符串
   * @return 上传成功实体
   * @throws IOException ioexception
   */
  UploadResult upload(InputStream uploadStream, UploadRequest uploadRequest, String token)
    throws IOException;

  /**
   * 通过字节数组上传文件，有token
   *
   * @param uploadByte 上传的字节数组
   * @param uploadRequest 请求实体
   * @param token 签名字符串
   * @return 上传成功实体
   * @throws IOException ioexception
   */
  UploadResult upload(byte[] uploadByte, UploadRequest uploadRequest, String token)
    throws IOException;

  /**
   * 预览、下载上传文件
   *
   * @param fileName 文件名称
   * @return 字节流
   */
  byte[] files(String fileName);
}

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

package net.dragonshard.dsf.upload.local.framework.service.base;

import static net.dragonshard.dsf.upload.local.common.UploadLocalUtils.MILLIS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.core.toolkit.TimeUtils;
import net.dragonshard.dsf.upload.local.common.UploadLocalUtils;
import net.dragonshard.dsf.upload.local.common.model.UploadRequest;
import net.dragonshard.dsf.upload.local.common.model.UploadResult;
import net.dragonshard.dsf.upload.local.common.model.UploadToken;
import net.dragonshard.dsf.upload.local.configuration.properties.UploadLocalProperties;
import net.dragonshard.dsf.upload.local.exception.UploadTokenTimeoutException;
import net.dragonshard.dsf.upload.local.exception.UploadTokenValidException;
import net.dragonshard.dsf.upload.local.framework.service.IUploadLocalService;
import net.dragonshard.dsf.upload.local.tinypng.service.IAsyncCompressService;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 抽象骨架
 *
 * @author mayee
 **/
@Slf4j
public abstract class AbstractUploadLocalService implements IUploadLocalService {
  private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter
    .ofPattern("yyyyMMddHHmmssSS");
  private UploadLocalProperties uploadLocalConfig;
  private IAsyncCompressService asyncCompressService;

  public AbstractUploadLocalService(
    UploadLocalProperties uploadLocalProperties, IAsyncCompressService asyncCompressService) {
    this.uploadLocalConfig = uploadLocalProperties;
    this.asyncCompressService = asyncCompressService;
  }

  @Override
  public UploadToken preload(UploadRequest uploadRequest) {
    long timestamp = TimeUtils.getCurrentMillisecond();
    uploadRequest.setTimestamp(timestamp);
    return new UploadToken(UploadLocalUtils.signWithSort(uploadRequest,
      uploadLocalConfig.getSignature().getSecretKey()), timestamp);
  }

  @Override
  public UploadResult upload(InputStream uploadStream, UploadRequest uploadRequest, String token)
    throws IOException {
    //验证签名
    signValid(uploadRequest, token);
    String fileName = getFileName(uploadRequest);
    File file = new File(uploadLocalConfig.getFile().getDir(), fileName);
    //验证父目录是否存在
    if (!parentDirValid(file)) {
      return new UploadResult();
    }
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(StreamUtils.copyToByteArray(uploadStream));
    }
    return uploadSuccess(fileName);
  }

  @Override
  public UploadResult upload(byte[] uploadByte, UploadRequest uploadRequest, String token)
    throws IOException {
    //验证签名
    signValid(uploadRequest, token);
    String fileName = getFileName(uploadRequest);
    File file = new File(uploadLocalConfig.getFile().getDir(), fileName);
    //验证父目录是否存在
    if (!parentDirValid(file)) {
      return new UploadResult();
    }
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(uploadByte);
    }
    return uploadSuccess(fileName);
  }

  @Override
  public UploadResult upload(MultipartFile multipartFile, UploadRequest uploadRequest, String token)
    throws IOException {
    //验证签名
    signValid(uploadRequest, token);
    String fileName = getFileName(uploadRequest) + UploadLocalUtils
      .fileSuffixWithPoint(multipartFile.getOriginalFilename());
    File file = new File(uploadLocalConfig.getFile().getDir(), fileName);
    //验证父目录是否存在
    if (!parentDirValid(file)) {
      return new UploadResult();
    }
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(multipartFile.getBytes());
      asyncCompress(file.getPath());
    }
    return uploadSuccess(fileName);
  }

  @Override
  public byte[] files(String fileName) {
    try {
      return Files.readAllBytes(Paths.get(uploadLocalConfig.getFile().getDir(), fileName));
    } catch (IOException e) {
      return new byte[]{};
    }
  }

  /**
   * 异步压缩
   *
   * @param filePath 文件路径
   */
  private void asyncCompress(String filePath) {
    if (uploadLocalConfig.getCompress().isEnabled()) {
      if (asyncCompressService == null) {
        log.warn("[DSE-UL-1]No compression services available -> {}", filePath);
      } else {
        asyncCompressService.compress(filePath);
      }
    }
  }

  /**
   * 验证父目录是否存在
   */
  private boolean parentDirValid(File file) {
    if (file.getParentFile().exists()) {
      return true;
    } else {
      return file.getParentFile().mkdirs();
    }
  }

  private UploadResult uploadSuccess(String fileName) {
    UploadResult uploadResult = new UploadResult();
    uploadResult.setUrl(uploadLocalConfig.getUrlPrefix() + fileName);
    uploadResult.setFileName(fileName);
    return uploadResult;
  }

  /**
   * 根据上传参数获取文件名称
   *
   * @param uploadRequest 上传参数
   * @return 文件名
   */
  private String getFileName(UploadRequest uploadRequest) {
    String fileName = StringUtils.isEmpty(uploadRequest.getFileName()) ? LocalDateTime.now()
      .format(FILE_NAME_FORMATTER) : uploadRequest.getFileName();
    //如果不覆盖,在文件后增加时间后缀
    if (uploadRequest.getIsCover() == 0) {
      fileName = fileName.concat(LocalDateTime.now().format(FILE_NAME_FORMATTER));
    }
    return fileName;
  }

  /**
   * 验证签名
   *
   * @param uploadRequest 上传请求
   * @param token token
   */
  private void signValid(UploadRequest uploadRequest, String token) {
    if (!uploadLocalConfig.getSignature().getEnabled()) {
      return;
    } else if (token == null) {
      throw new UploadTokenValidException("[DSE-UL-5]Signature Token required!");
    }
    if (System.currentTimeMillis() - uploadRequest.getTimestamp()
      > uploadLocalConfig.getSignature().getTimeoutSecond() * MILLIS) {
      throw new UploadTokenTimeoutException("[DSE-UL-6]Signature timed out, please regenerate!");
    }
    boolean success = UploadLocalUtils.signWithSort(
      uploadRequest, uploadLocalConfig.getSignature().getSecretKey()).equals(token);
    if (!success) {
      throw new UploadTokenValidException("[DSE-UL-7]Signature error!");
    }
  }

}

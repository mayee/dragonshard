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

package net.dragonshard.dsf.id.generator.zookeeper.entity;

public enum RetryTypeEnum {

  /**
   *
   */
  EXPONENTIAL_BACKOFF_RETRY("exponentialBackoffRetry"),
  BOUNDED_EXPONENTIAL_BACKOFF_RETRY("boundedExponentialBackoffRetry"),
  RETRY_NTIMES("retryNTimes"),
  RETRY_FOREVER("retryForever"),
  RETRY_UNTIL_ELAPSED("retryUntilElapsed");

  private String value;

  private RetryTypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static RetryTypeEnum fromString(String value) {
    for (RetryTypeEnum type : RetryTypeEnum.values()) {
      if (type.getValue().equalsIgnoreCase(value.trim())) {
        return type;
      }
    }

    throw new IllegalArgumentException("Mismatched type with value=" + value);
  }

  @Override
  public String toString() {
    return value;
  }
}

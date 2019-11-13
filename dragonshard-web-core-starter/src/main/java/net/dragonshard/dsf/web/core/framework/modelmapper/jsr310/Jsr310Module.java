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
package net.dragonshard.dsf.web.core.framework.modelmapper.jsr310;

import org.modelmapper.ModelMapper;
import org.modelmapper.Module;

/**
 * Supports the JSR310 {@code java.time} objects with  ModelMapper
 *
 * @author Chun Han Hsiao
 */
public class Jsr310Module implements Module {

  private final Jsr310ModuleConfig config;

  public Jsr310Module() {
    this(new Jsr310ModuleConfig());
  }

  public Jsr310Module(Jsr310ModuleConfig config) {
    this.config = config;
  }

  @Override
  public void setupModule(ModelMapper modelMapper) {
    modelMapper.getConfiguration().getConverters().add(0, new FromTemporalConverter(config));
    modelMapper.getConfiguration().getConverters().add(0, new ToTemporalConverter(config));
    modelMapper.getConfiguration().getConverters().add(0, new TemporalToTemporalConverter());
  }
}

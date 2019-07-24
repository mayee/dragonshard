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

package net.dragonshard.dsf.data.mybatis.generate;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.*;

/**
 * 基于Mybatis-plus代码生成器
 *
 * @author mayee
 * @date 2019-07-19
 *
 * @version v1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class MybatisCodeGenerator {

    private DataSourceConfig dataSourceConfig;

    private GlobalConfig globalConfig;

    private TemplateConfig templateConfig;

    private StrategyConfig strategyConfig;

    private PackageConfig packageConfig;

    private InjectionConfig injectionConfig = buildInjectionConfig();

    public MybatisCodeGenerator() {
        strategyConfig = new StrategyConfig()
                //自定义实体父类
                .setSuperEntityClass("dev.zhangchi.dsf.web.core.framework.model.BaseModel")
                // 自定义实体，公共字段
                .setSuperEntityColumns("id")
                // 自定义 controller 父类
                .setSuperControllerClass("dev.zhangchi.dsf.data.mybatis.framework.controller.MyBatisController")
                // 【实体】是否为lombok模型（默认 false）
                .setEntityLombokModel(true)
                // Boolean类型字段是否移除is前缀处理
                .setEntityBooleanColumnRemoveIsPrefix(true)
                .setRestControllerStyle(true)
                .setNaming(NamingStrategy.underline_to_camel)
                // 是否生成实体时，生成字段注解
                .setEntityTableFieldAnnotationEnable(true);

        dataSourceConfig = new DataSourceConfig()
                .setTypeConvert(new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        if ("bit".equals(fieldType.toLowerCase())) {
                            return DbColumnType.BOOLEAN;
                        }
                        if ("tinyint".equals(fieldType.toLowerCase())) {
                            return DbColumnType.BOOLEAN;
                        }
                        if ("date".equals(fieldType.toLowerCase())) {
                            return DbColumnType.LOCAL_DATE;
                        }
                        if ("time".equals(fieldType.toLowerCase())) {
                            return DbColumnType.LOCAL_TIME;
                        }
                        if ("datetime".equals(fieldType.toLowerCase())) {
                            return DbColumnType.LOCAL_DATE_TIME;
                        }
                        return super.processTypeConvert(globalConfig, fieldType);
                    }
                });

        packageConfig = new PackageConfig()
                .setController("controller")
                .setEntity("entity")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl");

        templateConfig = new TemplateConfig()
                .setXml(null);

        globalConfig = new GlobalConfig()
                //输出目录
                .setOutputDir(getJavaPath())
                .setBaseResultMap(false)
                // XML columList
                .setBaseColumnList(false)
                .setOpen(false)
                .setAuthor("Dragonshard");

    }

    private InjectionConfig buildInjectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                this.setMap(map);
            }
        }.setFileOutConfigList(Collections.<FileOutConfig>singletonList(new FileOutConfig(
                "/templates/mapper.xml.vm") {
            // 自定义输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return getResourcePath() + "/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        }));
    }

    /**
     * 获取根目录
     *
     * @return
     */
    private String getRootPath() {
        String file = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getFile();
        return new File(file).getParentFile().getParent();
    }

    /**
     * 获取JAVA目录
     *
     * @return
     */
    private String getJavaPath() {
        String javaPath = getRootPath() + "/src/main/java";
        System.err.println(" Generator Java Path:【 " + javaPath + " 】");
        return javaPath;
    }

    /**
     * 获取Resource目录
     *
     * @return
     */
    private String getResourcePath() {
        String resourcePath = getRootPath() + "/src/main/resources";
        System.err.println(" Generator Resource Path:【 " + resourcePath + " 】");
        return resourcePath;
    }

    /**
     * 获取TableFill策略
     *
     * @return
     */
    private List<TableFill> getTableFills() {
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("createTime", FieldFill.INSERT));
        tableFillList.add(new TableFill("modifiedTime", FieldFill.INSERT_UPDATE));
        tableFillList.add(new TableFill("createUid", FieldFill.INSERT));
        tableFillList.add(new TableFill("modifiedUid", FieldFill.INSERT_UPDATE));
        return tableFillList;
    }

    public void execute() {
        new AutoGenerator()
                // 全局配置
                .setGlobalConfig(getGlobalConfig())
                // 数据源配置
                .setDataSource(getDataSourceConfig())
                // 策略配置
                .setStrategy(getStrategyConfig())
                // 包配置
                .setPackageInfo(getPackageConfig())
                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                .setCfg(getInjectionConfig())
                .setTemplate(getTemplateConfig()).execute();

        System.err.println(" TableName【 " + String.join(",", getStrategyConfig().getInclude()) + " 】" + "Generator Success !");
    }

}

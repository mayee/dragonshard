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

package net.dragonshard.dsf.id.generator.zookeeper.impl;

import net.dragonshard.dsf.core.toolkit.ExceptionUtils;
import net.dragonshard.dsf.core.toolkit.KeyUtil;
import net.dragonshard.dsf.id.generator.configuration.property.ZookeeperIdGeneratorProperties;
import net.dragonshard.dsf.id.generator.zookeeper.ZookeeperIdGenerator;
import net.dragonshard.dsf.id.generator.zookeeper.handler.CuratorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;

@Slf4j
public class ZookeeperIdGeneratorImpl implements ZookeeperIdGenerator {
    private static final int MAX_BATCH_COUNT = 1000;

    @Autowired
    private CuratorHandler curatorHandler;
    @Autowired
    private ZookeeperIdGeneratorProperties zookeeperIdGeneratorProperties;

    @PreDestroy
    public void destroy() {
        try {
            curatorHandler.close();
        } catch (Exception e) {
            throw ExceptionUtils.get("Close Curator failed", e);
        }
    }

    @Override
    public String nextSequenceId(String name, String key) throws Exception {
        if (StringUtils.isEmpty(name)) {
            throw ExceptionUtils.get("name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw ExceptionUtils.get("key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(zookeeperIdGeneratorProperties.getPrefix(), name, key);

        return nextSequenceId(compositeKey);
    }

    @Override
    public String nextSequenceId(String compositeKey) throws Exception {
        if (StringUtils.isEmpty(compositeKey)) {
            throw ExceptionUtils.get("Composite key is null or empty");
        }

        curatorHandler.validateStartedStatus();

        String path = curatorHandler.getPath(zookeeperIdGeneratorProperties.getPrefix(), compositeKey);

        // 并发过快，这里会抛“节点已经存在”的错误，当节点存在时候，就不会创建，所以不必打印异常
        try {
            if (!curatorHandler.pathExist(path)) {
                curatorHandler.createPath(path, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            // do nothing
        }

        CuratorFramework curator = curatorHandler.getCurator();
        int nextSequenceId = curator.setData().withVersion(-1).forPath(path, "".getBytes()).getVersion();

        return String.valueOf(nextSequenceId);
    }

    @Override
    public String[] nextSequenceIds(String name, String key, int count) throws Exception {
        if (count <= 0 || count > MAX_BATCH_COUNT) {
            throw ExceptionUtils.get(String.format("Count can't be greater than %d or less than 0", MAX_BATCH_COUNT));
        }

        String[] nextSequenceIds = new String[count];
        for (int i = 0; i < count; i++) {
            nextSequenceIds[i] = nextSequenceId(name, key);
        }

        return nextSequenceIds;
    }

    @Override
    public String[] nextSequenceIds(String compositeKey, int count) throws Exception {
        if (count <= 0 || count > MAX_BATCH_COUNT) {
            throw ExceptionUtils.get(String.format("Count can't be greater than %d or less than 0", MAX_BATCH_COUNT));
        }

        String[] nextSequenceIds = new String[count];
        for (int i = 0; i < count; i++) {
            nextSequenceIds[i] = nextSequenceId(compositeKey);
        }

        return nextSequenceIds;
    }
}

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

package net.dragonshard.dsf.id.generator.zookeeper.handler;

import net.dragonshard.dsf.id.generator.configuration.property.ZookeeperIdGeneratorProperties;
import net.dragonshard.dsf.id.generator.zookeeper.entity.RetryTypeEnum;
import net.dragonshard.dsf.id.generator.zookeeper.exception.CuratorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.*;
import org.apache.curator.utils.PathUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CuratorHandlerImpl implements CuratorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CuratorHandlerImpl.class);

    @Autowired
    private ZookeeperIdGeneratorProperties zookeeperIdGeneratorProperties;

    private CuratorFramework curator;

    // 创建Curator，并初始化根节点
    @PostConstruct
    private void initialize() throws Exception {
        try {
            create();

            String rootPath = getRootPath(zookeeperIdGeneratorProperties.getPrefix());
            if (!pathExist(rootPath)) {
                createPath(rootPath, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            LOG.error("Initialize Curator failed", e);

            throw e;
        }
    }

    // 创建Curator
    private void create() throws Exception {
        if (StringUtils.isEmpty(zookeeperIdGeneratorProperties.getCurator().getConnectString())) {
            throw new CuratorException(zookeeperIdGeneratorProperties.getCurator().getConnectString() + " can't be null or empty");
        }

        RetryPolicy retryPolicy = null;
        RetryTypeEnum retryTypeEnum = RetryTypeEnum.fromString(zookeeperIdGeneratorProperties.getCurator().getRetryType());
        switch (retryTypeEnum) {
            case EXPONENTIAL_BACKOFF_RETRY: {
                retryPolicy = createExponentialBackoffRetry(
                        zookeeperIdGeneratorProperties.getCurator().getExponentialBackoffRetry().getBaseSleepTimeMs(),
                        zookeeperIdGeneratorProperties.getCurator().getExponentialBackoffRetry().getMaxRetries()
                );
                break;
            }
            case BOUNDED_EXPONENTIAL_BACKOFF_RETRY: {
                retryPolicy = createBoundedExponentialBackoffRetry(
                        zookeeperIdGeneratorProperties.getCurator().getBoundedExponentialBackoffRetry().getBaseSleepTimeMs(),
                        zookeeperIdGeneratorProperties.getCurator().getBoundedExponentialBackoffRetry().getMaxSleepTimeMs(),
                        zookeeperIdGeneratorProperties.getCurator().getBoundedExponentialBackoffRetry().getMaxRetries()
                );
                break;
            }
            case RETRY_NTIMES: {
                retryPolicy = createRetryNTimes(
                        zookeeperIdGeneratorProperties.getCurator().getRetryNTimes().getCount(),
                        zookeeperIdGeneratorProperties.getCurator().getRetryNTimes().getSleepMsBetweenRetries()
                );
                break;
            }
            case RETRY_FOREVER: {
                retryPolicy = createRetryForever(zookeeperIdGeneratorProperties.getCurator().getRetryForever().getRetryIntervalMs());
                break;
            }
            case RETRY_UNTIL_ELAPSED: {
                retryPolicy = createRetryUntilElapsed(
                        zookeeperIdGeneratorProperties.getCurator().getRetryUntilElapsed().getMaxElapsedTimeMs(),
                        zookeeperIdGeneratorProperties.getCurator().getRetryUntilElapsed().getSleepMsBetweenRetries()
                );
                break;
            }
            default: {

            }
        }

        if (retryPolicy == null) {
            throw new CuratorException("Invalid config value for retryType=" + zookeeperIdGeneratorProperties.getCurator().getRetryType());
        }

        create(
                zookeeperIdGeneratorProperties.getCurator().getConnectString(),
                zookeeperIdGeneratorProperties.getCurator().getSessionTimeoutMs(),
                zookeeperIdGeneratorProperties.getCurator().getConnectionTimeoutMs(),
                retryPolicy
        );

        startAndBlock();
    }

    // 重试指定的次数, 且每一次重试之间停顿的时间逐渐增加
    private RetryPolicy createExponentialBackoffRetry(int baseSleepTimeMs, int maxRetries) {
        return new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
    }

    // 重试指定的次数, 且每一次重试之间停顿的时间逐渐增加，增加了最大重试次数的控制
    private RetryPolicy createBoundedExponentialBackoffRetry(int baseSleepTimeMs, int maxSleepTimeMs, int maxRetries) {
        return new BoundedExponentialBackoffRetry(baseSleepTimeMs, maxSleepTimeMs, maxRetries);
    }

    // 指定最大重试次数的重试
    private RetryPolicy createRetryNTimes(int count, int sleepMsBetweenRetries) {
        return new RetryNTimes(count, sleepMsBetweenRetries);
    }

    // 永远重试
    private RetryPolicy createRetryForever(int retryIntervalMs) {
        return new RetryForever(retryIntervalMs);
    }

    // 一直重试，直到达到规定的时间
    private RetryPolicy createRetryUntilElapsed(int maxElapsedTimeMs, int sleepMsBetweenRetries) {
        return new RetryUntilElapsed(maxElapsedTimeMs, sleepMsBetweenRetries);
    }

    // 创建ZooKeeper客户端实例
    private void create(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, RetryPolicy retryPolicy) {
        LOG.info("Start to initialize Curator..");

        if (curator != null) {
            throw new CuratorException("Curator isn't null, it has been initialized already");
        }

        curator = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
    }

    // 启动ZooKeeper客户端
    @Override
    public void start() {
        LOG.info("Start Curator...");

        validateClosedStatus();

        curator.start();
    }

    // 启动ZooKeeper客户端，直到第一次连接成功
    @Override
    public void startAndBlock() throws Exception {
        LOG.info("start and block Curator...");

        validateClosedStatus();

        curator.start();
        curator.blockUntilConnected();
    }

    // 启动ZooKeeper客户端，直到第一次连接成功，为每一次连接配置超时
    @Override
    public void startAndBlock(int maxWaitTime, TimeUnit units) throws Exception {
        LOG.info("start and block Curator...");

        validateClosedStatus();

        curator.start();
        curator.blockUntilConnected(maxWaitTime, units);
    }

    // 关闭ZooKeeper客户端连接
    @Override
    public void close() {
        LOG.info("Start to close Curator...");

        validateStartedStatus();

        curator.close();
    }

    // 获取ZooKeeper客户端是否初始化
    @Override
    public boolean isInitialized() {
        return curator != null;
    }

    // 获取ZooKeeper客户端连接是否正常
    @Override
    public boolean isStarted() {
        return curator.getState() == CuratorFrameworkState.STARTED;
    }

    // 检查ZooKeeper是否是启动状态
    @Override
    public void validateStartedStatus() {
        if (curator == null) {
            throw new CuratorException("Curator isn't initialized");
        }

        if (!isStarted()) {
            throw new CuratorException("Curator is closed");
        }
    }

    // 检查ZooKeeper是否是关闭状态
    @Override
    public void validateClosedStatus() {
        if (curator == null) {
            throw new CuratorException("Curator isn't initialized");
        }

        if (isStarted()) {
            throw new CuratorException("Curator is started");
        }
    }

    // 获取ZooKeeper客户端
    @Override
    public CuratorFramework getCurator() {
        return curator;
    }

    // 判断路径是否存在
    @Override
    public boolean pathExist(String path) throws Exception {
        return getPathStat(path) != null;
    }

    // 判断stat是否存在
    @Override
    public Stat getPathStat(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        ExistsBuilder builder = curator.checkExists();
        if (builder == null) {
            return null;
        }

        Stat stat = builder.forPath(path);

        return stat;
    }

    // 创建路径
    @Override
    public void createPath(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        curator.create().creatingParentsIfNeeded().forPath(path, null);
    }

    // 创建路径，并写入数据
    @Override
    public void createPath(String path, byte[] data) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        curator.create().creatingParentsIfNeeded().forPath(path, data);
    }

    // 创建路径
    @Override
    public void createPath(String path, CreateMode mode) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        curator.create().creatingParentsIfNeeded().withMode(mode).forPath(path, null);
    }

    // 创建路径，并写入数据
    @Override
    public void createPath(String path, byte[] data, CreateMode mode) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        curator.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
    }

    // 删除路径
    @Override
    public void deletePath(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        curator.delete().deletingChildrenIfNeeded().forPath(path);
    }

    // 获取子节点名称列表
    @Override
    public List<String> getChildNameList(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        return curator.getChildren().forPath(path);
    }

    // 获取子节点路径列表
    @Override
    public List<String> getChildPathList(String path) throws Exception {
        List<String> childNameList = getChildNameList(path);

        List<String> childPathList = new ArrayList<String>();
        for (String childName : childNameList) {
            String childPath = path + "/" + childName;
            childPathList.add(childPath);
        }

        return childPathList;
    }

    // 组装根节点路径
    @Override
    public String getRootPath(String prefix) {
        return "/" + prefix;
    }

    // 组装节点路径
    @Override
    public String getPath(String prefix, String key) {
        return "/" + prefix + "/" + key;
    }
}

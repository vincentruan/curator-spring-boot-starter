/*
 * Copyright [2016] [vincentruan]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.boot.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZookeeperFactory;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.curator.utils.ClassResolveUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadFactory;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass({ZooKeeper.class, CuratorFramework.class})
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration {

    private final CuratorProperties curatorProperties;

    public CuratorAutoConfiguration(CuratorProperties curatorProperties) {
        this.curatorProperties = curatorProperties;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework curatorFramework() {
        Assert.hasText(curatorProperties.getConnectString(), "[Assertion failed] 'connection-string' must be configured.");

        final CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

        if(StringUtils.hasText(curatorProperties.getAclProviderClass())) {
            builder.aclProvider((ACLProvider) ClassResolveUtils.instantiateClass(curatorProperties.getAclProviderClass(), curatorProperties.getAclProviderParams()));
        }

        if(StringUtils.hasText(curatorProperties.getScheme()) && StringUtils.hasText(curatorProperties.getAuthBase64Str())) {
            builder.authorization(curatorProperties.getScheme(), Base64Utils.decodeFromString(curatorProperties.getAuthBase64Str()));
        }

        if(curatorProperties.getCanBeReadOnly() != null) {
            builder.canBeReadOnly(curatorProperties.getCanBeReadOnly());
        }

        if(StringUtils.hasText(curatorProperties.getCompressionProviderClass())) {
            builder.compressionProvider((CompressionProvider) ClassResolveUtils.instantiateClass(curatorProperties.getCompressionProviderClass(), curatorProperties.getCompressionProviderParams()));
        }

        if(StringUtils.hasText(curatorProperties.getConnectString())) {
            builder.connectString(curatorProperties.getConnectString());
        }

        if(curatorProperties.getDefaultDataBase64Str() != null) {
            builder.defaultData(Base64Utils.decodeFromString(curatorProperties.getDefaultDataBase64Str()));
        }

        if(StringUtils.hasText(curatorProperties.getNamespace())) {
            builder.namespace(curatorProperties.getNamespace());
        }


        // 重试策略
        if(StringUtils.hasText(curatorProperties.getRetryPolicyClass())) {
            builder.retryPolicy((RetryPolicy) ClassResolveUtils.instantiateClass(curatorProperties.getRetryPolicyClass(), curatorProperties.getRetryPolicyParams()));
        } else {
            builder.retryPolicy(new ExponentialBackoffRetry(1000, 5));
        }

        builder.sessionTimeoutMs(curatorProperties.getSessionTimeOutMs());
        builder.connectionTimeoutMs(curatorProperties.getConnectionTimeoutMs());

        if(StringUtils.hasText(curatorProperties.getThreadFactoryClass())) {
            builder.threadFactory((ThreadFactory) ClassResolveUtils.instantiateClass(curatorProperties.getThreadFactoryClass(), curatorProperties.getThreadFactoryParams()));
        }

        if(StringUtils.hasText(curatorProperties.getZookeeperFactoryClass())) {
            builder.zookeeperFactory((ZookeeperFactory) ClassResolveUtils.instantiateClass(curatorProperties.getZookeeperFactoryClass(), curatorProperties.getZookeeperFactoryParams()));
        }

        log.info("Start curatorFramework -> {}, sessionTimeOutMs={}, connectionTimeoutMs={}",
                curatorProperties.getConnectString(),
                curatorProperties.getSessionTimeOutMs(),
                curatorProperties.getConnectionTimeoutMs());

        return builder.build();
    }





}

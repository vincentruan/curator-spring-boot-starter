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
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration {

    private final CuratorProperties curatorProperties;

    public CuratorAutoConfiguration(CuratorProperties curatorProperties) {
        this.curatorProperties = curatorProperties;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework curatorFramework() {
        if(!StringUtils.hasText(curatorProperties.getConnectString())) {
            throw new IllegalArgumentException("[Assertion failed] 'connection-string' must be configured.");
        }

        final CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

        /*if(curatorProperties != null) {
            builder.aclProvider(aclProvider);
        }

        if(StringUtils.hasText(scheme)) {
            builder.authorization(scheme, auth);
        }

        if(canBeReadOnly != null) {
            builder.canBeReadOnly(canBeReadOnly);
        }

        if(compressionProvider != null) {
            builder.compressionProvider(compressionProvider);
        }

        if(StringUtils.hasText(connectionString)) {
            builder.connectString(connectionString);
        }

        if(connectionTimeout != null) {
            builder.connectionTimeoutMs(connectionTimeout);
        }

        if(defaultData != null) {
            builder.defaultData(defaultData);
        }

        if(ensembleProvider != null) {
            builder.ensembleProvider(ensembleProvider);
        }

        if(StringUtils.hasText(namespace)) {
            builder.namespace(namespace);
        }

        // 重试策略
        RetryPolicy retryPolicy;
        if(StringUtils.hasText(curatorProperties.getRetryPolicyClass())) {
            BeanUtils.instantiateClass(curatorProperties.getRetryPolicyClass(), RetryPolicy.class);
            ReflectionUtils
        } else {

            retryPolicy = new ExponentialBackoffRetry(1 * 1000, 5);


        }

        if(sessionTimeout != null) {
            builder.sessionTimeoutMs(sessionTimeout);
        }

        if(threadFactory != null) {
            builder.threadFactory(threadFactory);
        }

        if(zookeeperFactory != null) {
            builder.zookeeperFactory(zookeeperFactory);
        }

        client = builder.build();*/

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(curatorProperties.getConnectString(),
                curatorProperties.getSessionTimeOutMs(),
                curatorProperties.getConnectionTimeoutMs(),
                new ExponentialBackoffRetry(1 * 1000, 5));

        /*log.info("Start curatorFramework -> {}, sessionTimeOutMs={}, connectionTimeoutMs={}, baseSleepTimeMs={}, maxRetries={}",
                curatorProperties.getConnectString(),
                curatorProperties.getSessionTimeOutMs(),
                curatorProperties.getConnectionTimeoutMs(),
                this.baseSleepTimeMs,
                this.maxRetries);*/
        return curatorFramework;
    }





}

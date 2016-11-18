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

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CuratorProperties curatorProperties;

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework curatorFramework() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(curatorProperties.getConnectString(),
                curatorProperties.getSessionTimeOutMs(),
                curatorProperties.getConnectionTimeoutMs(),
                curatorProperties.getRetryPolicy());

        log.info("Start curatorFramework -> {}, sessionTimeOutMs={}, connectionTimeoutMs={}, baseSleepTimeMs={}, maxRetries={}",
                curatorProperties.getConnectString(),
                curatorProperties.getSessionTimeOutMs(),
                curatorProperties.getConnectionTimeoutMs(),
                this.baseSleepTimeMs,
                this.maxRetries);
    }
}

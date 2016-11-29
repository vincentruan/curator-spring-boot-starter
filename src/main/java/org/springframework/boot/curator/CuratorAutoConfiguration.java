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
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZookeeperFactory;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass({ZooKeeper.class, CuratorFramework.class})
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration implements BeanFactoryAware {

    private final CuratorProperties curatorProperties;

    private BeanFactory beanFactory;

    public CuratorAutoConfiguration(CuratorProperties curatorProperties) {
        this.curatorProperties = curatorProperties;
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework curatorFramework(RetryPolicy retryPolicy) {

        final CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

        // IMPORTANT: use either connection-string or ensembleProvider but not both.
        if(StringUtils.hasText(curatorProperties.getConnectString())) {
            // connection string will be first
            builder.connectString(curatorProperties.getConnectString());
        } else if (StringUtils.hasLength(curatorProperties.getEnsembleProviderRef())) {
            builder.ensembleProvider(beanFactory.getBean(curatorProperties.getEnsembleProviderRef(), EnsembleProvider.class));
        } else {
            throw new IllegalArgumentException("[Assertion failed] 'connection-string' must be configured.");
        }

        if(StringUtils.hasLength(curatorProperties.getAclProviderRef())) {
            builder.aclProvider(beanFactory.getBean(curatorProperties.getAclProviderRef(), ACLProvider.class));
        }

        if(StringUtils.hasText(curatorProperties.getAuthInfosRef())) {
            List<AuthInfo> authInfos = beanFactory.getBean(curatorProperties.getAuthInfosRef(), List.class);
            builder.authorization(authInfos);
        } else if(StringUtils.hasText(curatorProperties.getScheme()) && StringUtils.hasText(curatorProperties.getAuthBase64Str())) {
            builder.authorization(curatorProperties.getScheme(), Base64Utils.decodeFromString(curatorProperties.getAuthBase64Str()));
        }

        if(curatorProperties.getCanBeReadOnly() != null) {
            builder.canBeReadOnly(curatorProperties.getCanBeReadOnly());
        }

        if(curatorProperties.getUseContainerParentsIfAvailable() != null && !curatorProperties.getUseContainerParentsIfAvailable()) {
            builder.dontUseContainerParents();
        }

        if(StringUtils.hasLength(curatorProperties.getCompressionProviderRef())) {
            builder.compressionProvider(beanFactory.getBean(curatorProperties.getCompressionProviderRef(), CompressionProvider.class));
        }

        if(curatorProperties.getDefaultDataBase64Str() != null) {
            builder.defaultData(Base64Utils.decodeFromString(curatorProperties.getDefaultDataBase64Str()));
        }

        if(StringUtils.hasText(curatorProperties.getNamespace())) {
            builder.namespace(curatorProperties.getNamespace());
        }

        // 重试策略
        if(null != retryPolicy) {
            builder.retryPolicy(retryPolicy);
        }

        if(null != curatorProperties.getSessionTimeOutMs()) {
            builder.sessionTimeoutMs(curatorProperties.getSessionTimeOutMs());
        }

        if(null != curatorProperties.getConnectionTimeoutMs()) {
            builder.connectionTimeoutMs(curatorProperties.getConnectionTimeoutMs());
        }

        if(null != curatorProperties.getMaxCloseWaitMs()) {
            builder.maxCloseWaitMs(curatorProperties.getMaxCloseWaitMs());
        }

        if(StringUtils.hasLength(curatorProperties.getThreadFactoryRef())) {
            builder.threadFactory(beanFactory.getBean(curatorProperties.getThreadFactoryRef(), ThreadFactory.class));
        }

        if(StringUtils.hasLength(curatorProperties.getZookeeperFactoryRef())) {
            builder.zookeeperFactory(beanFactory.getBean(curatorProperties.getZookeeperFactoryRef(), ZookeeperFactory.class));
        }

        log.info("Start curatorFramework -> {}, sessionTimeOutMs={}, connectionTimeoutMs={}",
                curatorProperties.getConnectString(),
                curatorProperties.getSessionTimeOutMs(),
                curatorProperties.getConnectionTimeoutMs());

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean(RetryPolicy.class)
    public RetryPolicy retryPolicy() {
        return new ExponentialBackoffRetry(curatorProperties.getBaseSleepTimeMs(), curatorProperties.getMaxRetries());
    }


    /**
     * Callback that supplies the owning factory to a bean instance.
     * <p>Invoked after the population of normal bean properties
     * but before an initialization callback such as
     * {@link InitializingBean#afterPropertiesSet()} or a custom init-method.
     *
     * @param beanFactory owning BeanFactory (never {@code null}).
     *                    The bean can immediately call methods on the factory.
     * @throws BeansException in case of initialization errors
     * @see BeanInitializationException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

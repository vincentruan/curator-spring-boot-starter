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

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.curator")
public class CuratorProperties {

    private String aclProviderRef;

    private String scheme;

    private String authBase64Str;

    private String authInfosRef;

    private Boolean canBeReadOnly;

    private Boolean useContainerParentsIfAvailable;

    private String compressionProviderRef;

    private String ensembleProviderRef;

    /**
     * list of servers to connect to
     */
    private String connectString;

    private String defaultDataBase64Str;

    private String namespace;

    /**
     * session timeout
     */
    private Integer sessionTimeOutMs;

    /**
     * connection timeout
     */
    private Integer connectionTimeoutMs;

    private Integer maxCloseWaitMs;

    private String threadFactoryRef;

    private String zookeeperFactoryRef;

    /**
     * initial amount of time to wait between retries
     */
    private int baseSleepTimeMs = 1 * 1000;

    /**
     * max number of times to retry
     */
    private int maxRetries = 5;
}

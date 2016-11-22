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
import org.apache.curator.framework.api.ACLProvider;
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

    private String aclProviderClass;

    private String aclProviderParams;

    private String scheme;

    private String authBase64Str;

    private Boolean canBeReadOnly;

    private String compressionProviderClass;

    private String compressionProviderParams;

    /**
     * list of servers to connect to
     */
    private String connectString;

    private Integer connectionTimeout;

    private String defaultDataBase64Str;

    private String namespace;

    /**
     * session timeout
     */
    private int sessionTimeOutMs = 60 * 1000;

    /**
     * connection timeout
     */
    private int connectionTimeoutMs = 15 * 1000;

    private String retryPolicyClass;

    private String retryPolicyParams;

    private String threadFactoryClass;

    private String threadFactoryParams;

    private String zookeeperFactoryClass;

    private String zookeeperFactoryParams;
}

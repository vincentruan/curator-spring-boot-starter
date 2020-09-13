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
package org.springhub.boot.curator;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * configuration properties for curator client
 * @author vincentruan
 * @version 1.0.1
 */
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
    private int baseSleepTimeMs = 1000;

    /**
     * max number of times to retry
     */
    private int maxRetries = 5;

    public String getAclProviderRef() {
        return aclProviderRef;
    }

    public void setAclProviderRef(String aclProviderRef) {
        this.aclProviderRef = aclProviderRef;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getAuthBase64Str() {
        return authBase64Str;
    }

    public void setAuthBase64Str(String authBase64Str) {
        this.authBase64Str = authBase64Str;
    }

    public String getAuthInfosRef() {
        return authInfosRef;
    }

    public void setAuthInfosRef(String authInfosRef) {
        this.authInfosRef = authInfosRef;
    }

    public Boolean getCanBeReadOnly() {
        return canBeReadOnly;
    }

    public void setCanBeReadOnly(Boolean canBeReadOnly) {
        this.canBeReadOnly = canBeReadOnly;
    }

    public Boolean getUseContainerParentsIfAvailable() {
        return useContainerParentsIfAvailable;
    }

    public void setUseContainerParentsIfAvailable(Boolean useContainerParentsIfAvailable) {
        this.useContainerParentsIfAvailable = useContainerParentsIfAvailable;
    }

    public String getCompressionProviderRef() {
        return compressionProviderRef;
    }

    public void setCompressionProviderRef(String compressionProviderRef) {
        this.compressionProviderRef = compressionProviderRef;
    }

    public String getEnsembleProviderRef() {
        return ensembleProviderRef;
    }

    public void setEnsembleProviderRef(String ensembleProviderRef) {
        this.ensembleProviderRef = ensembleProviderRef;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public String getDefaultDataBase64Str() {
        return defaultDataBase64Str;
    }

    public void setDefaultDataBase64Str(String defaultDataBase64Str) {
        this.defaultDataBase64Str = defaultDataBase64Str;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Integer getSessionTimeOutMs() {
        return sessionTimeOutMs;
    }

    public void setSessionTimeOutMs(Integer sessionTimeOutMs) {
        this.sessionTimeOutMs = sessionTimeOutMs;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getMaxCloseWaitMs() {
        return maxCloseWaitMs;
    }

    public void setMaxCloseWaitMs(Integer maxCloseWaitMs) {
        this.maxCloseWaitMs = maxCloseWaitMs;
    }

    public String getThreadFactoryRef() {
        return threadFactoryRef;
    }

    public void setThreadFactoryRef(String threadFactoryRef) {
        this.threadFactoryRef = threadFactoryRef;
    }

    public String getZookeeperFactoryRef() {
        return zookeeperFactoryRef;
    }

    public void setZookeeperFactoryRef(String zookeeperFactoryRef) {
        this.zookeeperFactoryRef = zookeeperFactoryRef;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}

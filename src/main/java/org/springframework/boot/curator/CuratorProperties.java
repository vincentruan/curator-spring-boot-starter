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

import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.curator")
public class CuratorProperties {

    /**
     * list of servers to connect to
     */
    private String connectString;

    /**
     * session timeout
     */
    private int sessionTimeOutMs = 60 * 1000;

    /**
     * connection timeout
     */
    private int connectionTimeoutMs = 15 * 1000;

    /**
     * initial amount of time to wait between retries
     */
    private int baseSleepTimeMs = 1 * 1000;

    /**
     * max number of times to retry
     */
    private int maxRetries = 29;

    private RetryPolicy retryPolicy;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeOutMs() {
        return sessionTimeOutMs;
    }

    public void setSessionTimeOutMs(int sessionTimeOutMs) {
        this.sessionTimeOutMs = sessionTimeOutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
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

    public RetryPolicy getRetryPolicy() {
        return null == retryPolicy ? new ExponentialBackoffRetry(this.baseSleepTimeMs, this.maxRetries) : retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }
}

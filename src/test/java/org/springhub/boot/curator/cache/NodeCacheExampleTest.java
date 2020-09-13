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
package org.springhub.boot.curator.cache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springhub.boot.curator.AbstractCuratorMockServer;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeCacheExampleTest extends AbstractCuratorMockServer {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String PATH = "/example/nodeCache";

    @Autowired
    private CuratorFramework client;

    @Test
    public void testNodeCache() throws Exception {
        CuratorCache cache = null;
        try {
            cache = CuratorCache.build(client, PATH);
            cache.start();
            processCommands(cache);
        } finally {
            CloseableUtils.closeQuietly(cache);
        }
    }


    private void addListener(final CuratorCache cache) {
        // a PathChildrenCacheListener is optional. Here, it's used just to log
        // changes
        CuratorCacheListener listener = (type, oldData, newData) -> {
            Optional<ChildData> optionalOldData = Optional.ofNullable(oldData), optionalNewData = Optional.ofNullable(newData);
            log.info("Node changed: {} from [{}]-[{}] to [{}]-[{}]", type,
                    optionalOldData.map(ChildData::getPath).orElse(""), optionalOldData.map(childData -> new String(childData.getData(), StandardCharsets.UTF_8)).orElse(""),
                    optionalNewData.map(ChildData::getPath).orElse(""), optionalNewData.map(childData -> new String(childData.getData(), StandardCharsets.UTF_8)).orElse(""));
        };
        cache.listenable().addListener(listener);
    }

    private void processCommands(CuratorCache cache) throws Exception {
        addListener(cache);

        setValue(UUID.randomUUID().toString() + "-1");

        setValue(UUID.randomUUID().toString() + "-2");
        show(cache);
        setValue(UUID.randomUUID().toString() + "-3");
        setValue(UUID.randomUUID().toString() + "-4");

        remove();
    }

    /**
     * show: Display the node's value in the cache
     * @param cache
     */
    private void show(CuratorCache cache) {
        cache.get(PATH).ifPresent(d -> log.info("show path -> " + d.getPath() + " = " + new String(d.getData(), StandardCharsets.UTF_8)));
        cache.stream().forEach(m -> log.info("show -> " + m.getPath() + " = " + new String(m.getData(), StandardCharsets.UTF_8)));
    }

    /**
     * remove: Deletes the node with the given name
     * @throws Exception
     */
    private void remove() throws Exception {
        try {
            client.delete().forPath(PATH);
        } catch (KeeperException.NoNodeException e) {
            // ignore
        }
    }

    /**
     * set <value>: Adds or updates a node with the given name
     * @param val
     * @throws Exception
     */
    private void setValue(String val) throws Exception {
        byte[] bytes = val.getBytes();
        try {
            client.setData().forPath(PATH, bytes);
        } catch (KeeperException.NoNodeException e) {
            client.create().creatingParentsIfNeeded().forPath(PATH, bytes);
        }
    }
}

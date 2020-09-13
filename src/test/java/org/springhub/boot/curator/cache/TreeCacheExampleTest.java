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
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeCacheExampleTest {

    private static final String PATH = "/example/treeCache";

    @Autowired
    private CuratorFramework client;

    @Test
    public void testTreeCache() throws Exception {
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
        CuratorCacheListener listener = (type, oldData, newData) -> {
            switch (type) {
                case NODE_CREATED: {
                    System.out.println("TreeNode added: " + ZKPaths.getNodeFromPath(newData.getPath()) + ", value: "
                            + new String(newData.getData(), StandardCharsets.UTF_8));
                    break;
                }
                case NODE_CHANGED: {
                    System.out.println("TreeNode changed: " + ZKPaths.getNodeFromPath(newData.getPath()) + ", value: "
                            + new String(newData.getData()));
                    break;
                }
                case NODE_DELETED: {
                    System.out.println("TreeNode removed: " + ZKPaths.getNodeFromPath(oldData.getPath()));
                    break;
                }
                default:
                    System.out.println("Other event: " + type);
            }
        };

        cache.listenable().addListener(listener);
    }

    private void processCommands(CuratorCache cache) throws Exception {
        // More scaffolding that does a simple command line processor
        printHelp();
        addListener(cache);
        String[] commands = {"set kk kk缓存的值", "set aa aa缓存的值", "set bb bb缓存的值", "list", "set kk kk缓存的值21", "remove kk", "remove aa", "remove bb"};

        int i = 0;
        while (i < commands.length) {
            System.out.print("> ");
            String command = commands[i++].trim();
            String[] parts = command.split("\\s");
            if (parts.length == 0) {
                continue;
            }
            String operation = parts[0];
            String args[] = Arrays.copyOfRange(parts, 1, parts.length);
            if (operation.equalsIgnoreCase("help") || operation.equalsIgnoreCase("?")) {
                printHelp();
            } else if (operation.equals("set")) {
                setValue(command, args);
            } else if (operation.equals("remove")) {
                remove(command, args);
            } else if (operation.equals("list")) {
                list(cache);
            }
            Thread.sleep(1000); // just to allow the console output to catch
            // up
        }
    }

    private void list(CuratorCache cache) {
        if (cache.size() == 0) {
            System.out.println("* empty *");
        } else {
            cache.stream().forEach(data -> System.out.println(data.getPath() + " = " + new String(data.getData())));
        }
    }

    private void remove(String command, String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("syntax error (expected remove <path>): " + command);
            return;
        }
        String name = args[0];
        if (name.contains("/")) {
            System.err.println("Invalid node name" + name);
            return;
        }
        String path = ZKPaths.makePath(PATH, name);
        try {
            client.delete().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            // ignore
        }
    }

    private void setValue(String command, String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("syntax error (expected set <path> <value>): " + command);
            return;
        }
        String name = args[0];
        if (name.contains("/")) {
            System.err.println("Invalid node name" + name);
            return;
        }
        String path = ZKPaths.makePath(PATH, name);
        byte[] bytes = args[1].getBytes();
        try {
            client.setData().forPath(path, bytes);
        } catch (KeeperException.NoNodeException e) {
            client.create().creatingParentsIfNeeded().forPath(path, bytes);
        }
    }

    private void printHelp() {
        System.out.println("An example of using PathChildrenCache. This example is driven by entering commands at the prompt:\n");
        System.out.println("set <name> <value>: Adds or updates a node with the given name");
        System.out.println("remove <name>: Deletes the node with the given name");
        System.out.println("list: List the nodes/values in the cache");
        System.out.println("quit: Quit the example");
        System.out.println();
    }
}

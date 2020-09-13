/*
 * Copyright [2020] [vincentruan]
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

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.client.ConnectStringParser;
import org.apache.zookeeper.server.admin.JettyAdminServer;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vincentruan
 * @version 1.0.0
 * @since 2020-09-13
 */
public abstract class AbstractCuratorMockServer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 在junit环境中使用TestingCluster会有意想不到的坑, 如
     * org.apache.curator.test.TestingQuorumPeerMain#blockUntilStarted()
     * 在获取quorumPeer时，只会等待另外一个初始化quorumPeer的异步线程1秒或者2个网卡获取的时间周期，
     * 如果CPU时间片切换不过来，这里妥妥的超时，然后就会在使用时抛出quorumPeer never got set的启动异常
     * 所以单测这里不建议使用TestingCluster，而改为使用TestingServer
     */
    TestingCluster cluster;
    TestingServer server;

    @Value("${zookeeper.admin.serverPort:" + JettyAdminServer.DEFAULT_PORT + "}")
    private int zkTestServerPort;

    @Autowired
    private CuratorProperties curatorProperties;

    public Path inmemoryJimfs(String first, String... more) {
        // For a simple file system with Unix-style paths and behavior: jimfs
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        /*Path foo = fs.getPath("/foo");
        Files.createDirectory(foo);
        Path hello = foo.resolve("hello.txt"); // /foo/hello.txt
        Files.write(hello, ImmutableList.of("hello world"), StandardCharsets.UTF_8);*/
        return fs.getPath(first, more);
    }

    private void initCluster() throws Exception {
        System.setProperty("zookeeper.admin.serverPort", String.valueOf(zkTestServerPort));
        List<InstanceSpec> specs = new ArrayList<>();
        List<Integer> ports = new ArrayList<>();
        int port = 30155, electionPort = 31155, quorumPort = 32155;
        for (int i = 0; i < 3; i++) {
            InstanceSpec spec = new InstanceSpec(null, port, electionPort, quorumPort,
                    true, i, 10000, 100, null, "127.0.0.1");
            logger.info("Zookeeper-{} : port : {}, election port : {}, quorum port : {}", i, port, electionPort, quorumPort);
            specs.add(spec);
            ports.add(port);
            port++;
            electionPort++;
            quorumPort++;
        }
        //TestingCluster cluster = new TestingCluster(specs);
        cluster = new TestingCluster(specs);
        logger.info("Connect string ports : {},  connect-string={}", ports, cluster.getConnectString());
        try {
            cluster.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.debug("Stopping cluster");
                    cluster.stop();
                } catch (IOException e) {
                    logger.error("Error when stopping cluster", e);
                }
            }));
        } catch (Exception e) {
            logger.error("Error when starting zookeeper cluster", e);
            throw e;
        }
        logger.info("Cluster started with ports : {}", ports);
    }

    private void initServer() throws Exception {
        ConnectStringParser connectStringParser = new ConnectStringParser(curatorProperties.getConnectString());
        server = new TestingServer(connectStringParser.getServerAddresses().get(0).getPort());
        logger.info("Server started : {}", server.getConnectString());
    }

    @Before
    public void setup() throws Exception {
        this.initServer();
    }

    @After
    public void teardown() throws IOException {
        CloseableUtils.closeQuietly(cluster);
        CloseableUtils.closeQuietly(server);
    }

}

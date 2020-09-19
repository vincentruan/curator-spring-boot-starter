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
package org.springhub.boot.curator.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.queue.DistributedIdQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Distributed Queue - 分布式的ZK队列
 * @author vincentruan
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributedIdQueueExampleTest {

    private static final String PATH = "/example/queue";

    @Autowired
    private CuratorFramework client;

    @Test
    public void testDistributedIdQueueExample() throws Exception {
        DistributedIdQueue<String> queue = null;
        try {
            client.getCuratorListenable().addListener(new CuratorListener() {
                @Override
                public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println("CuratorEvent: " + event.getType().name());
                }
            });

            QueueConsumer<String> consumer = createQueueConsumer();
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildIdQueue();
            queue.start();

            for (int i = 0; i < 10; i++) {
                queue.put(" test-" + i, "Id" + i);
                Thread.sleep((long)(50 * Math.random()));
                queue.remove("Id" + i);
            }

            Thread.sleep(200);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(queue);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>(){

            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {

        return new QueueConsumer<String>(){

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume one message: " + message);
            }

        };
    }
}

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
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AkinDistributedBlockingQueueExampleTest {

    private static final String PATH = "/example/queue";

    @Autowired
    private CuratorFramework client;

    @Test
    public void testAkinDistributedBlockingQueueExample() throws Exception {
        DistributedQueue<String> queue = null;
        try {
            client.getCuratorListenable().addListener((client1, event) -> System.out.println("CuratorEvent: " + event.getType().name()));

            AkinDistributedBlockingQueue<String> consumerQueue = new AkinDistributedBlockingQueue<String>(null, (client12, newState) -> {

            });
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumerQueue, createQueueSerializer(), PATH);
            queue = builder.buildQueue();
            consumerQueue.setDistributedQueue(queue);
            queue.start();

            for (int i = 0; i < 10; i++) {
                queue.put(" test-" + i);
                Thread.sleep((long)(3 * Math.random()));
            }


            Thread.sleep(20000);

            for (Object object : consumerQueue) {
                System.out.println(consumerQueue.poll());
            }

        } catch (Exception ex) {

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
}

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
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
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
public class SimpleDistributedQueueExampleTest {
    private static final String PATH = "/example/queue";

    @Autowired
    private CuratorFramework client;

    @Test
    public void testDistributedQueueExample() throws Exception {
        SimpleDistributedQueue queue = null;
        try {
            client.getCuratorListenable().addListener(new CuratorListener() {
                @Override
                public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println("CuratorEvent: " + event.getType().name());
                }
            });

            queue = new SimpleDistributedQueue(client, PATH);
            for (int i = 0; i < 10; i++) {
                queue.offer(("test-" + i).getBytes());
            }

            for (int i = 0; i < 10; i++) {
                System.out.println("took: " + new String(queue.take()));
            }

            Thread.sleep(20000);

        } catch (Exception ex) {

        }
    }
}

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
package org.springhub.boot.curator.barrier;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springhub.boot.curator.AbstractCuratorMockServer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributedDoubleBarrierExampleTest extends AbstractCuratorMockServer {
    private static final int QTY = 5;

    private static final String PATH = "/examples/barrier";

    @Autowired
    private CuratorFramework client;

    @Test
    public void testDistributedDoubleBarrier() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(QTY);
        DistributedBarrier controlBarrier = new DistributedBarrier(client, PATH);
        controlBarrier.setBarrier();

        for (int i = 0; i < QTY; ++i) {
            final DistributedBarrier barrier = new DistributedBarrier(client, PATH);
            final int index = i;
            Callable<Void> task = () -> {

                Thread.sleep((long) (3 * Math.random()));
                System.out.println("Client #" + index + " waits on Barrier");
                barrier.waitOnBarrier();
                System.out.println("Client #" + index + " begins");
                return null;
            };
            service.submit(task);
        }

        Thread.sleep(1000);
        System.out.println("all Barrier instances should wait the condition");

        controlBarrier.removeBarrier();

        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
    }
}

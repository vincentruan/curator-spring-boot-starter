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
package org.springhub.boot.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Multi Shared Lock - 将多个锁看成整体，要不全部acquire成功，要不acquire全部失败。release也是释放全部锁
 *
 * @author vincentruan
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InterProcessMultiLockExampleTest {

    private static final String PATH1 = "/examples/locks1";
    private static final String PATH2 = "/examples/locks2";

    @Autowired
    private CuratorFramework client;

    @Test
    public void testInterProcessMultiLockExample() throws Exception {
        FakeLimitedResource resource = new FakeLimitedResource();
        InterProcessLock lock1 = new InterProcessMutex(client, PATH1);
        InterProcessLock lock2 = new InterProcessSemaphoreMutex(client, PATH2);

        InterProcessMultiLock lock = new InterProcessMultiLock(Arrays.asList(lock1, lock2));

        if (!lock.acquire(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("could not acquire the lock");
        }
        System.out.println("has the lock");

        System.out.println("has the lock1: " + lock1.isAcquiredInThisProcess());
        System.out.println("has the lock2: " + lock2.isAcquiredInThisProcess());

        try {
            resource.use(); //access resource exclusively
        } finally {
            System.out.println("releasing the lock");
            lock.release(); // always release the lock in a finally block
        }
        System.out.println("has the lock1: " + lock1.isAcquiredInThisProcess());
        System.out.println("has the lock2: " + lock2.isAcquiredInThisProcess());
    }
}

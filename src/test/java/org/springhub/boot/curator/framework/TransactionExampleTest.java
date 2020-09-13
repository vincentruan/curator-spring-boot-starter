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
package org.springhub.boot.curator.framework;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.utils.ZKPaths;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

/**
 * @author vincentruan
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionExampleTest {

    @Autowired
    private CuratorFramework client;

    @Test
    public void testTransactionExample() throws Exception {
        try {
            ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), "/a");
            ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), "/another/path");
            ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), "/yet/another/path");


            transaction(client);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        // this example shows how to use ZooKeeper's new transactions

        Collection<CuratorTransactionResult> results = client.inTransaction()
                .create().forPath("/a/path", "some data".getBytes())
                .and()
                .setData().forPath("/another/path", "other data".getBytes())
                .and()
                .delete().forPath("/yet/another/path")
                .and()
                .commit();  // IMPORTANT! The transaction is not submitted until commit() is called

        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }

        return results;
    }

    /*
            These next four methods show how to use Curator's transaction APIs in a more
            traditional - one-at-a-time - manner
     */

    public static CuratorTransaction startTransaction(CuratorFramework client) {
        // start the transaction builder
        return client.inTransaction();
    }

    public static CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction) throws Exception {
        // add a create operation
        return transaction.create().forPath("/a/path", "some data".getBytes()).and();
    }

    public static CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction) throws Exception {
        // add a delete operation
        return transaction.delete().forPath("/another/path").and();
    }

    public static void commitTransaction(CuratorTransactionFinal transaction) throws Exception {
        // commit the transaction
        transaction.commit();
    }
}

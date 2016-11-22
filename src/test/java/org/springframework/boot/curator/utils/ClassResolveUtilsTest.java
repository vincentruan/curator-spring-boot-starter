package org.springframework.boot.curator.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vincentruan on 2016/11/21.
 */
public class ClassResolveUtilsTest {
    @Test
    public void instantiateClass() throws Exception {
        Object obj = ClassResolveUtils.instantiateClass("org.apache.curator.retry.ExponentialBackoffRetry", "3000:int,5:int");
        assertTrue(obj instanceof ExponentialBackoffRetry);
        assertTrue(RetryPolicy.class.isAssignableFrom(obj.getClass()));
    }

}
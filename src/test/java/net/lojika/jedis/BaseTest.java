/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis;

import com.fiftyonred.mock_jedis.MockJedisPool;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author bamasyali
 */
public class BaseTest {

    private static final JedisPool JEDIS_POOL;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxWaitMillis(5000);
        poolConfig.setBlockWhenExhausted(false);

        JEDIS_POOL = new MockJedisPool(poolConfig, "localhost");
    }

    public static JedisPool getJedisPool() {
        return JEDIS_POOL;
    }

}

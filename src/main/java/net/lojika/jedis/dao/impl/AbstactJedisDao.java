/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao.impl;

import java.util.Set;
import net.lojika.jedis.dao.JedisDao;
import net.lojika.jedis.exception.JedisException;
import net.lojika.jedis.model.JedisModel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author cancobanoglu
 * @param <K>
 * @param <T>
 */
public abstract class AbstactJedisDao<K extends Object, T extends Object> implements JedisDao<K, T> {

    protected abstract JedisPool getJedisPool();

    protected abstract String convertKeyToString(K key);

    protected abstract T convert(String value) throws JedisException;

    protected abstract String convert(T value) throws JedisException;

    private final String modelName;

    public AbstactJedisDao(Class<T> modelClass) {
        JedisModel jedisModel = modelClass.getAnnotation(JedisModel.class);
        modelName = jedisModel == null ? null : jedisModel.name();
    }

    private String keyToString(K key) {
        String _key = convertKeyToString(key);
        return modelName == null ? _key : String.format("%s.%s", modelName, _key);
    }

    @Override
    public T get(K key) throws JedisException {
        String value;
        try (Jedis jedis = getJedisPool().getResource()) {
            value = jedis.get(keyToString(key));
        }
        return convert(value);
    }

    @Override
    public void put(K key, T value) throws JedisException {
        if (value == null) {
            return;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.set(keyToString(key), convert(value));
        }
    }

    @Override
    public void put(K key, T value, Integer expireInSeconds) throws JedisException {
        if (value == null) {
            return;
        }
        String _key = keyToString(key);
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.set(_key, convert(value));
            jedis.expire(_key, expireInSeconds);
        }
    }

    @Override
    public void delete(K key) {
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.del(keyToString(key));
        }
    }

    @Override
    public void expireAll() {
        if (modelName == null) {
            return;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            Set<String> keys = jedis.keys(modelName);
            for (String key : keys) {
                jedis.del(key);
            }
        }
    }

}

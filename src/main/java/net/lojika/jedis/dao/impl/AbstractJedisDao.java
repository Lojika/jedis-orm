/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;
import net.lojika.jedis.dao.JedisDao;
import net.lojika.jedis.exception.JedisException;
import net.lojika.jedis.model.JedisModel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author Burak Amasyalı
 * @param <K>
 * @param <T>
 */
public abstract class AbstractJedisDao<K extends Object, T extends Object> implements JedisDao<K, T> {

    protected abstract JedisPool getJedisPool();

    protected abstract ObjectMapper getObjectMapper();

    private final Class<T> modelClass;
    private final String modelName;

    public AbstractJedisDao(Class<T> modelClass) {
        this.modelClass = modelClass;

        JedisModel jedisModel = modelClass.getAnnotation(JedisModel.class);

        if (jedisModel == null && !modelClass.equals(String.class)) {
            throw new RuntimeException("Model must be Sring or JedisModel");
        }

        modelName = jedisModel == null ? null : jedisModel.name();
    }

    private String keyToString(K key) {
        String _key = convertKeyToString(key);
        return modelName == null ? _key : String.format("%s.%s", modelName, _key);
    }

    protected T convertStringToModel(String value) throws JedisException {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return getObjectMapper().readValue(value, modelClass);
        } catch (IOException ex) {
            throw new JedisException(ex);
        }
    }

    protected String convertModelToString(T value) throws JedisException {
        if (value == null) {
            return null;
        }
        try {
            return getObjectMapper().writer().writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new JedisException(ex);
        }
    }

    protected String convertKeyToString(K key) {
        return key.toString();
    }

    @Override
    public T get(K key) throws JedisException {
        String value;
        try (Jedis jedis = getJedisPool().getResource()) {
            value = jedis.get(keyToString(key));
        }
        return convertStringToModel(value);
    }

    @Override
    public void put(K key, T value) throws JedisException {
        if (value == null) {
            return;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.set(keyToString(key), convertModelToString(value));
        }
    }

    @Override
    public void put(K key, T value, Integer expireInSeconds) throws JedisException {
        if (value == null) {
            return;
        }
        String _key = keyToString(key);
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.set(_key, convertModelToString(value));
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
    public void deleteAll() {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.lojika.jedis.dao.JedisDao;
import net.lojika.jedis.exception.JedisException;
import net.lojika.jedis.model.JedisModel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author Burak Amasyalı
 * @param <T>
 */
public abstract class AbstractJedisDao< T extends Object> implements JedisDao< T> {

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

    private String keyToString(String key) {
        return modelName == null ? key : String.format("%s.%s", modelName, key);
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

    @Override
    public T get(String key) throws JedisException {
        String value;

        try (Jedis jedis = getJedisPool().getResource()) {
            value = jedis.get(keyToString(key));
        }

        return convertStringToModel(value);
    }

    @Override
    public Set<String> findKeys() throws JedisException {
        if (modelName == null) {
            throw new JedisException("This operation is not permitted for string dao");
        }

        Set<String> keys;

        try (Jedis jedis = getJedisPool().getResource()) {
            keys = jedis.keys(modelName);
        }

        Set<String> _keys = new HashSet<>();

        for (String key : keys) {
            String _key = key.substring(key.indexOf('.'));
            _keys.add(_key);
        }

        return _keys;
    }

    @Override
    public Map<String, T> find(String filter) throws JedisException {
        if (modelName == null) {
            throw new JedisException("This operation is not permitted for string dao");
        }

        Set<String> keys;

        try (Jedis jedis = getJedisPool().getResource()) {
            keys = jedis.keys(String.format("%s.%s", modelName, filter));

            Map<String, T> map = new HashMap<>();

            for (String key : keys) {

                String _key = key.substring(key.indexOf('.'));
                map.put(_key, convertStringToModel(jedis.get(key)));
            }
            return map;
        }
    }

    @Override
    public Map<String, T> findAll() throws JedisException {
        if (modelName == null) {
            throw new JedisException("This operation is not permitted for string dao");
        }

        Set<String> keys;

        try (Jedis jedis = getJedisPool().getResource()) {
            keys = jedis.keys(modelName);

            Map<String, T> map = new HashMap<>();

            for (String key : keys) {

                String _key = key.substring(key.indexOf('.'));
                map.put(_key, convertStringToModel(jedis.get(key)));
            }
            return map;
        }
    }

    @Override
    public void put(String key, T value) throws JedisException {
        if (value == null) {
            return;
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.set(keyToString(key), convertModelToString(value));
        }
    }

    @Override
    public void put(String key, T value, Integer expireInSeconds) throws JedisException {
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
    public void delete(String key) {
        try (Jedis jedis = getJedisPool().getResource()) {
            jedis.del(keyToString(key));
        }
    }

    @Override
    public void deleteAll() throws JedisException {
        if (modelName == null) {
            throw new JedisException("This operation is not permitted for string dao");
        }
        try (Jedis jedis = getJedisPool().getResource()) {
            Set<String> keys = jedis.keys(modelName);
            for (String key : keys) {
                jedis.del(key);
            }
        }
    }

}

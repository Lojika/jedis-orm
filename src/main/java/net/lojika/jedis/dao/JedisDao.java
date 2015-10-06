/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao;

import java.util.Map;
import java.util.Set;
import net.lojika.jedis.exception.JedisException;

/**
 *
 * @author Burak Amasyalı
 * @param <T>
 */
public interface JedisDao< T extends Object> {

    T findOne(String key) throws JedisException;

    Map<String, T> find(String filter) throws JedisException;

    Map<String, T> findAll() throws JedisException;

    Integer getCount(String filter) throws JedisException;

    Integer getCount() throws JedisException;

    Set<String> findKeys() throws JedisException;

    void saveOrUpdate(String key, T value) throws JedisException;

    void saveOrUpdate(String key, T value, Integer expireInSeconds) throws JedisException;

    void delete(String key);

    void deleteAll() throws JedisException;
}

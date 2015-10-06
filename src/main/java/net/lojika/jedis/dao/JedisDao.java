/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao;

import net.lojika.jedis.exception.JedisException;

/**
 *
 * @author cancobanoglu
 * @param <K>
 * @param <T>
 */
public interface JedisDao<K extends Object, T extends Object> {

    T get(K key) throws JedisException;

    void put(K key, T value) throws JedisException;

    void put(K key, T value, Integer expireInSeconds) throws JedisException;

    void delete(K key);

    void expireAll();
}

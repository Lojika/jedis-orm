/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao.impl;

import net.lojika.jedis.dao.DefaultJedisDao;
import net.lojika.jedis.exception.JedisException;

/**
 *
 * @author bamasyali
 */
public abstract class DefaultJedisDaoImpl extends AbstactJedisDao<String, String> implements DefaultJedisDao {

    public DefaultJedisDaoImpl() {
        super(String.class);
    }

    @Override
    protected String convertKeyToString(String key) {
        return key;
    }

    @Override
    protected String convert(String value) throws JedisException {
        return value;
    }

}

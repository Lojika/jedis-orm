/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao;

import net.lojika.jedis.dao.impl.AbstractJedisDao;

/**
 *
 * @author bamasyali
 */
public abstract class StringModelDao extends AbstractJedisDao< String> {

    public StringModelDao() {
        super(String.class);
    }

}

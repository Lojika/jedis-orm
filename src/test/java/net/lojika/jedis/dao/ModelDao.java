/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao;

import net.lojika.jedis.dao.impl.AbstractJedisDao;
import net.lojika.jedis.model.Model;

/**
 *
 * @author bamasyali
 */
public abstract class ModelDao extends AbstractJedisDao<String, Model> {

    public ModelDao() {
        super(Model.class);
    }

}

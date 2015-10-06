/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.lojika.jedis.dao;

import net.lojika.jedis.dao.impl.AbstractJedisDao;
import net.lojika.jedis.model.JSONModel;

/**
 *
 * @author bamasyali
 */
public abstract class JSONModelDao extends AbstractJedisDao<String, JSONModel> {

    public JSONModelDao() {
        super(JSONModel.class);
    }

}

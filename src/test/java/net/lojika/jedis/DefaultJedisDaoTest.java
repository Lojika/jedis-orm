package net.lojika.jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import net.lojika.jedis.dao.impl.DefaultJedisDaoImpl;
import net.lojika.jedis.exception.JedisException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author bamasyali
 */
public class DefaultJedisDaoTest extends BaseTest {

    public DefaultJedisDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldInsertJedis() throws JedisException {

        String key = "myKey";
        String value = "myValue";

        SampleDao sampleDao = new SampleDao();
        sampleDao.put(key, value);

        assertEquals(value, sampleDao.get(key));

    }

    @Test
    public void shouldInsertJedisWithExpire() throws JedisException, InterruptedException {

        String key = "myKey";
        String value = "myValue";
        Integer expireInSeconds = 1;

        SampleDao sampleDao = new SampleDao();
        sampleDao.put(key, value, expireInSeconds);

        assertEquals(value, sampleDao.get(key));

        Thread.sleep(2000);

        assertNull(sampleDao.get(key));

    }

    @Test
    public void shouldDeleteFromJedis() throws JedisException {

        String key = "myKey";
        String value = "myValue";

        SampleDao sampleDao = new SampleDao();
        sampleDao.put(key, value);

        assertEquals(value, sampleDao.get(key));

        sampleDao.delete(key);

        assertNull(sampleDao.get(key));

    }

    @Test
    public void shouldNotDeleteAllForDefaultDao() throws JedisException {

        String key = "myKey";
        String value = "myValue";

        SampleDao sampleDao = new SampleDao();
        sampleDao.put(key, value);

        assertEquals(value, sampleDao.get(key));

        sampleDao.deleteAll();

        assertNotNull(sampleDao.get(key));

    }

    private class SampleDao extends DefaultJedisDaoImpl {

        @Override
        protected JedisPool getJedisPool() {
            return BaseTest.getJedisPool();
        }

    }

}

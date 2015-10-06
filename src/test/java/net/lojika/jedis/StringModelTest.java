package net.lojika.jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lojika.jedis.dao.StringModelDao;
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
public class StringModelTest extends BaseTest {

    private final StringModelDao sampleDao;

    public StringModelTest() {
        this.sampleDao = new StringModelDao() {

            @Override
            protected JedisPool getJedisPool() {
                return StringModelTest.this.getJedisPool();
            }

            @Override
            protected ObjectMapper getObjectMapper() {
                return StringModelTest.this.getObjectMapper();
            }
        };
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

        sampleDao.put(key, value);

        assertEquals(value, sampleDao.get(key));

    }

    @Test
    public void shouldInsertJedisWithExpire() throws JedisException, InterruptedException {

        String key = "myKey";
        String value = "myValue";
        Integer expireInSeconds = 1;

        sampleDao.put(key, value, expireInSeconds);

        assertEquals(value, sampleDao.get(key));

        Thread.sleep(2000);

        assertNull(sampleDao.get(key));

    }

    @Test
    public void shouldDeleteFromJedis() throws JedisException {

        String key = "myKey";
        String value = "myValue";

        sampleDao.put(key, value);

        assertEquals(value, sampleDao.get(key));

        sampleDao.delete(key);

        assertNull(sampleDao.get(key));

    }

    @Test(expected = JedisException.class)
    public void shouldNotDeleteAllForDefaultDao() throws JedisException {

        String key = "myKey";
        String value = "myValue";

        sampleDao.put(key, value);

        assertEquals(value, sampleDao.get(key));

        sampleDao.deleteAll();

        assertNotNull(sampleDao.get(key));

    }

}

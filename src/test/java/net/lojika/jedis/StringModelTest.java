package net.lojika.jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Set;
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

        sampleDao.saveOrUpdate(key, value);

        assertEquals(value, sampleDao.findOne(key));

    }

    @Test
    public void shouldInsertJedisWithExpire() throws JedisException, InterruptedException {

        String key = "myKey";
        String value = "myValue";
        Integer expireInSeconds = 1;

        sampleDao.saveOrUpdate(key, value, expireInSeconds);

        assertEquals(value, sampleDao.findOne(key));

        Thread.sleep(2000);

        assertNull(sampleDao.findOne(key));

    }

    @Test
    public void shouldDeleteFromJedis() throws JedisException {

        String key = "myKey";
        String value = "myValue";

        sampleDao.saveOrUpdate(key, value);

        assertEquals(value, sampleDao.findOne(key));

        sampleDao.delete(key);

        assertNull(sampleDao.findOne(key));

    }

    @Test()
    public void shouldNotDeleteAllForDefaultDao() throws JedisException {

        String key = "myKey";
        String value = "myValue";

        sampleDao.saveOrUpdate(key, value);

        assertEquals(value, sampleDao.findOne(key));

        sampleDao.deleteAll();

        assertNull(sampleDao.findOne(key));

    }

    @Test
    public void shouldFindKeysFromJedis() throws JedisException {

        String key1 = "myKey";
        String value1 = "myValue";

        String key2 = "myKey2";
        String value2 = "myValue2";

        sampleDao.saveOrUpdate(key1, value1);
        sampleDao.saveOrUpdate(key2, value2);

        Set<String> keys = sampleDao.findKeys();

        assertTrue(keys.contains(key1));
        assertTrue(keys.contains(key2));
        assertEquals(2, keys.size());
    }

    @Test
    public void shouldFindJedis() throws JedisException {

        String key1 = "myKey";
        String value1 = "myValue";

        String key2 = "myKey2";
        String value2 = "myValue2";

        sampleDao.saveOrUpdate(key1, value1);
        sampleDao.saveOrUpdate(key2, value2);

        Map<String, String> map = sampleDao.find("my");

        for (String key : map.keySet()) {
            if (key.equals(key1)) {
                assertEquals(value1, map.get(key));
            }
            if (key.equals(key2)) {
                assertEquals(value2, map.get(key));
            }
        }
        assertEquals(2, map.size());
    }

    @Test
    public void shouldNotFindJedis() throws JedisException {

        String key1 = "myKey";
        String value1 = "myValue";

        String key2 = "myKey2";
        String value2 = "myValue2";

        sampleDao.saveOrUpdate(key1, value1);
        sampleDao.saveOrUpdate(key2, value2);

        Map<String, String> map = sampleDao.find("ma");

        assertEquals(0, map.size());
    }

    @Test
    public void shouldFindAllJedis() throws JedisException {

        String key1 = "myKey";
        String value1 = "myValue";

        String key2 = "myKey2";
        String value2 = "myValue2";

        sampleDao.saveOrUpdate(key1, value1);
        sampleDao.saveOrUpdate(key2, value2);

        Map<String, String> map = sampleDao.findAll();

        for (String key : map.keySet()) {
            if (key.equals(key1)) {
                assertEquals(value1, map.get(key));
            }
            if (key.equals(key2)) {
                assertEquals(value2, map.get(key));
            }
        }
        assertEquals(2, map.size());
    }

    @Test
    public void shouldCountJedis() throws JedisException {

        String key1 = "myKey";
        String value1 = "myValue";

        String key2 = "myKey2";
        String value2 = "myValue2";

        sampleDao.saveOrUpdate(key1, value1);
        sampleDao.saveOrUpdate(key2, value2);

        int count = sampleDao.getCount("my");

        assertEquals(2, count);
    }

    @Test
    public void shouldNotCountJedis() throws JedisException {

        String key1 = "myKey";
        String value1 = "myValue";

        String key2 = "myKey2";
        String value2 = "myValue2";

        sampleDao.saveOrUpdate(key1, value1);
        sampleDao.saveOrUpdate(key2, value2);

        int count = sampleDao.getCount("ma");

        assertEquals(0, count);
    }

    @Test
    public void shouldCountAllJedis() throws JedisException {

        String key1 = "myKey";
        String value1 = "myValue";

        String key2 = "myKey2";
        String value2 = "myValue2";

        sampleDao.saveOrUpdate(key1, value1);
        sampleDao.saveOrUpdate(key2, value2);

        int count = sampleDao.getCount();

        assertEquals(2, count);
    }

}

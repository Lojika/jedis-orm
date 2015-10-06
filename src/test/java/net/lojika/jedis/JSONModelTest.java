package net.lojika.jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lojika.jedis.model.JSONModel;
import java.util.Arrays;
import net.lojika.jedis.dao.JSONModelDao;
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
public class JSONModelTest extends BaseTest {

    private final JSONModelDao modelDao;

    public JSONModelTest() {
        this.modelDao = new JSONModelDao() {
            @Override
            protected JedisPool getJedisPool() {
                return JSONModelTest.this.getJedisPool();
            }

            @Override
            protected ObjectMapper getObjectMapper() {
                return JSONModelTest.this.getObjectMapper();
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
    public void tearDown() throws JedisException {
        modelDao.deleteAll();
    }

    @Test
    public void shouldInsertJedis() throws JedisException {

        String key = "myKey";
        JSONModel model = new JSONModel();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));

        modelDao.saveOrUpdate(key, model);

        JSONModel _model = modelDao.findOne(key);

        assertEquals("name", _model.getName());
        assertEquals("surname", _model.getSurname());
        assertEquals(3, _model.getTagList().size());

    }

    @Test
    public void shouldInsertJedisWithExpire() throws JedisException, InterruptedException {

        String key = "myKey";
        JSONModel model = new JSONModel();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));
        Integer expireInSeconds = 1;

        modelDao.saveOrUpdate(key, model, expireInSeconds);

        assertEquals("name", modelDao.findOne(key).getName());

        Thread.sleep(2000);

        assertNull(modelDao.findOne(key));

    }

    @Test
    public void shouldDeleteFromJedis() throws JedisException {

        String key = "myKey";
        JSONModel model = new JSONModel();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));

        modelDao.saveOrUpdate(key, model);

        assertEquals("name", modelDao.findOne(key).getName());

        modelDao.delete(key);

        assertNull(modelDao.findOne(key));

    }

    @Test
    public void shouldDeleteAllForDefaultDao() throws JedisException {

        String key = "myKey";
        JSONModel model = new JSONModel();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));

        modelDao.saveOrUpdate(key, model);

        assertEquals("name", modelDao.findOne(key).getName());

        modelDao.deleteAll();

        assertNull(modelDao.findOne(key));

    }

}

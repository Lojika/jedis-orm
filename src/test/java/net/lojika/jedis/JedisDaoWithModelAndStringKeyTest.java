package net.lojika.jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lojika.jedis.model.Model;
import java.util.Arrays;
import net.lojika.jedis.dao.ModelDao;
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
public class JedisDaoWithModelAndStringKeyTest extends BaseTest {

    private final ModelDao modelDao;

    public JedisDaoWithModelAndStringKeyTest() {
        this.modelDao = new ModelDao() {
            @Override
            protected JedisPool getJedisPool() {
                return JedisDaoWithModelAndStringKeyTest.this.getJedisPool();
            }

            @Override
            protected ObjectMapper getObjectMapper() {
                return JedisDaoWithModelAndStringKeyTest.this.getObjectMapper();
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
        modelDao.deleteAll();
    }

    @Test
    public void shouldInsertJedis() throws JedisException {

        String key = "myKey";
        Model model = new Model();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));

        modelDao.put(key, model);

        Model _model = modelDao.get(key);

        assertEquals("name", _model.getName());
        assertEquals("surname", _model.getSurname());
        assertEquals(3, _model.getTagList().size());

    }

    @Test
    public void shouldInsertJedisWithExpire() throws JedisException, InterruptedException {

        String key = "myKey";
        Model model = new Model();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));
        Integer expireInSeconds = 1;

        modelDao.put(key, model, expireInSeconds);

        assertEquals("name", modelDao.get(key).getName());

        Thread.sleep(2000);

        assertNull(modelDao.get(key));

    }

    @Test
    public void shouldDeleteFromJedis() throws JedisException {

        String key = "myKey";
        Model model = new Model();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));

        modelDao.put(key, model);

        assertEquals("name", modelDao.get(key).getName());

        modelDao.delete(key);

        assertNull(modelDao.get(key));

    }

    @Test
    public void shouldDeleteAllForDefaultDao() throws JedisException {

        String key = "myKey";
        Model model = new Model();
        model.setName("name");
        model.setSurname("surname");
        model.setTagList(Arrays.asList("a", "b", "c"));

        modelDao.put(key, model);

        assertEquals("name", modelDao.get(key).getName());

        modelDao.deleteAll();

        assertNull(modelDao.get(key));

    }

}

package net.lojika.jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import net.lojika.jedis.dao.ModelDao;
import net.lojika.jedis.dao.SampleDao;
import net.lojika.jedis.exception.JedisException;
import net.lojika.jedis.model.Model;
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
public class MultipleJedisDaoTest extends BaseTest {

    private final SampleDao sampleDao;
    private final ModelDao modelDao;

    public MultipleJedisDaoTest() {
        this.sampleDao = new SampleDao() {

            @Override
            protected JedisPool getJedisPool() {
                return MultipleJedisDaoTest.this.getJedisPool();
            }

            @Override
            protected ObjectMapper getObjectMapper() {
                return MultipleJedisDaoTest.this.getObjectMapper();
            }
        };
        this.modelDao = new ModelDao() {
            @Override
            protected JedisPool getJedisPool() {
                return MultipleJedisDaoTest.this.getJedisPool();
            }

            @Override
            protected ObjectMapper getObjectMapper() {
                return MultipleJedisDaoTest.this.getObjectMapper();
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

        {
            String key = "myKey";
            String value = "myValue";

            sampleDao.put(key, value);

            assertEquals(value, sampleDao.get(key));
        }

        {
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

        {
            String key = "myKey";
            String value = "myValue";

            assertEquals(value, sampleDao.get(key));
        }

    }

    @Test
    public void shouldDeleteFromJedis() throws JedisException {

        {
            String key = "myKey";
            String value = "myValue";

            sampleDao.put(key, value);

            assertEquals(value, sampleDao.get(key));
        }

        {
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

            modelDao.delete(key);

            assertNull(modelDao.get(key));
        }

        {
            String key = "myKey";
            String value = "myValue";

            assertEquals(value, sampleDao.get(key));
        }

    }

    @Test
    public void shouldNotDeleteAllForDefaultDao() throws JedisException {

        {
            String key = "myKey";
            String value = "myValue";

            sampleDao.put(key, value);

            assertEquals(value, sampleDao.get(key));

            sampleDao.deleteAll();

            assertNotNull(sampleDao.get(key));
        }

        {
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

        {
            String key = "myKey";

            assertNotNull(sampleDao.get(key));
        }

    }

}

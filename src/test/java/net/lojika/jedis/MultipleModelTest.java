package net.lojika.jedis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import net.lojika.jedis.dao.JSONModelDao;
import net.lojika.jedis.dao.StringModelDao;
import net.lojika.jedis.exception.JedisException;
import net.lojika.jedis.model.JSONModel;
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
public class MultipleModelTest extends BaseTest {

    private final StringModelDao sampleDao;
    private final JSONModelDao modelDao;

    public MultipleModelTest() {
        this.sampleDao = new StringModelDao() {

            @Override
            protected JedisPool getJedisPool() {
                return MultipleModelTest.this.getJedisPool();
            }

            @Override
            protected ObjectMapper getObjectMapper() {
                return MultipleModelTest.this.getObjectMapper();
            }
        };
        this.modelDao = new JSONModelDao() {
            @Override
            protected JedisPool getJedisPool() {
                return MultipleModelTest.this.getJedisPool();
            }

            @Override
            protected ObjectMapper getObjectMapper() {
                return MultipleModelTest.this.getObjectMapper();
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
            JSONModel model = new JSONModel();
            model.setName("name");
            model.setSurname("surname");
            model.setTagList(Arrays.asList("a", "b", "c"));

            modelDao.put(key, model);

            JSONModel _model = modelDao.get(key);

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
            JSONModel model = new JSONModel();
            model.setName("name");
            model.setSurname("surname");
            model.setTagList(Arrays.asList("a", "b", "c"));

            modelDao.put(key, model);

            JSONModel _model = modelDao.get(key);

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
            JSONModel model = new JSONModel();
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

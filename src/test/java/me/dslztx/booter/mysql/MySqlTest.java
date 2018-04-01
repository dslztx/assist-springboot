package me.dslztx.booter.mysql;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import me.dslztx.booter.TestBooter;
import me.dslztx.booter.client.mysql.DBFactory;
import me.dslztx.booter.client.mysql.DBName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBooter.class)
public class MySqlTest {

  private static final Logger logger = LoggerFactory.getLogger(MySqlTest.class);

  @Test
  public void testIn() {
    try {
      assertNotNull(DBFactory.obtainDataSourceByName(DBName.IN));
      assertNotNull(DBFactory.obtainJdbcTemplateByName(DBName.IN));

      JdbcTemplate jdbcTemplate = DBFactory.obtainJdbcTemplateByName(DBName.IN);

      String createSql = "CREATE TABLE person (\n"
          + "id BIGINT NOT NULL,\n"
          + "name VARCHAR(128),\n"
          + "phone VARCHAR(128)\n"
          + ")";

      jdbcTemplate.execute(createSql);

      String insertSql = "insert into person (id,name,phone) values (100,'dslztx','123456789')";
      jdbcTemplate.execute(insertSql);

      String selectSql = "select * from person";

      List<Map<String, Object>> result = jdbcTemplate.queryForList(selectSql);
      assertTrue(result.size() == 1);
      assertTrue(result.get(0).get("ID").equals(100L));
      assertTrue(result.get(0).get("NAME").equals("dslztx"));
      assertTrue(result.get(0).get("PHONE").equals("123456789"));
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  @Test
  public void testOut() {
    try {
      assertNotNull(DBFactory.obtainDataSourceByName(DBName.OUT));
      assertNotNull(DBFactory.obtainJdbcTemplateByName(DBName.OUT));

      JdbcTemplate jdbcTemplate = DBFactory.obtainJdbcTemplateByName(DBName.OUT);

      String createSql = "CREATE TABLE person (\n"
          + "id BIGINT NOT NULL,\n"
          + "name VARCHAR(128),\n"
          + "phone VARCHAR(128)\n"
          + ")";

      jdbcTemplate.execute(createSql);

      String insertSql = "insert into person (id,name,phone) values (200,'dslztx','123456789')";
      jdbcTemplate.execute(insertSql);

      String selectSql = "select * from person";

      List<Map<String, Object>> result = jdbcTemplate.queryForList(selectSql);
      assertTrue(result.size() == 1);
      assertTrue(result.get(0).get("ID").equals(200L));
      assertTrue(result.get(0).get("NAME").equals("dslztx"));
      assertTrue(result.get(0).get("PHONE").equals("123456789"));
    } catch (Exception e) {
      logger.error("", e);
    }
  }
}

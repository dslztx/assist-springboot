package me.dslztx.booter.client.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourcePool {

  private static final Logger logger = LoggerFactory.getLogger(DataSourcePool.class);

  Map<String, DruidDataSource> namedDataSources;

  public void init() {
    if (namedDataSources != null) {
      try {
        for (DruidDataSource dataSource : namedDataSources.values()) {
          dataSource.init();
        }
      } catch (Exception e) {
        logger.error("", e);
      }
    }
  }

  public void close() {
    if (namedDataSources != null) {
      try {
        for (DruidDataSource dataSource : namedDataSources.values()) {
          dataSource.close();
        }
      } catch (Exception e) {
        logger.error("", e);
      }
    }
  }

  public Map<String, DruidDataSource> getNamedDataSources() {
    return namedDataSources;
  }

  public void setNamedDataSources(Map<String, DruidDataSource> namedDataSources) {
    this.namedDataSources = namedDataSources;
  }

  public DataSource obtainDataSourceByName(String name) {
    if (namedDataSources == null || namedDataSources.get(name) == null) {
      throw new RuntimeException("no datasource instance found");
    }
    return namedDataSources.get(name);
  }
}

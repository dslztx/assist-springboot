package me.dslztx.booter.client.mysql;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

public class DBFactory {

    /**
     * 注入对Spring容器的依赖
     */
    private static ApplicationContext context;

    private static volatile boolean init = false;

    private static DataSourcePool dataSourcePool;

    private static JdbcTemplatePool jdbcTemplatePool;

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        DBFactory.context = context;
    }

    public static DataSource obtainDataSourceByName(DBName dbName) {
        init();

        // “dataSourcePool”如果为空，在“init()”中会报错

        return dataSourcePool.obtainDataSourceByName(dbName.name().toLowerCase());
    }

    public static JdbcTemplate obtainJdbcTemplateByName(DBName dbName) {
        init();

        // “jdbcTemplatePool”如果为空，在“init()”中会报错

        return jdbcTemplatePool.obtainJdbcTemplateByName(dbName.name().toLowerCase());
    }

    private static void init() {
        if (!init) {
            synchronized (DBFactory.class) {
                if (!init) {
                    dataSourcePool = context.getBean(DataSourcePool.class);
                    jdbcTemplatePool = context.getBean(JdbcTemplatePool.class);
                    init = true;
                }
            }
        }
    }
}

@Component
class DBFactoryInitializer {

    @Autowired
    ApplicationContext context;

    @PostConstruct
    public void init() {
        DBFactory.setContext(context);
    }

}

package me.dslztx.booter.property;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "mysql")
@PropertySource(value = {"classpath:conf/mysql.properties", "classpath:application.properties"})
public class MysqlProperties {

    Map<String, SingleMysqlProperties> map;

    public Map<String, SingleMysqlProperties> getMap() {
        return map;
    }

    public void setMap(Map<String, SingleMysqlProperties> map) {
        this.map = map;
    }

}

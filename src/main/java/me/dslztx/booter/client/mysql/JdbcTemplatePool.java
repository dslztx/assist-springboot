package me.dslztx.booter.client.mysql;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplatePool {

    Map<String, JdbcTemplate> namedJdbcTemplates;

    public Map<String, JdbcTemplate> getNamedJdbcTemplates() {
        return namedJdbcTemplates;
    }

    public void setNamedJdbcTemplates(Map<String, JdbcTemplate> namedJdbcTemplates) {
        this.namedJdbcTemplates = namedJdbcTemplates;
    }

    public JdbcTemplate obtainJdbcTemplateByName(String name) {
        if (namedJdbcTemplates == null || namedJdbcTemplates.get(name) == null) {
            throw new RuntimeException("no jdbcTemplate instance found");
        }
        return namedJdbcTemplates.get(name);
    }
}

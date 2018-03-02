package me.dslztx.booter.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dslztx
 */
@Configuration
public class JavaConfigSpot {

  @Bean(name = "examplePOJOBean")
  public POJOBean definePOJOBean() {
    return new POJOBean();
  }

}

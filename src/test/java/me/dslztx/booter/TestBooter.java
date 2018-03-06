package me.dslztx.booter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 增加“exclude=ZooKeeperCuratorAutoConfiguration.class”，放弃“ZooKeeperCuratorAutoConfiguration”对应的自动配置<br/>
 *
 * 增加“exclude=ActiveMQAutoConfiguration.class”，放弃“ActiveMQAutoConfiguration”对应的自动配置<br/>
 *
 * @author dslztx
 */
@SpringBootApplication(scanBasePackages = "me.dslztx.booter.activemq")
public class TestBooter {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(TestBooter.class, args);

    String[] beanDefinitions = context.getBeanDefinitionNames();
    System.out.println("beans as follows are registered : ");
    if (beanDefinitions != null) {
      for (String beanDefinition : beanDefinitions) {
        System.out.println(beanDefinition);
      }
    }
  }
}

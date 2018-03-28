package me.dslztx.booter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.ApplicationContext;

/**
 * 增加“exclude=ZooKeeperCuratorAutoConfiguration.class”，放弃“ZooKeeperCuratorAutoConfiguration”对应的自动配置<br/>
 *
 * 增加“exclude=ActiveMQAutoConfiguration.class”，放弃“ActiveMQAutoConfiguration”对应的自动配置<br/>
 *
 * 增加“exclude=DataSourceAutoConfiguration.class”，放弃“DataSourceAutoConfiguration”对应的自动配置<br/>
 *
 * @author dslztx
 */
@SpringBootApplication(scanBasePackages = "me.dslztx.booter", exclude = {
    ActiveMQAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class Booter {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Booter.class, args);

    String[] beanDefinitions = context.getBeanDefinitionNames();
    System.out.println("beans as follows are registered : ");
    if (beanDefinitions != null) {
      for (String beanDefinition : beanDefinitions) {
        System.out.println(beanDefinition);
      }
    }
  }
}

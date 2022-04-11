package provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

/**
 * @author dslztx
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBooter {

    public static void main(String[] args) {
        //创建了一个SpringContext
        ApplicationContext context = SpringApplication.run(SpringBooter.class, args);

        String[] beanDefinitions = context.getBeanDefinitionNames();
        System.out.println("beans as follows are registered : ");
        if (beanDefinitions != null) {
            for (String beanDefinition : beanDefinitions) {
                System.out.println(beanDefinition);
            }
        }
    }
}

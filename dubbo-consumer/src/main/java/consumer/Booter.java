package consumer;

import dubbo.service.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;

/**
 * @author dslztx
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource(locations = "classpath:consumer.xml")
public class Booter {

    @Autowired
    private DubboService dubboService;

    public static void main(String[] args) {
        //创建了一个SpringContext
        ApplicationContext context = SpringApplication.run(Booter.class, args);

        String[] beanDefinitions = context.getBeanDefinitionNames();
        System.out.println("beans as follows are registered : ");
        if (beanDefinitions != null) {
            for (String beanDefinition : beanDefinitions) {
                System.out.println(beanDefinition);
            }
        }
    }

    @PostConstruct
    public void init() {
        System.out.println(dubboService.helloWorld());
    }
}

package app;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dslztx
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource(locations = "classpath:spring-dubbo-provider.xml")
@Slf4j
public class Booter {

    public static void main(String[] args) {
        log.info("dubbo application is starting");

        // 创建了一个SpringContext
        ApplicationContext context = SpringApplication.run(Booter.class, args);

        String[] beanDefinitions = context.getBeanDefinitionNames();
        System.out.println("beans as follows are registered : ");
        if (beanDefinitions != null) {
            for (String beanDefinition : beanDefinitions) {
                System.out.println(beanDefinition);
            }
        }

        // 无需显示调用context.start()方法，ClassPathXmlApplicationContext会在创建后立即启动
        // context.start();

        // 走到这里已经依次完成：
        // 1、Bean的初始化，post-init方法 / @PostConstruct注解方法的执行
        // 2、dubbo服务初始化完成，并向注册中心注册（具体源码入口可参见org.apache.dubbo.config.ServiceConfig#export）
        // 即满足相应的happens-before条件
        log.info("dubbo application has started");

        // 保持主线程活，避免整个进程退出
        keepAppAlive();
    }

    private static void keepAppAlive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.DAYS.sleep(1);
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
            }
        }).start();
    }
}

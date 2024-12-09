package dubbo.consumer.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import dubbo.api.domain.DubboServiceQuery;
import dubbo.api.domain.DubboServiceResult;
import dubbo.api.domain.Person;
import dubbo.api.service.DubboService;
import lombok.extern.slf4j.Slf4j;
import me.dslztx.assist.util.ObjectAssist;
import me.dslztx.assist.util.RandomAssist;
import me.dslztx.assist.util.StringAssist;

/**
 * @author dslztx
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ImportResource(locations = "classpath:spring-dubbo-consumer.xml")
@Slf4j
public class Booter {

    @Autowired
    private DubboService dubboService;

    public static void main(String[] args) {
        // 创建了一个SpringContext
        ApplicationContext context = SpringApplication.run(Booter.class, args);

        String[] beanDefinitions = context.getBeanDefinitionNames();
        System.out.println("beans as follows are registered : ");
        if (beanDefinitions != null) {
            for (String beanDefinition : beanDefinitions) {
                System.out.println(beanDefinition);
            }
        }
    }

    private static void retainRpcContext() {
        RpcContext.getContext().clearAfterEachInvoke(false);
    }

    private static void printRemoteServerAndClearRpcContext() {
        if (ObjectAssist.isNotNull(RpcContext.getContext())
            && StringAssist.isNotBlank(RpcContext.getContext().getRemoteHost())) {
            log.info("remote host: {}", RpcContext.getContext().getRemoteHost());
        }

        RpcContext.getContext().clearAfterEachInvoke(true);

        RpcContext.removeContext();
    }

    @PostConstruct
    public void init() {
        while (true) {
            for (int i = 0; i < 100; i++) {
                Person person = new Person("dslztx", RandomAssist.randomInt(0, 100));

                try {
                    retainRpcContext();

                    DubboServiceResult result = dubboService.invoke(new DubboServiceQuery(person));
                    log.info(result.getMsg());
                } catch (RpcException e) {
                    log.warn("", e);
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    printRemoteServerAndClearRpcContext();
                }
            }

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
    }
}

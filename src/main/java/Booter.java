import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "me.dslztx.booter")
public class Booter {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(Booter.class, args);

    Object obj = context.getBean("pojoBean");
    System.out.println(obj.getClass());
  }
}

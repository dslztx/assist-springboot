package me.dslztx.booter.activemq;

import java.util.HashMap;
import java.util.Map;
import javax.jms.Destination;
import me.dslztx.booter.Booter;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Booter.class)
public class ActiveMQTest {

  private static final Logger logger = LoggerFactory.getLogger(ActiveMQTest.class);

  @Autowired
  private Producer producer;

  @Test
  public void activeMQTest() {
    try {
      Destination destination = new ActiveMQQueue("test.queue");

      producer.sendMessage(destination, "hello world");

      byte[] byteArray = new byte[1024];
      for (int index = 0; index < 1024; index++) {
        byteArray[index] = (byte) index;
      }
      producer.sendMessage(destination, byteArray);

      Map<String, Object> map = new HashMap<>();
      map.put("1", "spring is the core");
      map.put("2", "spring boot is perfect");
      map.put("3", "fight spring stack");
      producer.sendMessage(destination, map);
    } catch (Exception e) {
      Assert.fail();
      logger.error("", e);
    }
  }

}

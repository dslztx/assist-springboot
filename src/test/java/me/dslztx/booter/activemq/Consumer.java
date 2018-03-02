package me.dslztx.booter.activemq;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

  private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

  // 使用JmsListener配置消费者监听的队列
  @JmsListener(destination = "test.queue")
  public void receiveQueue(Object message) {
    System.out.println("Consumer收到的消息类型为: " + message.getClass() + " , 内容为： " + message);

    try {
      if (message instanceof ActiveMQTextMessage) {
        ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
        System.out.println("ActiveMQTextMessage: " + textMessage.getText());
      } else if (message instanceof ActiveMQBytesMessage) {
        ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
        byte[] bytesArray = new byte[2048];
        int length = bytesMessage.readBytes(bytesArray);
        System.out.println("ActiveMQBytesMessage: " + length + " bytes");
      } else if (message instanceof ActiveMQMapMessage) {
        ActiveMQMapMessage mapMessage = (ActiveMQMapMessage) message;
        System.out.println("ActiveMQMapMessage: " + mapMessage.getContentMap());
      }
    } catch (Exception e) {
      logger.error("", e);
    }
  }
}


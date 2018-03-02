package me.dslztx.booter.activemq;

import java.io.IOException;
import javax.jms.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service("producer")
public class Producer {

  //也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
  @Autowired
  private JmsMessagingTemplate jmsTemplate;

  //发送消息，destination是发送到的队列，message是待发送的消息
  public void sendMessage(Destination destination, final Object message)
      throws IOException {
    //会根据message的类型自动转换成相应的JMS消息类型（这里具体是ActiveMQ消息类型）
    jmsTemplate.convertAndSend(destination, message);
  }
}
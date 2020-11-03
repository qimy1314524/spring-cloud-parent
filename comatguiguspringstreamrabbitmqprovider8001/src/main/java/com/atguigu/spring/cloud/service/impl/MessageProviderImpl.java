package com.atguigu.spring.cloud.service.impl;

import com.atguigu.spring.cloud.service.IMessageProvider;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

/**
 * @author tey
 * @version V1.0
 * @date 2020/9/25- 15:22
 * @desc
 **/
@EnableBinding(Source.class)//定义消息的推送管道
public class MessageProviderImpl implements IMessageProvider {

  @Autowired
  private MessageChannel output;

  @Override
  public String send() {
    String message = UUID.randomUUID().toString();
    //消息构建器
    output.send(MessageBuilder.withPayload(message).build());
    System.out.println("*****"+message);
    return message;
  }
}

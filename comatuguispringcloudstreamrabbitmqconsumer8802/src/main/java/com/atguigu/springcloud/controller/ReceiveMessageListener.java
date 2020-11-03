package com.atguigu.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/9/25- 15:52
 * @desc
 **/
@Component
@EnableBinding(Sink.class) //绑定sink
public class ReceiveMessageListener {

  @Value("${server.port}")
  private String serverPort;

  @StreamListener(Sink.INPUT)
  public void  input(Message<String>message){
    System.out.println("消费者01号--->收到消息: "+message.getPayload()+"\t"+serverPort);

  }



}

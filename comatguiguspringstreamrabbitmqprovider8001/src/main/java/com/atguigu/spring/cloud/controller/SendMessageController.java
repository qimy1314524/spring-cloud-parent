package com.atguigu.spring.cloud.controller;

import com.atguigu.spring.cloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/9/25- 15:31
 * @desc
 **/
@RestController
public class SendMessageController {

  @Autowired
  private IMessageProvider messageProvider;

  @GetMapping("/sendMessage")
  public String sendMessage(){
    return messageProvider.send();
  }

}

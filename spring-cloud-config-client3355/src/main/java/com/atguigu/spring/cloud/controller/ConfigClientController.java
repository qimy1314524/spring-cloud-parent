package com.atguigu.spring.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/24- 9:31
 * @desc
 **/
@RestController
@RefreshScope
public class ConfigClientController {

  @Value("${config.info}")
  private String configInfo;
  @Value("${server.port}")
  private String serverPort;


  @GetMapping("/configInfo")
  public String  getConfigInfo(){
    return configInfo+":"+serverPort;
  }


}

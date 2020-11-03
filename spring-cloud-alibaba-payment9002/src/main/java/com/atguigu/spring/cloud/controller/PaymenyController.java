package com.atguigu.spring.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/22- 15:33
 * @desc
 **/
@RestController
public class PaymenyController {

  @Value("${server.port}")
  private String serverPort;


  @GetMapping("getPort/{id}")
  public String getServerPort(@PathVariable Integer id){
    return "nacos registry serverPort: "+serverPort +" id "+ id;

  }

}

package com.atguigu.spring.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/23- 10:55
 * @desc
 **/
@RestController
public class ConfigController {

  @Autowired
  private StringRedisTemplate redisTemplate;
  @Value("${spring.redis.port}")
  private String redisPort;
  @Value("${spring.redis.host}")
  private String redisHost;

  @GetMapping("/getPort")
  public String getRedisPort(){
    return redisPort;
  }
  @GetMapping("/getHost")
  public String getRedisHost(){
    return redisHost;
  }


  @RequestMapping("/setKey")
  public String addReids(String key,String value){
    redisTemplate.opsForValue().set(key,value);
    return "key :"+key+ " value: "+value;
  }

}

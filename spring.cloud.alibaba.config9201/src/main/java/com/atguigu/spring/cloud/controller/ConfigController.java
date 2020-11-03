package com.atguigu.spring.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/23- 9:51
 * @desc
 **/
@RestController
@RefreshScope //支持nacos的动态刷新功能
public class ConfigController {

  @Value("${config.info}")
  private String configInfo;

  @GetMapping("/config/info")
  public String  getConfigInfo(){
    return "spring cloud alibaba nacos config info :"+configInfo;
  }


}

package com.atguigu.spring.cloud.controller;

import com.atguigu.spring.cloud.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/22- 16:28
 * @desc
 **/
@RestController
public class ConsumerController {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ProviderService providerService;

  @Value("${service-url.nacos-user-service}")
  private String  serverUrl;

  @GetMapping("consumer/getPort/{id}")
  public String getPayment(@PathVariable Integer id){
    return restTemplate.getForObject(serverUrl+"/getPort/"+id,String.class);

  }

  @GetMapping("consumer/openfeign/getPort/{id}")
  public String getOpenFeignPayment(@PathVariable Integer id){
    return providerService.getServerPort(id);
  }



}

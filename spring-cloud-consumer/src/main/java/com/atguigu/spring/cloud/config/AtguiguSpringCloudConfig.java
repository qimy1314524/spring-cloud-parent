package com.atguigu.spring.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 12:53
 * @desc
 **/
//@Configuration
public class AtguiguSpringCloudConfig {

  @Bean
  //可以使RestTemplate有负载均衡的能力,通过调用Ribbon访问provider集群
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}

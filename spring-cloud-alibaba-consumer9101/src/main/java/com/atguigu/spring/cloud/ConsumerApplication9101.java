package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/22- 16:24
 * @desc
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication9101 {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication9101.class,args);
  }
}

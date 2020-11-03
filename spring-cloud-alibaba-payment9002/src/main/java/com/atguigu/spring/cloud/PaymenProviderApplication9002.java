package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/22- 15:44
 * @desc
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class PaymenProviderApplication9002 {

  public static void main(String[] args) {
    SpringApplication.run(PaymenProviderApplication9002.class,args);
  }
}

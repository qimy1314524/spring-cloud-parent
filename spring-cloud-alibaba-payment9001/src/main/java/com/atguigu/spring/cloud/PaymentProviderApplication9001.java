package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/22- 15:26
 * @desc
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentProviderApplication9001 {

  public static void main(String[] args) {
    SpringApplication.run(PaymentProviderApplication9001.class,args);
  }
}

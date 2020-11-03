package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/27- 15:23
 * @desc
 **/

@EnableDiscoveryClient
@SpringBootApplication
public class SentinelApplication9401 {

  public static void main(String[] args) {
    SpringApplication.run(SentinelApplication9401.class,args);
  }
}

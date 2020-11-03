package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/23- 9:40
 * @desc
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class ConfigApplication9201 {

  public static void main(String[] args) {
    SpringApplication.run(ConfigApplication9201.class,args);
  }
}

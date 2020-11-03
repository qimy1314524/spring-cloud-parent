package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/18- 14:42
 * @desc
 **/
@SpringBootApplication
@EnableEurekaServer
public class EurekaRunApplication5001 {

  public static void main(String[] args) {
    SpringApplication.run(EurekaRunApplication5001.class,args);
  }

}

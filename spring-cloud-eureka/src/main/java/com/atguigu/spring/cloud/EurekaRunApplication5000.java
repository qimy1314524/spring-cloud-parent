package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 14:52
 * @desc
 **/
@EnableEurekaServer
@SpringBootApplication
public class EurekaRunApplication5000 {

  public static void main(String[] args) {
    SpringApplication.run(EurekaRunApplication5000.class,args);
  }


}

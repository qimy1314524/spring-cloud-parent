package com.atguigu.sprig.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author tey
 * @version V1.0
 * @date 2020/9/25- 10:17
 * @desc
 **/
@SpringBootApplication
@EnableEurekaClient
public class ConfigClien3366Application {


  public static void main(String[] args) {
    SpringApplication.run(ConfigClien3366Application.class,args);
  }
}

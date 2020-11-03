package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/21- 9:16
 * @desc
 **/
@SpringBootApplication
@EnableEurekaClient
public class GateWayRunApplication {

  public static void main(String[] args) {
    SpringApplication.run(GateWayRunApplication.class, args);
  }

}

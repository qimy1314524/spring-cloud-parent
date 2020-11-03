package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/21- 12:49
 * @desc
 **/
@SpringBootApplication
@EnableConfigServer
public class ConfigServerRunApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigServerRunApplication.class,args);
  }
}

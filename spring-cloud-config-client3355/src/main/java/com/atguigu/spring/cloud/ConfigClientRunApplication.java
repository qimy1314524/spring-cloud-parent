package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/24- 9:29
 * @desc 分布式配置动态刷新问题: 1.pom文件中引入spring-boot-starter-actuator
 * 2. 暴露监控端点
 * management:
 *  endpoints:
 *    web:
 *      exposure:
 *        include: '*'
 * 3.在controller层加入@RefreshScope注解
 * 4.需要发送post请求到config客户端
 * curl -X POST "http://localhost:3355/actuator/refresh"
 *
 *
 *
 **/

@SpringBootApplication
@EnableEurekaClient
public class ConfigClientRunApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigClientRunApplication.class, args);
  }

}

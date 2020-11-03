package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 12:39
 * @desc
 **/
//@EnableEurekaClient 启用eureka客户端功能,必须是Eureka注册中心
//@EnableDiscoveryClient 发现服务功能,不局限与Eureka
@SpringBootApplication
//启用Feign的功能
@EnableFeignClients
@EnableHystrix
//@EnableCircuitBreaker
public class ConsumerRunApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerRunApplication.class,args);
  }

}

package com.atguigu.cloud;

import com.atguigu.cloud.config.OSSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 12:39
 * @desc
 **/
@SpringBootApplication
//使用@EnableCircuitBreaker注解开启断路器功能
@EnableCircuitBreaker
//@EnableConfigurationProperties({OSSProperties.class})
public class ProviderRunApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProviderRunApplication.class,args);
  }

}

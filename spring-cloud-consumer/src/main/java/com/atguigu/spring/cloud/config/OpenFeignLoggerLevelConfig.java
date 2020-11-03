package com.atguigu.spring.cloud.config;

import feign.Logger;
import feign.Logger.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/19- 17:08
 * @desc 配置openfeign的日志打印级别
 **/
@Configuration
public class OpenFeignLoggerLevelConfig {

  @Bean
  public Logger.Level feignLoggerLevel(){
    return Level.FULL;

  }

}

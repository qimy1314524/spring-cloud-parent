package com.smart.aiplatformauth;

import com.smart.aiplatformauth.filter.CurrentUserResolver;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 接口授权服务     ---因开发时间较紧急，部分代码优化、日志打印、注释等工作未开展，以后完善
 * @author: chengjz
 */
@EnableSwagger2
@SpringCloudApplication
@ComponentScan(basePackages = {"com.smart"})
@MapperScan("com.smart.aiplatformauth.mapper")
@EnableAsync
@EnableFeignClients(basePackages = {"com.smart.aiplatformauth.service"})
@EnableHystrix
public class AiplatformAuthApplication implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(AiplatformAuthApplication.class, args);
  }

  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new CurrentUserResolver());
  }
}

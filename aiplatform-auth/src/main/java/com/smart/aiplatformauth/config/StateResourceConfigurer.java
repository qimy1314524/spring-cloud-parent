package com.smart.aiplatformauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源配置类
 * @author: chengjz
 */
@Configuration
public class StateResourceConfigurer implements WebMvcConfigurer {

  @Value("${Instructions.imagepath}")
  private String imagepathurl;
  /**
   * 配置访问静态资源
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry){
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    registry.addResourceHandler("/image/**").addResourceLocations("file:" + imagepathurl);
  }
}
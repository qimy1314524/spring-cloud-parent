package com.atguigu.spring.cloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/21- 9:55
 * @desc 路由配置代码实现
 **/
@Configuration
public class GateWayConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
    Builder route = routeLocatorBuilder.routes()
        .route("path_route_atguigu01", r -> r.path("/guonei").uri("http://news.baidu.com/guonei"))
        .route("path_route_atguigu02", r -> r.path("/guoji").uri("http://news.baidu.com/guoji"));
    return route.build();
  }

}

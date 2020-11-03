package com.atguigu.spring.cloud.config.filter;

import java.time.LocalDateTime;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/10- 10:38
 * @desc
 **/
@Configuration

public class GlobalFilterConfig implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    System.out.println("GlobalFilter启动 Data:" + LocalDateTime.now());
    ServerHttpRequest request = exchange.getRequest();
    if (false) {
      exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
      return exchange.getResponse().setComplete();
    }
    //放行
    Mono<Void> filter = chain.filter(exchange);
    return filter;
  }


  @Override
  public int getOrder() {
    //数值越小优先级越高
    return 0;
  }
}

package com.atguigu.spring.cloud.service;

import com.atguigu.spring.cloud.hystrixFactory.FeignDemoClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/22- 17:11
 * @desc
 **/
@FeignClient(value = "nacos-payment-provider")
public interface ProviderService {

  @GetMapping("getPort/{id}")
  String getServerPort(@PathVariable("id") Integer id);


}

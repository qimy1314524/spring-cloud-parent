package com.atguigu.spring.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.spring.cloud.enums.ResponseEnum;
import com.atguigu.spring.cloud.util.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/11/2- 17:04
 * @desc
 **/
@RestController
public class RateLimitController {

  @GetMapping("/byResource")
  @SentinelResource(value = "byResource",blockHandler = "handlerException")
  public  ResponseVo<?> byResource(){

    return ResponseVo.enumMessage(ResponseEnum.OK, "成功", null);
  }

  public  ResponseVo<?> handlerException(BlockException e){
    System.out.println("handlerException");
    return ResponseVo.enumMessage(ResponseEnum.INTERNAL_SERVER_ERROR,"服务不可用",null);
  }






}

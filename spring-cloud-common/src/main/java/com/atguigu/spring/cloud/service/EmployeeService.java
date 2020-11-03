package com.atguigu.spring.cloud.service;

import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.hystrixFactory.FeignDemoClientFallBack;
import com.atguigu.spring.cloud.util.ResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 16:39
 * @desc
 * @FeignClient表示当前接口和一个Provider对应 注解中fallbackFactory属性指定Provider不可用时提供备用方案的工厂类型
 **/
@FeignClient(value = "atguigu-provider", fallbackFactory = FeignDemoClientFallBack.class)
public interface EmployeeService {

  /**
   * 获取employee 要求@RequestMapping注解映射地址一致 要求方法声明一致
   *
   * @RequestParam @PathVariable @RequestBody 一致
   */
  @RequestMapping("/getEmployee")
  Employee getEmployee();

  @RequestMapping("/hystrix/getEmployee")
  ResponseVo<Employee> hystrixGetEmployee(@RequestParam("signal") String signal)
      throws InterruptedException;
}

package com.atguigu.cloud.service;

import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.enums.ResponseEnum;
import com.atguigu.spring.cloud.util.ResponseVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 16:43
 * @desc
 **/
@RestController
public class EmployeeServiceImpl  {


  @Value("${server.port}")
  private String serverPort;

  @RequestMapping("/getEmployee")
  public Employee getEmployee() {
    return new Employee(1, "张三 :" + 2000, 8000.00);
  }

  /**
   *
   * @param signal
   * @return
   * @throws InterruptedException
   * 针对单个方法做超时设置
   * 下面三个hystrixProperty是和断路器相关的功能
   */
  @RequestMapping("/hystrix/getEmployee")
  @HystrixCommand(fallbackMethod = "hystrixGetEmployeeBroken",
      commandProperties = {
          @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000" )
          ,@HystrixProperty(name = "circuitBreaker.enabled",value = "true") //是否开启断路器
          ,@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10") //请求次数
          ,@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value="10000") //时间窗口期即时间范围
          ,@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value="60")//请求失败率
      })
  public ResponseVo<Employee> hystrixGetEmployee(@RequestParam("signal") String signal)
      throws InterruptedException {
    if ("quick-bang".equals(signal)) {
      throw new RuntimeException();
    }
    if ("slow-bang".equals(signal)) {
      Thread.sleep(3000);
    }
    Employee employee = new Employee(2, "李四 : serverPort:"+serverPort, 10000.00);
    return ResponseVo.enumMessage(ResponseEnum.OK, employee);
  }

  public ResponseVo<Employee> hystrixGetEmployeeBroken(@RequestParam("signal") String signal,Throwable throwable) {
    System.out.println(throwable);
    System.out.println(throwable.getMessage());

    System.out.println(signal);
    return ResponseVo.enumMessage(ResponseEnum.EXCPTION_ERROR, null);
  }


  public Employee getEmployeeBreak() {
    return new Employee(1, "张三 :" + 2000, 8000.00);
  }

}

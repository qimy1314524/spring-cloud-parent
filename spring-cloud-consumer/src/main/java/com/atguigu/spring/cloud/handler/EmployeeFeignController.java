package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.enums.ResponseEnum;
import com.atguigu.spring.cloud.service.EmployeeService;
import com.atguigu.spring.cloud.util.ResponseVo;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 16:50
 * @desc
 **/
@RestController
@DefaultProperties(defaultFallback ="global" )
public class EmployeeFeignController {

  @Autowired
  private EmployeeService employeeService;

  @RequestMapping("feign/getEmployee")
  public Employee getEmployee(){
    System.out.println(employeeService);
    return employeeService.getEmployee();
  }



  @RequestMapping("hystrix/getEmployee")
  @HystrixCommand
  public ResponseVo<?> getHystrixtEmployee(@RequestParam("signal")String signal)
      throws InterruptedException {
    if("bang-bang".equals(signal)) {
      throw new RuntimeException("磁盘io负载过大");
    }
    return employeeService.hystrixGetEmployee(signal);

  }

  @HystrixCommand(fallbackMethod = "testHystrixtEmployee_broken",commandProperties = {
      @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000" )
  })
  @RequestMapping("test/hystrix/getEmployee")
  public ResponseVo<?> testHystrixtEmployee(@RequestParam("signal")Integer signal)
      throws InterruptedException {
    java.lang.Thread.sleep(signal*1000);
    return ResponseVo.enumMessage(ResponseEnum.OK,"服务正常运行");
  }

  @RequestMapping("simpleRequest")
  public ResponseVo<?> simpleRequest()
       {
    return ResponseVo.enumMessage(ResponseEnum.OK,"服务正常运行");
  }


  public ResponseVo<?> testHystrixtEmployee_broken(@RequestParam("signal")Integer signal)
      throws InterruptedException {
    return ResponseVo.enumMessage(ResponseEnum.BAD_REQUEST,"服务降级启动");
  }

  public ResponseVo<?> global(Throwable throwable)
  {
    System.out.println(throwable.getCause());
    System.out.println(throwable.getMessage());
    return ResponseVo.enumMessage(ResponseEnum.BAD_REQUEST,"服务降级启动"+throwable.getCause()+throwable.getMessage());
  }

}

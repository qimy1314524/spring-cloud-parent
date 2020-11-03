package com.atguigu.spring.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 12:55
 * @desc
 **/
@RestController
public class HumanResourceHandler {

  /*@Autowired
  private RestTemplate restTemplate;

  @RequestMapping("/getEmployee")
  public Employee getEmployeeRemote() {
//    String host = "http://localhost:1000/";
//    将远程微服务调用地址从ip+端口号改成微服务名称
    String host = "http://atguigu-provider/";
    String url = "provider/get/employee/remote";

    return restTemplate.getForObject(host + url, Employee.class);

  }*/


}

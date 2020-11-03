package com.atguigu.cloud.handler;

import com.atguigu.spring.cloud.entity.Employee;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 12:43
 * @desc
 **/
@RestController
public class EmployeeHandler {

  @RequestMapping("provider/get/employee/remote")
  public Employee getEmployeeRemote(HttpServletRequest request) {
    int serverPort = request.getServerPort();
    return new Employee(1, "张三 :"+ serverPort, 8000.00);

  }


}

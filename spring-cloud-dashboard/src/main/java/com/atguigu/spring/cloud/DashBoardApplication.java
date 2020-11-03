package com.atguigu.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/10- 9:51
 * @desc
 **/
//启用仪表盘监控功能
@EnableHystrixDashboard
@SpringBootApplication
public class DashBoardApplication {

  public static void main(String[] args) {
    SpringApplication.run(DashBoardApplication.class,args);
  }

}

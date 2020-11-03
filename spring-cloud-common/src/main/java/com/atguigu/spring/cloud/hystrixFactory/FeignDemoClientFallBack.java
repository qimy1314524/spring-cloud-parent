package com.atguigu.spring.cloud.hystrixFactory;

import com.atguigu.spring.cloud.entity.Employee;
import com.atguigu.spring.cloud.enums.ResponseEnum;
import com.atguigu.spring.cloud.service.EmployeeService;
import com.atguigu.spring.cloud.util.ResponseVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 实现Consumer端服务剪辑功能,实现FallbackFactoru接口时要传入
 * @FeignClient接口类型
 * 在create()中返回@FeignClient注解标记的接口类型的对象,当Provider调用失败后
 * 会执行这个对象相应的方法
 * 服务降级
 */
@Slf4j
@Component
public class FeignDemoClientFallBack implements FallbackFactory<EmployeeService> {
    @Override
    public EmployeeService create(Throwable throwable) {
        return new EmployeeService() {
            @Override
            public Employee getEmployee() {
                System.out.println(throwable);
                Employee employee = new Employee();
                employee.setEmpName("发生错误");
                return employee;
            }

            @Override
            public ResponseVo<Employee> hystrixGetEmployee(String signal){
                System.out.println(throwable);
                System.out.println("FeignDemoClientFallBack");
                return ResponseVo.enumMessage(ResponseEnum.FORBIDDEN,null);
            }        };
    }
}
package com.atguigu.spring.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/5- 11:15
 * @desc
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

  private Integer empId;
  private String empName;
  private Double EmpSalary;



}

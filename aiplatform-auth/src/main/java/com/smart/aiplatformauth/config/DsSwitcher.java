package com.smart.aiplatformauth.config;


import com.smart.aiplatformauth.Enum.DataSourceEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义数据源切换注解
 * @Auther chengjz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DsSwitcher {
  //默认数据源是mysql
  DataSourceEnum value() default DataSourceEnum.MYSQL;

  //使用方式serviceimpl 的override方法前加注解@DsSwitcher(DataSourceEnum.PG)
}

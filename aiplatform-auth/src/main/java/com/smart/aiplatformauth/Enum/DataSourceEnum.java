package com.smart.aiplatformauth.Enum;

/**
 * 数据源切换枚举类
 * @Auther chengjz
 */
public enum DataSourceEnum {

  PG("PG"), MYSQL("MYSQL");

  private String value;

  DataSourceEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

}

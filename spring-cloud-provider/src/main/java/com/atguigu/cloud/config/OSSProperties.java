package com.atguigu.cloud.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/14- 16:01
 * @desc
 **/
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aliyun.oss")
public class OSSProperties {

  private String endPoint;
  private String bucketName;
  private String accessKeyId;
  private String accessKeySecret;
  private String bucketDomain;
}

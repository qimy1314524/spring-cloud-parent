package com.smart.aiplatformauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wchao
 */
@ConfigurationProperties(
        prefix = "wx.miniapp"
)
@Configuration
@Data
@EnableConfigurationProperties(WxProperties.class)
public class WxProperties{
    private String appid;
    private String secret;
    private String token;
    private String msgDataFormat;
}

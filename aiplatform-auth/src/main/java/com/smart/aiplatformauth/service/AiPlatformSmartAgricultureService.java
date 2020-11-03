package com.smart.aiplatformauth.service;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * Feign微服务aiplatform-smartagriculture接口调用类
 * @author chengjz
 */
@FeignClient(name = "aiplatform-smartagriculture")
public interface AiPlatformSmartAgricultureService {

}

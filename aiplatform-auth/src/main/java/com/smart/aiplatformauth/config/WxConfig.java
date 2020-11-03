package com.smart.aiplatformauth.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wchao
 */
@Configuration
public class WxConfig {

    @Autowired
    private WxProperties wxProperties;

    @Bean
    public WxMaService getWxMaService(){
        WxMaServiceImpl wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        BeanUtils.copyProperties(wxProperties,config);
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }
}

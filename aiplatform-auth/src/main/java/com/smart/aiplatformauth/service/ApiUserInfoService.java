package com.smart.aiplatformauth.service;

import com.smart.aiplatformauth.dto.WeChatBindingDto;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.vo.ResultVO;

/**
 * 登录鉴权接口类
 * @author: chengjz
 */
public interface ApiUserInfoService {

    ResultVO auth(User user);
    ResultVO refreshToken(String refreshToken);

    /**
     * 微信登录验证
     * @param code  小程序用户码
     * @return
     */
    ResultVO authWeChat(String code);


    /**
     * 微信绑定
     * @param weChatBindingDto
     * @return
     * author：wchao
     */
    ResultVO bindingWeChat(WeChatBindingDto weChatBindingDto);

}

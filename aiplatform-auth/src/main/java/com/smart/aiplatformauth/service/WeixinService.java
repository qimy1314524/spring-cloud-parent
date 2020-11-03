package com.smart.aiplatformauth.service;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import java.util.List;

/**
 * @author wchao
 */
public interface WeixinService {
    /**
     * 根据code获取openId和SessionKey
     * @param code
     * @return
     */
    WxMaJscode2SessionResult getOpenIdAndSession(String code);

    /**
     * 发送微信消息通知
     * @param openid
     * @param templateId
     * @param fields
     * @param values
     * @return
     */
    Integer sendSubscribeMsg(List<String> openid, String templateId,  List<String> fields, List<String> values);
}

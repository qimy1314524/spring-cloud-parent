package com.smart.aiplatformauth.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage.Data;
import com.google.common.collect.Lists;
import com.smart.aiplatformauth.exception.MyException;
import com.smart.aiplatformauth.service.WeixinService;
import com.smart.aiplatformauth.utils.ResultVOUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wchao
 */
@Service
@Slf4j
public class WeixinServiceImpl implements WeixinService {
    @Resource
    WxMaService wxMaService;

    @Override
    public WxMaJscode2SessionResult getOpenIdAndSession(String code) {
        WxMaJscode2SessionResult result;
        try {
            result = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            log.error("微信接口出错 ，错误信息"+error.getErrorMsg());
            throw new MyException(ResultVOUtil.error( String.valueOf(error.getErrorCode()),error.getErrorMsg()));
        }
        return result;
    }

    @Override
    public Integer sendSubscribeMsg(List<String> openids, String templateId,List<String> fields, List<String> values) {
        List<WxMaSubscribeMessage> messages = openids.stream()
            .map(openid -> {
                ArrayList<Data> list = Lists.newArrayList();
                for (int i = 0; i < fields.size(); i++) {
                    list.add(new WxMaSubscribeMessage.Data(fields.get(i), values.get(i)));
                }
                WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    .templateId(templateId)
                    .data(list)
                    .toUser(openid)
                    .build();
                return message;
            }).collect(Collectors.toList());
        AtomicInteger result  = new AtomicInteger(openids.size());
        messages.stream().forEach(message -> {
            try {
                wxMaService.getMsgService().sendSubscribeMsg(message);
            } catch (WxErrorException e) {
                result.getAndDecrement();
            }
        });
        log.info("总共发送{}条，{}条发送成功，{}条发送失败",openids.size(),result.get(),openids.size()-result.get());
        return result.get();
    }
}

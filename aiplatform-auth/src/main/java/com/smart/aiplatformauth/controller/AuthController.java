package com.smart.aiplatformauth.controller;


import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.smart.aiplatformauth.dto.WeChatBindingDto;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.model.request.ApiUserInfo;
import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.service.ApiUserInfoService;
import com.smart.aiplatformauth.utils.ResultVOUtil;
import com.smart.aiplatformauth.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权Controller
 * @author: chengjz
 */
@RestController
@Slf4j
@Api(value = "AiplatformAuthApi")
@Validated
@DefaultProperties(defaultFallback = "defaultFallback")
public class AuthController {

    @Autowired
    private ApiUserInfoService apiUserInfoService;

    /**
     * 登录授权，生成JWT
     * @return
     */
    @HystrixCommand(commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "100000"), //请求超时时间
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //断路器请求阈值
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "100000"), //断路器休眠时间
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "70")  //断路器错误请求百分比
    })
    @PostMapping("/auth")
    @ApiOperation(value = "登录授权", notes = "body体入参，包括userName和passWord参数信息")
    public ResultVO auth(@Valid @RequestBody ApiUserInfo apiUserInfo) {
        User user = new User();
        user.setUsername(apiUserInfo.getUserName());
        user.setPassword(apiUserInfo.getPassWord());
        return apiUserInfoService.auth(user);
    }

    /**
     * 微信登录授权，生成JWT
     * @param code
     * @return
     */
    @HystrixCommand(commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "100000"), //请求超时时间
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //断路器请求阈值
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "100000"), //断路器休眠时间
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "70")  //断路器错误请求百分比
    })
    @PostMapping("/authWeChat")
    @ApiOperation(value = "微信登录授权", notes = "body体入参，包括userName和passWord参数信息")
    public ResultVO authWeChat(@RequestBody String code) {
        if(code == null){
            return  ResultVOUtil.error(Code.BAD_REQUEST.getCode(),Code.BAD_REQUEST.getMessage());
        }
        return apiUserInfoService.authWeChat(code);
    }

    /**
     * 微信号绑定系统账号
     * @param weChatBindingDto
     * @return
     * author：wchao
     */
    @PostMapping("bindingWeChat")
    @ApiOperation(value = "微信号绑定系统账号", notes = "三个参数全传\n【@ApiImplicitParams({\n"
            + "      @ApiImplicitParam(paramType = \"body\", name = \"code\", dataType = \"String\", value = \"小程序端获取的code\", required = true),\n"
            + "      @ApiImplicitParam(paramType = \"body\", name = \"username\", dataType = \"String\", value = \"用户名\", required = true),\n"
            + "      @ApiImplicitParam(paramType = \"body\", name = \"password\", dataType = \"String\", value = \"密码\", required = true)\n"
            + "  })】")
    public ResultVO bindingWeChat(@Valid @RequestBody WeChatBindingDto weChatBindingDto) {
        return apiUserInfoService.bindingWeChat(weChatBindingDto);
    }

    /**
     * 刷新JWT
     * @param refreshToken
     * @return
     */
    @GetMapping("/token/refresh")
    @ApiOperation(value = "刷新Token令牌", notes = "string类型参数refreshToken")
    public ResultVO refreshToken(@RequestParam @NotNull String refreshToken) {
        return apiUserInfoService.refreshToken(refreshToken);
    }

    public ResultVO defaultFallback(){
        /*Map<String, String> map = new HashMap<String, String>();
        map.put("messagetitle","服务熔断通知");
        map.put("message","授权服务异常，已触发熔断保护");
        map.put("status","4001");
        map.put("msg","服务熔断通知:授权服务异常,已触发熔断保护!");*/
        //TODO 极光推送、邮件、短信、微信消息等通知到管理员服务出错
        log.error("授权服务触发降级，进行接口熔断处理");
        return ResultVOUtil.error("4001", "服务熔断通知:授权服务异常,已触发熔断保护!");
    }

    /**
     * 测试网关路由机制token应用
     * @return
     */
    @GetMapping("/testToken")
    public ResultVO testToken() {
        return ResultVOUtil.success();
    }
}
package com.smart.aiplatformauth.model.request;


import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 请求用户信息实体类
 * @author: chengjz
 */
@Data
public class ApiUserInfo {

    @NotNull(message = "userName is null")
    private String userName;
    @NotNull(message = "passWord is null")
    private String passWord;
}
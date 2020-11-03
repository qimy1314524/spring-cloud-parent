package com.smart.aiplatformauth.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wchao
 */
@Data
public class WeChatBindingDto {
    @NotNull(message = "code is null")
    private String code;
    @NotNull(message = "username is null")
    private String username;
    @NotNull(message = "password is null")
    private String password;
}

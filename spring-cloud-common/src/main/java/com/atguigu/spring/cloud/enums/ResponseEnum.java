package com.atguigu.spring.cloud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局响应状态
 *
 * @author linrf
 * @version V1.0
 * @date 2020/3/26 19:07
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {
    /**
     * 200请求成功
     */
    OK(200, "请求成功！"),
    /**
     * 303登录失败
     */
    LOGIN_FAIL(303, "登录失败！"),
    /**
     * 400请求参数出错
     */
    BAD_REQUEST(400, "请求参数错误！"),
    /**
     * 401没有登录
     */
    UNAUTHORIZED(401, "未登录！"),
    /**
     * 403没有权限
     */
    TOKENERR(402, "token验证失败！"),
    /**
     * 403没有权限
     */
    FORBIDDEN(405, "权限不足！"),
    /**
     * 410已被删除
     */
    GONE(410, "已删除！"),
    /**
     * 423已被锁定
     */
    LOCKED(423, "账户已锁定！"),
    /**
     * 500服务器出错
     */
    INTERNAL_SERVER_ERROR(500, "服务器出错！"),
    /**
     * 异常
     */
    EXCPTION_ERROR(4001, "特殊错误！");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 提示信息
     */
    private final String message;
}

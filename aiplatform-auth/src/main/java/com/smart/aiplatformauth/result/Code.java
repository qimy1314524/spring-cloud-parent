package com.smart.aiplatformauth.result;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回信息
 * @author: chengjz
 */
@Getter
@AllArgsConstructor
public enum Code {

    SUCCESS("00000", "操作成功"),
    PARAM_DEL("10001", "参数缺失"),
    THIRD_INTERFACE_EXCEPTION("10002","三方接口异常"),
    THIRD_INTERFACE_FAIL("10003","三方接口结果失败"),
    DATA_QUERY_EXCEPTION("10004", "服务数据查询异常"),
    DATA_CHECK_EXCEPTION("10005", "数据校验失败"),
    SYSTEM_EXCEPTION("10006", "系统异常"),

    OK("200", "请求成功"),
    LOGIN_FAIL("303", "登录失败"),
    BAD_REQUEST("400", "请求参数出错"),
    UNAUTHORIZED("401", "无效身份或没有登录"),
    FORBIDDEN("403", "没有权限"),
    GONE("410", "已被删除"),
    LOCKED("423", "已被锁定"),
    INTERNAL_SERVER_ERROR("500", "服务器出错"),
    EXCPTION_ERROR("4001", "异常"),
    TOKENERR("20003", "token验证失败");

    private String code;
    private String message;
}
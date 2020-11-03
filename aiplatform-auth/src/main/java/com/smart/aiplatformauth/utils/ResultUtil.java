package com.smart.aiplatformauth.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 封装返回结果集
 * @Auther chengjz
 */
public class ResultUtil {

    public static String result(final String status,final String msg,final Object data,final Integer total) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
                put("data", data);
                put("total", total);
            }
        };
        return jsonObject.toString();
    }

    public static String result(final String status,final String msg,final Object data,final Integer total,final Object data1) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
                put("data", data);
                put("total", total);
                put("data1", data1);
            }
        };
        return jsonObject.toString();
    }

    public static String result(final String status,final String msg,final Object data) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
                put("data", data);
            }
        };
        return jsonObject.toString();
    }

    public static String result(final String status,final String msg,final Object data,final Object data1) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
                put("data", data);
                put("data1", data1);
            }
        };
        return jsonObject.toString();
    }

    public static String result(final String status,final String msg) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
            }
        };
        return jsonObject.toString();
    }

}

package com.smart.aiplatformauth.utils;

import static com.xiaoleilu.hutool.lang.Validator.isNotEmpty;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * json数据格式化
 * @Auther chengjz
 */
@Slf4j
public class JsonUti {

    /**
     * 将object转化成json串
     * @param obj
     * @return
     */
    public static String object2Json(Object obj){
        if (obj == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将object转化成JSONObject
     * @param obj
     * @return
     */
    public static JSONObject object2JsonObject(Object obj){
        if (obj == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String temp = mapper.writeValueAsString(obj);
            JSONObject jsonObjectTemp = JSONObject.parseObject(temp);
            return jsonObjectTemp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* 推荐，速度最快
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断对象是否为空
     * @param obj
     * @return
     */
    public static Boolean isNotEmptyBean(Object obj) {
        Boolean flag = false;
        try {
            if (null != obj){
                //得到类对象
                Class<?> c = (Class<?>) obj.getClass();
                //得到属性集合
                Field[] fs = c.getDeclaredFields();
                //得到方法体集合
                Method[] methods = c.getDeclaredMethods();
                //遍历属性
                for (Field f : fs) {
                    //设置属性是可以访问的(私有的也可以)
                    f.setAccessible(true);
                    String fieldGetName = parGetName(f.getName());
                    //判断属性是否存在get方法
                    if (!checkGetMet(methods, fieldGetName)) {
                        continue;
                    }
                    //得到此属性的值
                    Object val = f.get(obj);
                    //只要有1个属性不为空,那么就不是所有的属性值都为空
                    if (isNotEmpty(val)) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("判断对象是否为空出错：" + e.getMessage());
        }
        return flag;
    }

    /**
     * 拼接某属性的 get方法
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        if (fieldName.charAt(0) == '_')
            startIndex = 1;
        return "get"
            + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
            + fieldName.substring(startIndex + 1);
    }

    /**
     * 判断是否存在某属性的 get方法
     * @param methods
     * @param fieldGetMet
     * @return boolean
     */
    public static Boolean checkGetMet(Method[] methods, String fieldGetMet) {

        for (Method met : methods) {
            if (fieldGetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }
}
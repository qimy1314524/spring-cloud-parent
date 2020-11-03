package com.atguigu.spring.cloud.util;

import com.atguigu.spring.cloud.enums.ResponseEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 返回vo响应数据结构
 *
 * @author linrf
 * @version V1.0
 * @date 2020/3/24 16:43
 */
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {
    private  Integer code;
    private  String message;
    private T data;

    public ResponseVo(){

    }

    private  ResponseVo(Integer code,String message,T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }


    public static <T> ResponseVo<T> enumMessage(ResponseEnum code ,String message,T data){
        return new ResponseVo<T>(code.getCode(),message,data);
    }

    public static <T> ResponseVo<T> enumMessage(ResponseEnum code ,T data){
        return  enumMessage(code,null,data);
    }


    public static <T> ResponseVo<T> success(String message,T data){
        return new ResponseVo<T>(ResponseEnum.OK.getCode(),message,data);
    }

    public static ResponseVo success(String message){
        return success(message,null);
    }


    public static  ResponseVo success(){
      return success(null,null);
    }

    public static <T> ResponseVo<T> error(String message,T data){

        return success(null,null);
    }

}

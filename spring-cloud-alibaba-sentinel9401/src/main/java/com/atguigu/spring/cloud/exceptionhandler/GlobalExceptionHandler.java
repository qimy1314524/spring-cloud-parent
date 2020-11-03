package com.atguigu.spring.cloud.exceptionhandler;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 异常的统一处理
 * @author: chengjz
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    //参数校验异常
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object bindExceptionHandler(HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
        log.error("URL:{},参数有误！", request.getRequestURI().replaceAll(request.getContextPath(), ""));
//        return ResultVOUtil.error(Code.BAD_REQUEST.getCode(), bindingResult.getFieldError().getDefaultMessage());
        return bindingResult.getFieldError().getDefaultMessage();
    }

    //运行异常
    /*@ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object runtimeExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        return ResultVOUtil.error(Code.INTERNAL_SERVER_ERROR.getCode(), Code.INTERNAL_SERVER_ERROR.getMessage());
    }*/

    //参数校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object bindExceptionHandler(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        log.error("URL:{},参数有误！", request.getRequestURI().replaceAll(request.getContextPath(), ""));
//        return ResultUtil.result(Code.BAD_REQUEST.getCode(), "请求失败,参数有误", bindingResult.getFieldError().getDefaultMessage());
        return bindingResult.getFieldError().getDefaultMessage();
    }

    //参数校验异常
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object constraintViolationExceptionHandler(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException exception) {
        log.error("URL:{},参数有误！", request.getRequestURI().replaceAll(request.getContextPath(), ""));
//        return ResultUtil.result(Code.BAD_REQUEST.getCode(), "请求失败,参数不能为null", exception.getMessage());
        return exception.getMessage();
    }

    //参数校验异常
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object methodArgumentTypeMismatchExceptionHandler(HttpServletRequest request, HttpServletResponse response, MethodArgumentTypeMismatchException exception) {
        log.error("URL:{},参数有误！", request.getRequestURI().replaceAll(request.getContextPath(), ""));
//        return ResultUtil.result(Code.BAD_REQUEST.getCode(), "请求失败,参数不能为null", exception.getMessage());
        return exception.getMessage();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handle(Exception e) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

//        if (e instanceof MyException) {
//            MyException myException = (MyException)e;
//            return myException.getResult();
//        } else {
            String url = request.getRequestURI();
            String remoteAddr = request.getRemoteAddr();
            String method = request.getMethod();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("请求url", url);
            jsonObject.put("请求的远程地址", remoteAddr);
            jsonObject.put("请求方法", method);
            log.error("系统异常 {}",e);
//            return ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "未知错误:"+e.getMessage(), jsonObject);
            return e.getMessage();
//        }
    }


}
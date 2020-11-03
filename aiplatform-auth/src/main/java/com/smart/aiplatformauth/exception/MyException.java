package com.smart.aiplatformauth.exception;

/**
 * @desc: 异常处理
 * @author: chengjz
 */
public class MyException extends  RuntimeException{

    /**
     * 返回结果
     */
    private Object result;

    public MyException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

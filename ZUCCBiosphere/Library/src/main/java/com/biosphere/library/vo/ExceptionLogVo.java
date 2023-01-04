package com.biosphere.library.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Author Administrator
 * @Date 2022/11/18 11:05
 * @Version 1.0
 */
public class ExceptionLogVo extends RuntimeException{

    private RespBeanEnum e;

    private Object errorData;

    public ExceptionLogVo() {
        super();
    }

    public ExceptionLogVo(RespBeanEnum res) {
        super(res.getMessage());
        this.e = res;
        // super(res.getMessage());
    }

    public ExceptionLogVo(RespBeanEnum res, List<String> errorData) {
        super(res.getMessage());
        this.e = res;
        this.errorData = errorData;
        // super(res.getMessage());
    }



    public RespBeanEnum getE() {
        return e;
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }
}

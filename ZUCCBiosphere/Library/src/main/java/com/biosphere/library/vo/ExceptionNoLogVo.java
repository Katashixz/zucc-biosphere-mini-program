package com.biosphere.library.vo;

import java.util.List;

/**
 * @Author Administrator
 * @Date 2022/11/18 11:05
 * @Version 1.0
 */
public class ExceptionNoLogVo extends RuntimeException{

    private RespBeanEnum e;

    private Object errorData;

    public ExceptionNoLogVo() {
        super();
    }

    public ExceptionNoLogVo(RespBeanEnum res) {
        super(res.getMessage());
        this.e = res;
        // super(res.getMessage());
    }

    public ExceptionNoLogVo(RespBeanEnum res, List<String> errorData) {
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

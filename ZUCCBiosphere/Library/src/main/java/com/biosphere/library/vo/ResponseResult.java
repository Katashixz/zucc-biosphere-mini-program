package com.biosphere.library.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Author 三更
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static ResponseResult success(){
        return new ResponseResult(com.biosphere.library.vo.RespBeanEnum.SUCCESS.getCode(), com.biosphere.library.vo.RespBeanEnum.SUCCESS.getMessage(),null);
    }

    public static ResponseResult success(Object obj){
        return new ResponseResult(com.biosphere.library.vo.RespBeanEnum.SUCCESS.getCode(), com.biosphere.library.vo.RespBeanEnum.SUCCESS.getMessage(),obj);
    }

    public static ResponseResult error(com.biosphere.library.vo.RespBeanEnum respBeanEnum){
        return new ResponseResult(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }

    public static ResponseResult error(com.biosphere.library.vo.RespBeanEnum respBeanEnum, Object obj){
        return new ResponseResult(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
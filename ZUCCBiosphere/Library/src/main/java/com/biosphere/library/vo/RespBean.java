package com.biosphere.library.vo;

/**
 * @author  
 * @description: 公共返回对象
 * @date 2022/1/22 17:31
 */
public class RespBean {

    private Integer code;
    private String message;
    private Object obj;



    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public RespBean() {

    }

    public RespBean(Integer code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    /**
     * 功能描述: 成功返回结果 
     * @param:
     * @return: com.bank.seckill.vo.RespBean 
     * @author  
     * @date: 2022/1/22 17:45
     */ 
    public static RespBean success(){
        return new RespBean(com.biosphere.library.vo.RespBeanEnum.SUCCESS.getCode(), com.biosphere.library.vo.RespBeanEnum.SUCCESS.getMessage(),null);
    }

    public static RespBean success(Object obj){
        return new RespBean(com.biosphere.library.vo.RespBeanEnum.SUCCESS.getCode(), com.biosphere.library.vo.RespBeanEnum.SUCCESS.getMessage(),obj);
    }
    
    /** 
     * 功能描述: 失败返回结果 
     * @param: respBeanEnum 
     * @return: com.bank.seckill.vo.RespBean 
     * @author  
     * @date: 2022/1/22 22:12
     */ 
    public static RespBean error(com.biosphere.library.vo.RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }

    public static RespBean error(com.biosphere.library.vo.RespBeanEnum respBeanEnum, Object obj){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
    }

}

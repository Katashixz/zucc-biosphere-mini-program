package com.biosphere.library.vo;

/**
 * @author  
 * @description: 公共返回对象枚举
 * @date 2022/1/22 17:31
 */
public enum RespBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    NO_LOGIN(300,"请先登录"),
    LOGIN_EXPIRED(301,"登录信息过期"),
    //登录模块
    EMPTY_CODE(1001,"请检查传入信息！"),
    CODE_GET_ERROR(1002,"获取OPENID失败！"),
    TOKEN_ILLEGAL(1003,"token非法"),
    GET_USERINFO_ERROR(1004,"获取用户信息失败"),
    REPEAT_CHECKIN(1005,"重复签到"),
    CHECKIN_INSERT_ERROR(1006,"签到记录插入失败"),
    //百科模块
    GET_WIKI_ERROR(2001,"获取百科信息失败"),
    GET_GUIDE_ERROR(2002,"获取养护指南信息失败"),
    GET_DETAIL_ERROR(2003,"获取详情信息失败"),
    //社区模块
    LOAD_POST_ERROR(3001,"加载帖子信息失败"),
    LOAD_POSTDETAIL_ERROR(3002,"该贴不存在或已被删除"),
    LOAD_COMMENT_ERROR(3003,"加载评论失败"),

    ;

    private final Integer code;
    private final String message;

    RespBeanEnum(Integer i, String message) {

        this.message = message;
        this.code = i;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RespBeanEnum{" +
                "code=" + code +
                ", massage='" + message + '\'' +
                '}';
    }
}

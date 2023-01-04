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
    NO_LOGIN(300,"登录过期，请重新登录"),
    ERROR_INFO(400,"请检查传入信息"),

    //登录模块
    CODE_GET_ERROR(1002,"获取OPENID失败"),
    TOKEN_ILLEGAL(1003,"token非法"),
    GET_USERINFO_ERROR(1004,"获取用户信息失败"),
    REPEAT_CHECKIN(1005,"重复签到了哦"),
    CHECKIN_INSERT_ERROR(1006,"签到记录插入失败"),
    NO_POST(1007,"还未发布过帖子哦"),
    NO_Star(1008,"还未收藏过帖子哦"),
    NO_Comment(1009,"还未评论过帖子哦"),
    //百科模块
    GET_WIKI_ERROR(2001,"获取百科失败"),
    GET_GUIDE_ERROR(2002,"获取养护指南信息失败"),
    GET_DETAIL_ERROR(2003,"获取详情失败"),
    //社区模块
    LOAD_POST_ERROR(3001,"加载帖子失败"),
    LOAD_HOT_POST_ERROR(3004,"加载热帖失败"),
    EMPTY_HOT_POST(3008,"暂无热帖"),
    LOAD_POSTDETAIL_ERROR(3002,"该贴不存在或已被删除"),
    LOAD_COMMENT_ERROR(3003,"加载评论失败"),
    UPLOAD_IMG_ERROR(3005,"上传图片失败"),
    UPLOAD_POST_ERROR(3006,"上传帖子失败"),
    CHANGE_LIKE_STATUS_ERROR(3307, "点赞失败"),
    SENSITIVE_WORDS(3309, "内容包含敏感词"),
    UPLOAD_COMMENT_ERROR(3310, "上传评论失败"),
    INFO_ERROR(3311, "传入信息有空值"),
    STAR_INSERT_ERROR(3312,"收藏帖子失败"),
    REPEAT_STAR(3313,"已经收藏过啦"),
    DELETE_ERROR(3314,"删除出错"),
    //其他
    INSERT_REWARD_ERROR(4001,"能量值插入失败"),
    UPDATE_USER_ENERGY_ERROR(4002,"用户能量值更新失败"),
    USER_ID_ERROR(4003,"用户ID异常"),
    ENERGY_NOT_ENOUGH(4004,"能量值不足啦"),
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

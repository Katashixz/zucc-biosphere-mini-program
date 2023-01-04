package com.biosphere.library.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author Administrator
 * @Date 2022/8/12 11:20
 * @Version 1.0
 */
public class LoginVo implements Serializable {

    @NotBlank(message = "code值不能为空")
    private String code;

    // @URL(message = "头像地址有误")
    private String avatarUrl;

    // @NotBlank(message = "名称不能为空")
    // @Length(min = 1,max = 15,message = "昵称长度限制在1-15位")
    private String nickName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

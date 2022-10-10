package com.biosphere.library.vo;

import java.io.Serializable;

/**
 * @Author Administrator
 * @Date 2022/10/10 14:35
 * @Version 1.0
 */
public class RewardVo implements Serializable {
    private Integer userID;

    private Integer toUserID;

    private Integer point;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getToUserID() {
        return toUserID;
    }

    public void setToUserID(Integer toUserID) {
        this.toUserID = toUserID;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}

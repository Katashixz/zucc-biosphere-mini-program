package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyh
 * @since 2022-08-10
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 微信openID
     */
    @TableId("openID")
    private String openID;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户名
     */
    @TableField("userName")
    private String userName;

    /**
     * 用户头像url
     */
    @TableField("avatarUrl")
    private String avatarUrl;

    /**
     * 最后登录时间
     */
    @TableField("latestLoginTime")
    private Date latestLoginTime;

    /**
     * 能量值
     */
    @TableField("energyPoint")
    private Integer energyPoint;

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public Date getLatestLoginTime() {
        return latestLoginTime;
    }

    public void setLatestLoginTime(Date latestLoginTime) {
        this.latestLoginTime = latestLoginTime;
    }
    public Integer getEnergyPoint() {
        return energyPoint;
    }

    public void setEnergyPoint(Integer energyPoint) {
        this.energyPoint = energyPoint;
    }

    @Override
    public String toString() {
        return "User{" +
                "openID=" + openID +
                ", id=" + id +
                ", userName=" + userName +
                ", avatarUrl=" + avatarUrl +
                ", latestLoginTime=" + latestLoginTime +
                ", energyPoint=" + energyPoint +
                "}";
    }
}

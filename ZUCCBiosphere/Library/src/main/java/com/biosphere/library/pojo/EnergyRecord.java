package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-08-18
 */
public class EnergyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 打赏记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 获得者ID
     */
    @TableField("userID")
    private Integer userID;

    /**
     * 打赏者ID(如果是自己则代表每日登录获得)
     */
    @TableField("toUserID")
    private Integer toUserID;

    /**
     * 获取日期
     */
    @TableField("getDate")
    private Date getDate;

    /**
     * 打赏值
     */
    private Integer point;

    /**
     * 打赏类型，1代表打赏获取，0代表每日登录，2代表完成任务
     */
    private Integer type;

    /**
     * 0代表未查看，1代表已查看
     */
    private Integer ischecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }
    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getIschecked() {
        return ischecked;
    }

    public void setIschecked(Integer ischecked) {
        this.ischecked = ischecked;
    }

    @Override
    public String toString() {
        return "Energyrecord{" +
            "id=" + id +
            ", userID=" + userID +
            ", toUserID=" + toUserID +
            ", getDate=" + getDate +
            ", point=" + point +
            ", type=" + type +
            ", ischecked=" + ischecked +
        "}";
    }
}

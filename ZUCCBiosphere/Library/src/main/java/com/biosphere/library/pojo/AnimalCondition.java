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
 * @since 2023-04-02
 */
public class AnimalCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 状况来源的用户ID
     */
    @TableField("sourceID")
    private Integer sourceID;

    /**
     * 动物ID
     */
    @TableField("targetID")
    private Integer targetID;

    /**
     * 若行为类型为喂食(2)，则记录食品id
     */
    @TableField("shopID")
    private Integer shopID;

    /**
     * 行为，1代表遇见，2代表喂食
     */
    private Integer action;

    /**
     * 创建时间
     */
    @TableField("createdAt")
    private Date createdAt;

    /**
     * 状况图片
     */
    @TableField("imageUrl")
    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSourceID() {
        return sourceID;
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }
    public Integer getTargetID() {
        return targetID;
    }

    public void setTargetID(Integer targetID) {
        this.targetID = targetID;
    }
    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "AnimalCondition{" +
            "id=" + id +
            ", sourceID=" + sourceID +
            ", targetID=" + targetID +
            ", shopID=" + shopID +
            ", action=" + action +
            ", createdAt=" + createdAt +
            ", imageUrl=" + imageUrl +
        "}";
    }
}

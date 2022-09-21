package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyh
 * @since 2022-09-05
 */
public class PlantsWiki implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 植物ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 学名ID
     */
    @TableField("scientificNameID")
    private Integer scientificNameID;

    /**
     * 名字
     */
    @TableField("nickName")
    private String nickName;

    /**
     * 我校分布地
     */
    private String location;

    /**
     * 图片url
     */
    @TableField("image")
    private String image;

    /**
     * 别名
     */
    @TableField("alias")
    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getScientificNameID() {
        return scientificNameID;
    }

    public void setScientificNameID(Integer scientificNameID) {
        this.scientificNameID = scientificNameID;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }

    @Override
    public String toString() {
        return "PlantsWiki{" +
            "id=" + id +
            ", scientificNameID=" + scientificNameID +
            ", nickName=" + nickName +
            ", location=" + location +
            ", image=" + image +
            ", alias=" + alias +
        "}";
    }
}

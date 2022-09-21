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
 * @since 2022-09-02
 */
public class AnimalsWiki implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 外号
     */
    @TableField("nickName")
    private String nickName;

    /**
     * 学名ID
     */
    @TableField("scientificNameID")
    private Integer scientificNameID;

    /**
     * 性别
     */
    private String sex;

    /**
     * 健康状况
     */
    private String condition;

    /**
     * 绝育情况
     */
    private String sterilization;

    /**
     * 性格
     */
    private String character;

    /**
     * 外貌
     */
    private String appearance;

    /**
     * 亲属关系
     */
    private String relationship;

    /**
     * 图片url
     */
    private String image;

    /**
     * 亲属关系动物ID
     */
    @TableField("relationID")
    private String relationID;

    /**
     * 出没地
     */
    @TableField("location")
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public Integer getScientificNameID() {
        return scientificNameID;
    }

    public void setScientificNameID(Integer scientificNameID) {
        this.scientificNameID = scientificNameID;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getSterilization() {
        return sterilization;
    }

    public void setSterilization(String sterilization) {
        this.sterilization = sterilization;
    }
    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

    @Override
    public String toString() {
        return "Animalswiki{" +
            "id=" + id +
            ", nickName=" + nickName +
            ", scientificNameID=" + scientificNameID +
            ", sex=" + sex +
            ", condition=" + condition +
            ", sterilization=" + sterilization +
            ", character=" + character +
            ", appearance=" + appearance +
            ", relationship=" + relationship +
            ", image=" + image +
            ", relationID=" + relationID +
        "}";
    }
}

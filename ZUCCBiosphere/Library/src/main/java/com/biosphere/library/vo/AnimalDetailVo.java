package com.biosphere.library.vo;

import java.io.Serializable;

/**
 * @Author Administrator
 * @Date 2022/9/6 17:50
 * @Version 1.0
 */
public class AnimalDetailVo implements Serializable {
    private String nickName;
    private String sex;
    private String condition;
    private String sterilization;
    private String character;
    private String appearance;
    private String relationship;
    private String image;
    private String relationID;
    private String name;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

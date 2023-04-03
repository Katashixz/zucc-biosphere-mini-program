package com.biosphere.library.vo;

import io.swagger.models.auth.In;

import java.io.Serializable;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/3/29 19:43
 */
public class AdoptPageVo implements Serializable {

    private Integer ID;

    private String nickName;

    private String sex;

    private String condition;

    private String sterilization;

    private String character;

    private String image;

    private String location;

    private String scientificName;

    private Integer adoptCondition;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public Integer getAdoptCondition() {
        return adoptCondition;
    }

    public void setAdoptCondition(Integer adoptCondition) {
        this.adoptCondition = adoptCondition;
    }
}

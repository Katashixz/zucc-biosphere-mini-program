package com.biosphere.library.vo;

/**
 * @Author Administrator
 * @Date 2022/9/2 15:34
 * @Version 1.0
 */
public class MainPageDataVo {
    private String familyID;
    private String image;

    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AnimalDataVo{" +
                "familyID='" + familyID + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

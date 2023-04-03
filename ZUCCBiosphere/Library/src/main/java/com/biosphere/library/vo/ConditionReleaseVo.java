package com.biosphere.library.vo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/4/3 10:47
 */
public class ConditionReleaseVo implements Serializable {

    @NotNull
    private Integer sourceID;

    @NotNull
    private Integer targetID;

    @NotNull
    private Integer action;

    @NotNull
    private String date;

    @NotNull
    private String time;

    @NotNull
    private String imageUrl;


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

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

package com.biosphere.library.vo;

import io.swagger.models.auth.In;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/3/22 16:53
 */
public class NotifyMsgVo {

    // 行为来源用户Id
    private Integer sourceId;

    private String sourceName;

    private String sourceAvatar;

    private Integer action;

    private String content;

    private Integer objectType;

    private Integer objectId;


    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceAvatar() {
        return sourceAvatar;
    }

    public void setSourceAvatar(String sourceAvatar) {
        this.sourceAvatar = sourceAvatar;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
}

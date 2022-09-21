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
public class CuringGuide implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 养护指南ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 条目内容
     */
    private String content;

    /**
     * 关键词高亮内容
     */
    private String keyContent;

    /**
     * 条目类型，1代表绿色建议，2代表黄色提醒，0代表红色禁止
     */
    private Integer type;

    /**
     * 属于类型，用字符串作键类型
     */
    @TableField("belongToWhichType")
    private String belongToWhichType;

    /**
     * 属于哪个类型的哪个具体生物
     */
    @TableField("belongToWhich")
    private Integer belongToWhich;

    public String getKeyContent() {
        return keyContent;
    }

    public void setKeyContent(String keyContent) {
        this.keyContent = keyContent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getBelongToWhichType() {
        return belongToWhichType;
    }

    public void setBelongToWhichType(String belongToWhichType) {
        this.belongToWhichType = belongToWhichType;
    }
    public Integer getBelongToWhich() {
        return belongToWhich;
    }

    public void setBelongToWhich(Integer belongToWhich) {
        this.belongToWhich = belongToWhich;
    }

    @Override
    public String toString() {
        return "CuringGuide{" +
            "id=" + id +
            ", content=" + content +
            ", type=" + type +
            ", belongToWhichType=" + belongToWhichType +
            ", belongToWhich=" + belongToWhich +
            ", keyContent=" + keyContent +
        "}";
    }
}

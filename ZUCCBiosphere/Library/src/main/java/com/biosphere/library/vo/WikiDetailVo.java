package com.biosphere.library.vo;

import java.util.List;
import java.util.Map;

/**
 * @Author Administrator
 * @Date 2022/9/7 10:19
 * @Version 1.0
 */
public class WikiDetailVo {

    private List<String> title;

    private List<String> content;

    private String nickName;

    private List<String> imageList;

    private List<Map<String, String>> relation;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<Map<String, String>> getRelation() {
        return relation;
    }

    public void setRelation(List<Map<String, String>> relation) {
        this.relation = relation;
    }
}

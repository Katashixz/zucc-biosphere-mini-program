package com.biosphere.library.vo;

import com.biosphere.library.pojo.CuringGuide;

import java.util.List;

/**
 * @Author Administrator
 * @Date 2022/9/5 16:55
 * @Version 1.0
 */
public class CuringGuideWithKeyContent extends CuringGuide {
    //分割关键词后的内容
    private char[] contentFinal;

    //关键字下标
    private int[] keyIndex;

    public int[] getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(int[] keyIndex) {
        this.keyIndex = keyIndex;
    }
    public CuringGuideWithKeyContent(CuringGuide queryRe) {
        this.setContent(queryRe.getContent());
        this.setKeyContent(queryRe.getKeyContent());
        this.setId(queryRe.getId());
        this.setBelongToWhich(queryRe.getBelongToWhich());
        this.setType(queryRe.getType());
    }

    public char[] getContentFinal() {
        return contentFinal;
    }

    public void setContentFinal(char[] contentFinal) {
        this.contentFinal = contentFinal;
    }
}

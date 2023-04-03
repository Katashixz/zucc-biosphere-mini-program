package com.biosphere.library.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyh
 * @since 2023-04-01
 */
public class ShopItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 创建日期
     */
    @TableField("createdAt")
    private Date createdAt;

    /**
     * 图片地址
     */
    @TableField("imageUrl")
    private String imageUrl;

    /**
     * 用途，1代表是云喂养模块，2代表商城模块
     */
    @TableField("`usage`")
    private Integer usage;

    /**
     * 库存
     */
    @TableField("`stock`")
    private Integer stock;

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "ShopItem{" +
            "id=" + id +
            ", name=" + name +
            ", price=" + price +
            ", createdAt=" + createdAt +
            ", imageUrl=" + imageUrl +
            ", usage=" + usage +
            ", stock=" + stock +
        "}";
    }
}

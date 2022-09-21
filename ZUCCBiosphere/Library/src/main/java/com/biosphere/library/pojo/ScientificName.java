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
public class ScientificName implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学名ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学名
     */
    private String name;

    /**
     * 种ID
     */
    @TableField("speciesID")
    private String speciesID;

    /**
     * 属ID
     */
    @TableField("genusID")
    private String genusID;

    /**
     * 科ID
     */
    @TableField("familyID")
    private String familyID;

    /**
     * 目ID
     */
    @TableField("orderID")
    private String orderID;

    /**
     * 纲ID
     */
    @TableField("classID")
    private String classID;

    /**
     * 门ID
     */
    @TableField("phylumID")
    private String phylumID;

    /**
     * 界ID
     */
    @TableField("domainID")
    private String domainID;

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
    public String getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(String speciesID) {
        this.speciesID = speciesID;
    }
    public String getGenusID() {
        return genusID;
    }

    public void setGenusID(String genusID) {
        this.genusID = genusID;
    }
    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
    public String getPhylumID() {
        return phylumID;
    }

    public void setPhylumID(String phylumID) {
        this.phylumID = phylumID;
    }
    public String getDomainID() {
        return domainID;
    }

    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }

    @Override
    public String toString() {
        return "Scientificname{" +
            "id=" + id +
            ", name=" + name +
            ", speciesID=" + speciesID +
            ", genusID=" + genusID +
            ", familyID=" + familyID +
            ", orderID=" + orderID +
            ", classID=" + classID +
            ", phylumID=" + phylumID +
            ", domainID=" + domainID +
        "}";
    }
}

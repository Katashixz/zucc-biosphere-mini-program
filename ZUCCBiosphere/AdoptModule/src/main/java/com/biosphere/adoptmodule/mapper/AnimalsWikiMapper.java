package com.biosphere.adoptmodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.vo.AdoptPageVo;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2023-03-29
 */
public interface AnimalsWikiMapper extends BaseMapper<AnimalsWiki> {


    @Select("SELECT w.ID, nickName, sex, `condition`, sterilization, `character`, image, location, IFNULL(`name`,\"未知\") as scientificName, adoptCondition\n" +
            "FROM animals_wiki w\n" +
            "LEFT OUTER JOIN scientific_name s ON w.scientificNameID = s.id\n" +
            "WHERE w.adoptCondition = 0")
    List<AdoptPageVo> getHomelessAnimalList();

}

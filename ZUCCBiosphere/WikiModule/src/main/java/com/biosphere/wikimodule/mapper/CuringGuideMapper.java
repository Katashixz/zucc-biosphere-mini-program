package com.biosphere.wikimodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.CuringGuide;
import com.biosphere.library.vo.CommentVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
public interface CuringGuideMapper extends BaseMapper<CuringGuide> {
    @Select("SELECT p.ID as id, p.nickName, p.image, '植物' as type\n" +
            "FROM plants_wiki p\n" +
            "\tleft outer join scientific_name s on p.scientificNameID = s.id\n" +
            "WHERE p.nickName like CONCAT('%',#{content},'%') or p.alias like CONCAT('%',#{content},'%') or s.name like CONCAT('%',#{content},'%')")
    List<Map<String, Object>> plantSearch(@Param("content") String content);

    @Select("select a.id as id, a.nickName, a.image, '动物' as type\n" +
            "from animals_wiki a\n" +
            "left outer join scientific_name s on a.scientificNameID = s.id\n" +
            "WHERE a.nickName like CONCAT('%',#{content},'%') or s.name like CONCAT('%',#{content},'%')")
    List<Map<String, Object>> animalSearch(@Param("content") String content);
}

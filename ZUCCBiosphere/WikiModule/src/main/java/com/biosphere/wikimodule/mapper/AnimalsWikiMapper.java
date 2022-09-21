package com.biosphere.wikimodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.vo.MainPageDataVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
public interface AnimalsWikiMapper extends BaseMapper<AnimalsWiki> {
    /**
     * 功能描述: 加载百科主页动物信息
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/2 10:56
     */
    @Select("SELECT family.familyID,image\n" +
            "FROM animals_wiki, (\n" +
            "SELECT id,familyID\n" +
            "from scientific_name\n" +
            "GROUP BY familyID\n" +
            ") family\n" +
            "WHERE scientificNameID = family.id\n" +
            "GROUP BY family.familyID")
    List<MainPageDataVo> getMainPageAnimalData();

}

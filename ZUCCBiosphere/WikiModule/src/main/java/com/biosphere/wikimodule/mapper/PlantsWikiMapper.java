package com.biosphere.wikimodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.PlantsWiki;
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
public interface PlantsWikiMapper extends BaseMapper<PlantsWiki> {
    /**
     * 功能描述: 加载百科主页动物信息
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/5 09:59
     */
    @Select("SELECT family.familyID,image\n" +
            "FROM plants_wiki,(SELECT id, familyID FROM scientific_name GROUP BY familyID) family \n" +
            "WHERE scientificNameID = family.id\n" +
            "GROUP BY family.familyID")
    List<MainPageDataVo> getMainPagePlantsData();
}

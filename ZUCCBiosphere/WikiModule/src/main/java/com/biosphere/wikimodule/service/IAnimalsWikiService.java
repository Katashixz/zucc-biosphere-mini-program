package com.biosphere.wikimodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.vo.MainPageDataVo;
import com.biosphere.library.vo.WikiDetailVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
public interface IAnimalsWikiService extends IService<AnimalsWiki> {

    /**
     * 功能描述: 获取百科首页动物分类
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/2 13:58
     */
    List<MainPageDataVo> getMainPageAnimalData();

    /**
     * 功能描述: 获取养护指南动物列表
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/5 13:40
     */
    List<AnimalsWiki> getCuringGuidePageAnimalList(String familyID);

    /**
     * 功能描述: 获取动物详情
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/7 10:29
     */
    WikiDetailVo getAnimalDetail(String ID);

}

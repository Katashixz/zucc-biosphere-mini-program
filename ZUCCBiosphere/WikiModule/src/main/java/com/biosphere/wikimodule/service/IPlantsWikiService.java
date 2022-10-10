package com.biosphere.wikimodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.PlantsWiki;
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
public interface IPlantsWikiService extends IService<PlantsWiki> {
    /**
     * 功能描述: 获取百科首页植物分类
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/5 09:56
     */
    List<MainPageDataVo> getMainPagePlantsData();

    /**
     * 功能描述: 获取养护指南植物列表
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/5 14:55
     */
    List<PlantsWiki> getCuringGuidePagePlantsList(String familyID);

    /**
     * 功能描述: 获取植物详情
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/7 14:36
     */
    WikiDetailVo getPlantDetail(String ID);

}

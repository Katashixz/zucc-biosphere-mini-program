package com.biosphere.wikimodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.CuringGuide;
import com.biosphere.library.vo.CuringGuideWithKeyContent;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
public interface ICuringGuideService extends IService<CuringGuide> {

    /**
     * 功能描述: 获取养护指南信息
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/5 10:40
     */
    List<CuringGuideWithKeyContent> getCuringGuideData(String familyID);

    /**
     * 功能描述: 获取百科搜索结果
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/10/9 17:05
     */
    List<Map<String,Object>> getSearchResult(String content);
}

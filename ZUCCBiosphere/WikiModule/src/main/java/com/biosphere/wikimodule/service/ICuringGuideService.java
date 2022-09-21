package com.biosphere.wikimodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.CuringGuide;
import com.biosphere.library.vo.CuringGuideWithKeyContent;

import java.util.List;

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

}

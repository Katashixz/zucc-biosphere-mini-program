package com.biosphere.adoptmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.vo.AdoptPageVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2023-03-29
 */
public interface IAnimalsWikiService extends IService<AnimalsWiki> {

    /**
     * 功能描述: 保存领养主页信息到数据库缓存
     * @param:
     * @return: void
     * @author hyh
     * @date: 2023/3/29 19:51
     */
    void saveAdoptCache();

    /**
     * 功能描述: 加载领养信息
     * @param:
     * @return: void
     * @author hyh
     * @date: 2023/3/29 19:51
     */
    List<AdoptPageVo> getAdoptPageInfo();



}

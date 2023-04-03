package com.biosphere.adoptmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.AnimalCondition;
import com.biosphere.library.pojo.ViewTodayCondition;
import com.biosphere.library.vo.ConditionReleaseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2023-04-01
 */
public interface IAnimalConditionService extends IService<AnimalCondition> {

    /**
     * 功能描述: 插入今日状况
     * @param: animalCondition
     * @return: void
     * @author hyh
     * @date: 2023/4/1 18:15
     */
    void saveAnimalContidion(AnimalCondition animalCondition);

    /**
     * 功能描述: 获取某日状况
     * @param: targetID date
     * @return: java.util.List<com.biosphere.library.pojo.ViewTodayCondition>
     * @author hyh
     * @date: 2023/4/2 15:48
     */
    List<ViewTodayCondition> getTodayCondition(Integer targetID, String date);

    void saveConditionPersonally(ConditionReleaseVo conditionReleaseVo);

}

package com.biosphere.adoptmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.adoptmodule.mapper.AnimalConditionMapper;
import com.biosphere.adoptmodule.service.IAnimalConditionService;
import com.biosphere.library.pojo.AnimalCondition;
import com.biosphere.library.pojo.ViewTodayCondition;
import com.biosphere.library.util.CommonUtil;
import com.biosphere.library.vo.ConditionReleaseVo;
import com.biosphere.library.vo.ExceptionLogVo;
import com.biosphere.library.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2023-04-01
 */
@Service
public class AnimalConditionServiceImpl extends ServiceImpl<AnimalConditionMapper, AnimalCondition> implements IAnimalConditionService {

    @Autowired
    private AnimalConditionMapper animalConditionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAnimalContidion(AnimalCondition animalCondition) {
        animalCondition.setCreatedAt(new Date(System.currentTimeMillis()));
        int insert = animalConditionMapper.insert(animalCondition);
        if (insert <= 0) {
            throw new ExceptionLogVo(RespBeanEnum.ERROR);
        }
    }

    @Override
    public List<ViewTodayCondition> getTodayCondition(Integer targetID, String date) {
        List<ViewTodayCondition> todayCondition = animalConditionMapper.getTodayCondition(targetID, date);
        return todayCondition;
    }

    @Override
    public void saveConditionPersonally(ConditionReleaseVo conditionReleaseVo) {
        Date date = new Date();
        try {
            date = CommonUtil.toDate(conditionReleaseVo.getDate(), conditionReleaseVo.getTime());
        }catch (ParseException e){
            throw new ExceptionLogVo(RespBeanEnum.FORMAT_ERROR);
        }
        AnimalCondition animalCondition = new AnimalCondition();
        animalCondition.setAction(conditionReleaseVo.getAction());
        animalCondition.setImageUrl(conditionReleaseVo.getImageUrl());
        animalCondition.setSourceID(conditionReleaseVo.getSourceID());
        animalCondition.setTargetID(conditionReleaseVo.getTargetID());
        animalCondition.setCreatedAt(date);
        int insert = animalConditionMapper.insert(animalCondition);
        if (insert <= 0) {
            throw new ExceptionLogVo(RespBeanEnum.ERROR);
        }
    }
}

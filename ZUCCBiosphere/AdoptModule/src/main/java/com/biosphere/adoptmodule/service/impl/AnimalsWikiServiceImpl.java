package com.biosphere.adoptmodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.adoptmodule.mapper.AnimalsWikiMapper;
import com.biosphere.adoptmodule.service.IAnimalsWikiService;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.vo.AdoptPageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2023-03-29
 */
@Service
public class AnimalsWikiServiceImpl extends ServiceImpl<AnimalsWikiMapper, AnimalsWiki> implements IAnimalsWikiService {

    @Autowired
    private AnimalsWikiMapper animalsWikiMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void saveAdoptCache() {
        List<AdoptPageVo> homelessAnimalList = animalsWikiMapper.getHomelessAnimalList();
        for (AdoptPageVo adoptPageVo : homelessAnimalList) {
            adoptPageVo.setImage(adoptPageVo.getImage().split("，")[0]);
        }
        redisTemplate.opsForValue().set("homeless", homelessAnimalList);

    }

    @Override
    public List<AdoptPageVo> getAdoptPageInfo() {
        return (List<AdoptPageVo>) redisTemplate.opsForValue().get("homeless");
    }
}

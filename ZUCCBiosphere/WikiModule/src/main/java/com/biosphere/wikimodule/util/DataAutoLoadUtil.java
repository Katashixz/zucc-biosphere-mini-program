package com.biosphere.wikimodule.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.pojo.CuringGuide;
import com.biosphere.library.pojo.PlantsWiki;
import com.biosphere.library.pojo.ScientificName;
import com.biosphere.wikimodule.mapper.AnimalsWikiMapper;
import com.biosphere.wikimodule.mapper.CuringGuideMapper;
import com.biosphere.wikimodule.mapper.PlantsWikiMapper;
import com.biosphere.wikimodule.mapper.ScientificNameMapper;
import com.biosphere.library.vo.MainPageDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @Date 2022/9/2 10:52
 * @Version 1.0
 */

@Slf4j
@Component
public class DataAutoLoadUtil {

    /**
     * 功能描述: 定时任务，每半小时加载一次数据库中百科数据到Redis
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/2 10:24
     */
    @Autowired
    private AnimalsWikiMapper animalsWikiMapper;

    @Autowired
    private ScientificNameMapper scientificNameMapper;

    @Autowired
    private PlantsWikiMapper plantsWikiMapper;

    @Autowired
    private CuringGuideMapper curingGuideMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Scheduled(cron = "0 0,30 * * * ?")
    public void wikiMainPageData(){
        log.info("定时加载主页百科数据任务启动");
        //主页动物数据
        List<MainPageDataVo> animalMainPageDataVoList = animalsWikiMapper.getMainPageAnimalData();
        //主页植物数据
        List<MainPageDataVo> plantsMainPageDataVoList = plantsWikiMapper.getMainPagePlantsData();
        //处理一下图片
        for (MainPageDataVo item: animalMainPageDataVoList){
            //取第一个
            item.setImage(item.getImage().split("，")[0]);
        }
        for (MainPageDataVo item: plantsMainPageDataVoList){
            //取第一个
            item.setImage(item.getImage().split("，")[0]);
        }
        //养护指南数据
        List<CuringGuide> curingGuideList = curingGuideMapper.selectList(new QueryWrapper<CuringGuide>().select("id,content,type,belongToWhichType,keyContent").orderByAsc("type"));
        List<AnimalsWiki> animalsWikiList = animalsWikiMapper.selectList(new QueryWrapper<AnimalsWiki>().select("ID,nickName,scientificNameID,sex,`condition`,sterilization,`character`,appearance,relationship,image,relationID,location"));
        List<PlantsWiki> plantsWikiList = plantsWikiMapper.selectList(new QueryWrapper<PlantsWiki>().select("ID,scientificNameID,nickName,location,image,alias"));
        Map<String,Object> animalsWikiMap = new HashMap<>();
        Map<String,Object> plantsWikiMap = new HashMap<>();
        for (AnimalsWiki animalsWiki : animalsWikiList) {
            animalsWikiMap.put(animalsWiki.getId().toString(), animalsWiki);
        }
        for (PlantsWiki plantsWiki : plantsWikiList) {
            plantsWikiMap.put(plantsWiki.getId().toString(), plantsWiki);

        }
        //学名数据
        List<ScientificName> scientificNameList = scientificNameMapper.selectList(new QueryWrapper<ScientificName>().select("id,name,speciesID,genusID,familyID,orderID,classID,phylumID,domainID"));
        //学名->条目id Map和id->信息 Map的初始化
        Map<String, List<Integer>> scientificNameToScientificID = new HashMap<>();
        Map<String, Object> scientificNameMap = new HashMap<>();
        for (ScientificName scientificName : scientificNameList) {
            scientificNameMap.put(scientificName.getId().toString(), scientificName);
            List<Integer> temp = new ArrayList<>();
            if (scientificNameToScientificID.containsKey(scientificName.getFamilyID())) {
                temp = scientificNameToScientificID.get(scientificName.getFamilyID());
                temp.add(scientificName.getId());
                scientificNameToScientificID.replace(scientificName.getFamilyID(),temp);
            }else {
                temp.add(scientificName.getId());
                scientificNameToScientificID.put(scientificName.getFamilyID(),temp);
            }


        }
        // Map animalsMap = new HashMap<>();
        // for (AnimalsWiki animalsWiki : animalsWikiList) {
        //     animalsMap.put(animalsWiki.getId().toString(),animalsWiki.getScientificNameID().toString());
        // }
        // Map plantsMap = new HashMap<>();
        // for (PlantsWiki plantsWiki : plantsWikiList) {
        //     plantsMap.put(plantsWiki.getId().toString(),plantsWiki.getScientificNameID().toString());
        // }
        try {
            //防止发生缓存雪崩，设置不同过期时间。同时防止缓存击穿，设置更新时间短于过期时间
            redisTemplate.opsForValue().set("animalDataVoList", animalMainPageDataVoList,40, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("plantsDataVoList", plantsMainPageDataVoList,41, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("curingGuideList",curingGuideList,42, TimeUnit.MINUTES);
            redisTemplate.opsForHash().putAll("scientificNameToScientificID",scientificNameToScientificID);
            redisTemplate.opsForHash().putAll("animalsWikiMap",animalsWikiMap);
            redisTemplate.opsForHash().putAll("plantsWikiMap",plantsWikiMap);
            redisTemplate.opsForHash().putAll("scientificNameMap",scientificNameMap);
            //设置Hash的过期时间
            redisTemplate.expire("scientificNameToScientificID",43,TimeUnit.MINUTES);
            redisTemplate.expire("animalsWikiMap",44,TimeUnit.MINUTES);
            redisTemplate.expire("plantsWikiMap",45,TimeUnit.MINUTES);
            redisTemplate.expire("scientificNameMap",46,TimeUnit.MINUTES);
            // redisTemplate.opsForHash().putAll("plantsMap",plantsMap);
        }catch (Exception e) {
            log.error("[Redis错误，定时加载任务失败]");
            e.printStackTrace();

            return;
        }
        log.info("定时加载主页百科数据任务结束");

    }
}

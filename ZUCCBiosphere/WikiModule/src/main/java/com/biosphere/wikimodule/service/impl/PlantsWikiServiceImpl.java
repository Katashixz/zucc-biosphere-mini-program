package com.biosphere.wikimodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.pojo.PlantsWiki;
import com.biosphere.library.pojo.ScientificName;
import com.biosphere.library.vo.MainPageDataVo;
import com.biosphere.library.vo.WikiDetailVo;
import com.biosphere.wikimodule.mapper.PlantsWikiMapper;
import com.biosphere.wikimodule.service.IPlantsWikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
@Slf4j
@Service
public class PlantsWikiServiceImpl extends ServiceImpl<PlantsWikiMapper, PlantsWiki> implements IPlantsWikiService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<MainPageDataVo> getMainPagePlantsData() {

        try{
            List<MainPageDataVo> mainPageDataVoList = (List<MainPageDataVo>) redisTemplate.opsForValue().get("plantsDataVoList");
            return mainPageDataVoList;

        }catch (Exception e){
            log.error("获取植物主页信息失败:{}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<PlantsWiki> getCuringGuidePagePlantsList(String familyID) {
        try{
            List<Integer> scientificIDList = (List<Integer>) redisTemplate.opsForHash().get("scientificNameToScientificID",familyID);
            List<PlantsWiki> res = new ArrayList<>();
            Map<String, Object> plantsMap = (Map<String, Object>) redisTemplate.opsForHash().entries("plantsWikiMap");

            for (Object values: plantsMap.values()){
                PlantsWiki temp = (PlantsWiki) values;
                if (scientificIDList.contains(temp.getScientificNameID())) {
                    //处理图片
                    temp.setImage(temp.getImage().split("，")[0]);
                    res.add(temp);
                }
            }
            return res;


        }catch (Exception e){
            log.error("获取养护指南植物列表失败：{}",e);
            return null;

        }
    }

    @Override
    public WikiDetailVo getPlantDetail(String ID) {
        PlantsWiki plantsWikis = new PlantsWiki();
        ScientificName scientificName = new ScientificName();
        try {
            plantsWikis = (PlantsWiki) redisTemplate.opsForHash().get("plantsWikiMap",ID);
            scientificName = (ScientificName) redisTemplate.opsForHash().get("scientificNameMap",plantsWikis.getScientificNameID().toString());
        }catch (Exception e){
            log.error("获取植物详情失败：{}",e);
            return null;
        }
        WikiDetailVo wikiDetailVo = new WikiDetailVo();
        List<String> title = new ArrayList<>();
        List<String> content = new ArrayList<>();
        List<String> imageList = new ArrayList<>();
        //顺序一定要一致
        title.add("学名");
        title.add("别名");
        title.add("我校分布地");
        title.add("门");
        title.add("纲");
        title.add("目");
        title.add("科");
        title.add("属");
        title.add("种");
        content.add(scientificName.getName());
        content.add(plantsWikis.getAlias());
        content.add(plantsWikis.getLocation());
        content.add(scientificName.getPhylumID());
        content.add(scientificName.getClassID());
        content.add(scientificName.getOrderID());
        content.add(scientificName.getFamilyID());
        content.add(scientificName.getGenusID());
        content.add(scientificName.getSpeciesID());
        imageList = Arrays.asList(plantsWikis.getImage().split("，"));
        //关系处理(植物没有)
        List<Map<String, String>> relation = new ArrayList<>();


        //汇总数据
        wikiDetailVo.setImageList(imageList);
        wikiDetailVo.setNickName(plantsWikis.getNickName());
        wikiDetailVo.setContent(content);
        wikiDetailVo.setTitle(title);
        wikiDetailVo.setRelation(relation);

        return wikiDetailVo;

    }
}

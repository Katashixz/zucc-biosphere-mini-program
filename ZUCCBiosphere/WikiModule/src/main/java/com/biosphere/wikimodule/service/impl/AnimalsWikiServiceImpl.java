package com.biosphere.wikimodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.library.pojo.ScientificName;
import com.biosphere.library.vo.WikiDetailVo;
import com.biosphere.wikimodule.mapper.AnimalsWikiMapper;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.wikimodule.service.IAnimalsWikiService;
import com.biosphere.library.vo.MainPageDataVo;
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
@Service
public class AnimalsWikiServiceImpl extends ServiceImpl<AnimalsWikiMapper, AnimalsWiki> implements IAnimalsWikiService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<MainPageDataVo> getMainPageAnimalData() {
        try{
            List<MainPageDataVo> mainPageDataVoList = (List<MainPageDataVo>) redisTemplate.opsForValue().get("animalDataVoList");
            return mainPageDataVoList;

        }catch (Exception e){
            log.error("获取动物主页信息失败:{}",e);
            return null;
        }


    }

    @Override
    public List<AnimalsWiki> getCuringGuidePageAnimalList(String familyID) {
        try{
            List<Integer> scientificIDList = (List<Integer>) redisTemplate.opsForHash().get("scientificNameToScientificID",familyID);
            List<AnimalsWiki> res = new ArrayList<>();
            Map<String, Object> animalsMap = (Map<String, Object>) redisTemplate.opsForHash().entries("animalsWikiMap");
            for (Object values: animalsMap.values()){
                AnimalsWiki temp = (AnimalsWiki) values;
                if (scientificIDList.contains(temp.getScientificNameID())) {
                    //处理图片
                    temp.setImage(temp.getImage().split("，")[0]);
                    res.add(temp);
                }
            }
            return res;


        }catch (Exception e){
            log.error("获取养护指南动物列表失败：{}",e);
            return null;

        }
    }

    @Override
    public WikiDetailVo getAnimalDetail(String ID) {
        AnimalsWiki animalsWiki = new AnimalsWiki();
        ScientificName scientificName = new ScientificName();
        try {
            animalsWiki = (AnimalsWiki) redisTemplate.opsForHash().get("animalsWikiMap",ID);
            scientificName = (ScientificName) redisTemplate.opsForHash().get("scientificNameMap",animalsWiki.getScientificNameID().toString());
        }catch (Exception e){
            log.error("获取动物详情失败：{}",e);
            return null;
        }
        WikiDetailVo wikiDetailVo = new WikiDetailVo();
        List<String> title = new ArrayList<>();
        List<String> content = new ArrayList<>();
        List<String> imageList = new ArrayList<>();
        //顺序一定要一致
        title.add("学名");
        title.add("性别");
        title.add("状况");
        title.add("绝育情况");
        title.add("领养情况");
        title.add("常出没地");
        title.add("性格");
        title.add("关系");
        title.add("外貌");
        content.add(scientificName.getName());
        content.add(animalsWiki.getSex());
        content.add(animalsWiki.getCondition());
        content.add(animalsWiki.getSterilization());
        content.add(animalsWiki.getAdoptCondition()==0?"未被领养":"已被领养");
        content.add(animalsWiki.getLocation());
        content.add(animalsWiki.getCharacter());
        content.add(animalsWiki.getRelationship());
        content.add(animalsWiki.getAppearance());
        imageList = Arrays.asList(animalsWiki.getImage().split("，"));
        //关系处理
        List<Map<String, String>> relation = new ArrayList<>();
        if (animalsWiki.getRelationID() != null) {
            String[] relationList = animalsWiki.getRelationID().split(",");
            try {
                for (int i = 0; i < relationList.length; i ++){
                    Map<String, String> map = new HashMap<>();
                    AnimalsWiki temp = (AnimalsWiki) redisTemplate.opsForHash().get("animalsWikiMap",relationList[i].toString());
                    map.put("id", temp.getId().toString());
                    map.put("name", temp.getNickName());
                    map.put("imageUrl", temp.getImage().split("，")[0]);
                    relation.add(map);
                }
            }catch (Exception e){
                log.error("获取动物详情关系详情错误:",e);
            }
        }

        //汇总数据
        wikiDetailVo.setImageList(imageList);
        wikiDetailVo.setNickName(animalsWiki.getNickName());
        wikiDetailVo.setContent(content);
        wikiDetailVo.setTitle(title);
        wikiDetailVo.setRelation(relation);

        return wikiDetailVo;
    }
}

package com.biosphere.wikimodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.pojo.PlantsWiki;
import com.biosphere.library.vo.*;
import com.biosphere.wikimodule.service.IAnimalsWikiService;
import com.biosphere.wikimodule.service.ICuringGuideService;
import com.biosphere.wikimodule.service.IPlantsWikiService;
import com.biosphere.wikimodule.util.DataAutoLoadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
@RestControllerAdvice
@Api(tags = "百科模块")
@Slf4j
@CrossOrigin
@RequestMapping("/wikiData")
public class WikiDataController implements InitializingBean{

    @Autowired
    private DataAutoLoadUtil dataAutoLoadUtil;

    @Autowired
    private IAnimalsWikiService animalsWikiService;

    @Autowired
    private IPlantsWikiService plantsWikiService;

    @Autowired
    private ICuringGuideService curingGuideService;


    @ApiOperation(value = "获取百科首页动植物内容", notes = "无需传参")
    @RequestMapping(value = "/exposure/getWikiMainPage",method = RequestMethod.GET)
    public JSONObject getWikiMainPage(){
        JSONObject resData = new JSONObject();

        List<MainPageDataVo> animalData = animalsWikiService.getMainPageAnimalData();
        List<MainPageDataVo> plantsData = plantsWikiService.getMainPagePlantsData();
        if (Objects.isNull(animalData) || Objects.isNull(plantsData)) {
            throw new ExceptionLogVo(RespBeanEnum.GET_WIKI_ERROR);
        }
        else {
            resData.put("animalData", animalData);
            resData.put("plantsData", plantsData);
        }

        return resData;
    }

    @ApiOperation(value = "获取养护指南页面内容", notes = "需要传入动植物类别以及科所属")
    @RequestMapping(value = "/exposure/getCuringGuideData/{animalOrPlant}/{familyID}",method = RequestMethod.GET)
    public JSONObject getCuringGuideData(@PathVariable("animalOrPlant") String animalOrPlant, @PathVariable("familyID") String familyID){
        JSONObject resData = new JSONObject();
        List<CuringGuideWithKeyContent> curingGuideList = curingGuideService.getCuringGuideData(familyID);
        //如果是动物
        if (animalOrPlant.equals("1")) {
            List<AnimalsWiki> animalsWikiList = animalsWikiService.getCuringGuidePageAnimalList(familyID);
            if (Objects.isNull(animalsWikiList) || Objects.isNull(curingGuideList)) {
                throw new ExceptionLogVo(RespBeanEnum.GET_GUIDE_ERROR);
            }
            else {
                resData.put("curingGuide",curingGuideList);
                resData.put("targetList",animalsWikiList);
            }
        } else {
            List<PlantsWiki> plantsWikiList = plantsWikiService.getCuringGuidePagePlantsList(familyID);
            if (Objects.isNull(plantsWikiList) || Objects.isNull(curingGuideList)) {
                throw new ExceptionLogVo(RespBeanEnum.GET_GUIDE_ERROR);
            }
            else {
                resData.put("curingGuide",curingGuideList);
                resData.put("targetList",plantsWikiList);
            }
        }

        return resData;
    }


    @ApiOperation(value = "获取百科详情", notes = "需要传入动植物判断和id")
    @RequestMapping(value = "/exposure/getWikiDetail/{animalOrPlant}/{ID}",method = RequestMethod.GET)
    public JSONObject getWikiDetail(@PathVariable("animalOrPlant") String animalOrPlant, @PathVariable("ID") String ID){
        JSONObject resData = new JSONObject();
        //如果是动物
        if (animalOrPlant.equals("1")) {
            WikiDetailVo animalDetail = animalsWikiService.getAnimalDetail(ID);
            if (Objects.isNull(animalDetail)) {
                throw new ExceptionLogVo(RespBeanEnum.GET_DETAIL_ERROR);
            }
            else {
                resData.put("detail",animalDetail);
            }
        }else {
            WikiDetailVo plantDetail = plantsWikiService.getPlantDetail(ID);
            if (Objects.isNull(plantDetail)) {
                throw new ExceptionLogVo(RespBeanEnum.GET_DETAIL_ERROR);
            }
            else {
                resData.put("detail",plantDetail);
            }
        }
        return resData;
    }


    @ApiOperation(value = "返回百科搜索结果", notes = "需要传入搜索类型、搜索内容")
    @RequestMapping(value = "/exposure/search",method = RequestMethod.GET)
    public JSONObject uploadComment(Integer type, String content){
        JSONObject resData = new JSONObject();
        List<Map<String, Object>> searchRes = curingGuideService.getSearchResult(content);
        resData.put("searchResult",searchRes);
        return resData;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dataAutoLoadUtil.wikiMainPageData();
    }
}

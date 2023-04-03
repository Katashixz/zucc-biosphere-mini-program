package com.biosphere.adoptmodule.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.biosphere.adoptmodule.service.IAnimalConditionService;
import com.biosphere.adoptmodule.service.IAnimalsWikiService;
import com.biosphere.adoptmodule.service.IShopItemService;
import com.biosphere.adoptmodule.util.AutoMission;
import com.biosphere.library.pojo.AnimalCondition;
import com.biosphere.library.pojo.ShopItem;
import com.biosphere.library.pojo.ViewTodayCondition;
import com.biosphere.library.vo.AdoptPageVo;
import com.biosphere.library.vo.ConditionReleaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyh
 * @since 2023-03-29
 */
@RestControllerAdvice
@Api(tags = "领养模块")
@Slf4j
@CrossOrigin
@Validated
@RequestMapping("/adopt")
public class AdpotController implements InitializingBean {

    @Autowired
    private AutoMission autoMission;

    @Autowired
    private IAnimalsWikiService animalsWikiService;

    @Autowired
    private IAnimalConditionService animalConditionService;

    @Autowired
    private IShopItemService shopItemService;

    @ApiOperation(value = "返回云领养主页数据", notes = "无需传参")
    @RequestMapping(value = "/exposure/getHomelessAnimals",method = RequestMethod.GET)
    public List<AdoptPageVo> getHomelessAnimals(){

        return animalsWikiService.getAdoptPageInfo();
    }

    @ApiOperation(value = "返回云喂养商品列表", notes = "无需传参")
    @RequestMapping(value = "/exposure/getShopItem",method = RequestMethod.GET)
    public List<ShopItem> getShopItem(@Validated @NotNull Integer type){

        return shopItemService.getShopList(type);
    }

    @ApiOperation(value = "插入今日状况", notes = "需要传入行为来源的用户ID，动物ID，状况类型")
    @RequestMapping(value = "/auth/saveCondition",method = RequestMethod.POST)
    public void saveCondition(@Validated@RequestBody AnimalCondition animalCondition){
        animalConditionService.saveAnimalContidion(animalCondition);
    }

    @ApiOperation(value = "获取今日状况", notes = "需要传入targetID,date")
    @RequestMapping(value = "/exposure/getTodayCondition",method = RequestMethod.GET)
    public List<ViewTodayCondition> getTodayCondition(Integer targetID, String date){
        return animalConditionService.getTodayCondition(targetID, date);
    }

    @ApiOperation(value = "上传今日状况", notes = "需要传入sourceID,targetID,action,createdAt,imageUrl")
    @RequestMapping(value = "/auth/saveConditionPersonally",method = RequestMethod.POST)
    public void saveConditionPersonally(@Validated @RequestBody ConditionReleaseVo object){
        animalConditionService.saveConditionPersonally(object);

    }


    @Override
    public void afterPropertiesSet(){
        autoMission.loadScientificName();
    }

}

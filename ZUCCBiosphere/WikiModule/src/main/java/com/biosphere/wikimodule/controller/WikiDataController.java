package com.biosphere.wikimodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.library.pojo.AnimalsWiki;
import com.biosphere.library.pojo.CuringGuide;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
@RestController
@Api(tags = "百科模块")
@Slf4j
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
    public ResponseResult getWikiMainPage(){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();

        List<MainPageDataVo> animalData = animalsWikiService.getMainPageAnimalData();
        List<MainPageDataVo> plantsData = plantsWikiService.getMainPageAnimalData();
        if (Objects.isNull(animalData) || Objects.isNull(plantsData)) {
            res.setCode(RespBeanEnum.GET_WIKI_ERROR.getCode());
            res.setMsg(RespBeanEnum.GET_WIKI_ERROR.getMessage());

        }
        else {
            res.setCode(RespBean.success().getCode());
            res.setMsg(RespBean.success().getMessage());
            resData.put("animalData", animalData);
            resData.put("plantsData", plantsData);
            res.setData(resData);
        }

        return res;
    }

    @ApiOperation(value = "获取养护指南页面内容", notes = "需要传入动植物类别以及科所属")
    @RequestMapping(value = "/exposure/getCuringGuideData/{animalOrPlant}/{familyID}",method = RequestMethod.GET)
    public ResponseResult getCuringGuideData(@PathVariable("animalOrPlant") String animalOrPlant, @PathVariable("familyID") String familyID){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        List<CuringGuideWithKeyContent> curingGuideList = curingGuideService.getCuringGuideData(familyID);
        //如果是动物
        if (animalOrPlant.equals("1")) {
            List<AnimalsWiki> animalsWikiList = animalsWikiService.getCuringGuidePageAnimalList(familyID);
            if (Objects.isNull(animalsWikiList) || Objects.isNull(curingGuideList)) {
                res.setCode(RespBeanEnum.GET_GUIDE_ERROR.getCode());
                res.setMsg(RespBeanEnum.GET_GUIDE_ERROR.getMessage());
            }
            else {
                res.setCode(RespBean.success().getCode());
                res.setMsg(RespBean.success().getMessage());
                resData.put("curingGuide",curingGuideList);
                resData.put("targetList",animalsWikiList);
                res.setData(resData);
            }
        } else {
            List<PlantsWiki> plantsWikiList = plantsWikiService.getCuringGuidePagePlantsList(familyID);
            if (Objects.isNull(plantsWikiList) || Objects.isNull(curingGuideList)) {
                res.setCode(RespBeanEnum.GET_GUIDE_ERROR.getCode());
                res.setMsg(RespBeanEnum.GET_GUIDE_ERROR.getMessage());
            }

            else {
                res.setCode(RespBean.success().getCode());
                res.setMsg(RespBean.success().getMessage());
                resData.put("curingGuide",curingGuideList);
                resData.put("targetList",plantsWikiList);

                res.setData(resData);
            }
        }

        return res;
    }


    @ApiOperation(value = "获取百科详情", notes = "需要传入动植物判断和id")
    @RequestMapping(value = "/exposure/getWikiDetail/{animalOrPlant}/{ID}",method = RequestMethod.GET)
    public ResponseResult getWikiDetail(@PathVariable("animalOrPlant") String animalOrPlant, @PathVariable("ID") String ID){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        //如果是动物
        if (animalOrPlant.equals("1")) {
            WikiDetailVo animalDetail = animalsWikiService.getAnimalDetail(ID);
            if (Objects.isNull(animalDetail)) {
                res.setCode(RespBeanEnum.GET_DETAIL_ERROR.getCode());
                res.setMsg(RespBeanEnum.GET_DETAIL_ERROR.getMessage());
            }
            else {
                res.setCode(RespBean.success().getCode());
                res.setMsg(RespBean.success().getMessage());
                resData.put("detail",animalDetail);
                res.setData(resData);
            }
        }else {
            WikiDetailVo plantDetail = plantsWikiService.getPlantDetail(ID);
            if (Objects.isNull(plantDetail)) {
                res.setCode(RespBeanEnum.GET_DETAIL_ERROR.getCode());
                res.setMsg(RespBeanEnum.GET_DETAIL_ERROR.getMessage());
            }
            else {
                res.setCode(RespBean.success().getCode());
                res.setMsg(RespBean.success().getMessage());
                resData.put("detail",plantDetail);
                res.setData(resData);
            }
        }
        return res;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        dataAutoLoadUtil.wikiMainPageData();
    }
}